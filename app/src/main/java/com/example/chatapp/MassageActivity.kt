package com.example.chatapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.databinding.ActivityMassageBinding
import com.example.chatapp.models.Message
import com.example.chatapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class MassageActivity : AppCompatActivity(),MessageAdapter.RvAction {
    lateinit var currentUser: String
    lateinit var user: User
    lateinit var messageAdapter: MessageAdapter
    lateinit var list: ArrayList<Message>
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var referenceStorage: StorageReference
    lateinit var reference: DatabaseReference
    private val binding by lazy { ActivityMassageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseStorage = FirebaseStorage.getInstance()
        referenceStorage = firebaseStorage.getReference("myPhotosMessage")
        currentUser = intent.getStringExtra("uid").toString()
        user = intent.getSerializableExtra("user") as User
        list = ArrayList()
        messageAdapter = MessageAdapter(this,list, currentUser)
        binding.rv.adapter = messageAdapter
        binding.name.text = user.name
        Picasso.get().load(user.photoUrl).into(binding.image)


        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        binding.apply {
            btnSend.setOnClickListener {
                val text = edtSend.text.toString()
                if (uri != null && text.isNotBlank()) {
                    val m = System.currentTimeMillis()
                    val task = referenceStorage.child(m.toString()).putFile(uri!!)
                    btnX.isEnabled = true
                    btnSend.isEnabled = false
                    edtSend.isEnabled = false
                    progress.visibility = View.VISIBLE

                    task.addOnSuccessListener {
                        if (it.task.isSuccessful) {
                            val downloadURL = it.metadata?.reference?.downloadUrl
                            downloadURL?.addOnSuccessListener { imageURL ->
                                imageUrl = imageURL.toString()
                                val message = Message(text, imageUrl,currentUser,user.uid,getDate())
                                val key = reference.push().key!!

                                reference.child(user.uid!!).child("messages").child(currentUser)
                                    .child(key).setValue(message)

                                reference.child(currentUser).child("messages").child(user.uid!!)
                                    .child(key).setValue(message)

                                uri = null

                                btnX.isEnabled = true
                                btnSend.isEnabled = true
                                edtSend.isEnabled = true
                                binding.edtSend.text.clear()
                                progress.visibility = View.GONE
                                sendImageLayout.visibility = View.GONE
                            }
                        }
                    }
                    task.addOnFailureListener {
                        Toast.makeText(this@MassageActivity, "Error " + it.message, Toast.LENGTH_SHORT).show()
                    }
                } else if (uri == null && text.isNotBlank()) {
                    val message = Message(text, currentUser, user.uid, getDate())
                    val key = reference.push().key!!

                    reference.child(user.uid!!).child("messages").child(currentUser)
                        .child(key).setValue(message)

                    reference.child(currentUser).child("messages").child(user.uid!!)
                        .child(key).setValue(message)

                    binding.edtSend.text.clear()

                } else if (uri != null && text.isBlank()) {
                    val m = System.currentTimeMillis()
                    val task = referenceStorage.child(m.toString()).putFile(uri!!)
                    btnX.isEnabled = false
                    btnSend.isEnabled = false
                    edtSend.isEnabled = false
                    progress.visibility = View.VISIBLE

                    task.addOnSuccessListener {
                        if (it.task.isSuccessful) {
                            val downloadURL = it.metadata?.reference?.downloadUrl
                            downloadURL?.addOnSuccessListener { imageURL ->
                                imageUrl = imageURL.toString()
                                val message = Message("", imageUrl, currentUser, user.uid, getDate())
                                val key = reference.push().key!!

                                reference.child(user.uid!!).child("messages").child(currentUser)
                                    .child(key).setValue(message)

                                reference.child(currentUser).child("messages").child(user.uid!!)
                                    .child(key).setValue(message)

                                uri = null
                                btnX.isEnabled = true
                                btnSend.isEnabled = true
                                edtSend.isEnabled = true
                                binding.edtSend.text.clear()
                                progress.visibility = View.GONE
                                sendImageLayout.visibility = View.GONE
                            }
                        }
                    }
                    task.addOnFailureListener {
                        Toast.makeText(this@MassageActivity, "Error " + it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        reference.child(currentUser).child("messages").child(user.uid?:"")
            .addValueEventListener(object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    val children = snapshot.children
                    for (child in children) {
                        val message = child.getValue(Message::class.java)
                        if (message != null) {
                            list.add(message)
                        }
                    }
                    binding.rv.scrollToPosition(list.size - 1)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        binding.imageJonat.setOnClickListener {
            getImageContent.launch("image/*")
        }
        binding.btnX.setOnClickListener {
            uri = null
            binding.sendImageLayout.visibility = View.GONE
            binding.progress.visibility = View.GONE
        }

    }
    var imageUrl = ""
    var uri: Uri? = null
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) { it ->
        it?: return@registerForActivityResult
        uri = it
        binding.sendImageLayout.visibility = View.VISIBLE
        binding.sendImage.setImageURI(it)
    }

    fun getDate(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return simpleDateFormat.format(date)
    }

    override fun imageClick(message: Message) {
        println("Ishlayapti")
        val intent = Intent(this,ImageActivity::class.java)
        intent.putExtra("key", message)
        startActivity(intent)
        println("ishlayapti 2")
    }
}