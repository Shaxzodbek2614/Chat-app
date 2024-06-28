package com.example.chatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class MassageActivity : AppCompatActivity() {
    lateinit var currentUser: String
    lateinit var user: User
    lateinit var messageAdapter: MessageAdapter
    lateinit var list: ArrayList<Message>
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    private val binding by lazy { ActivityMassageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        currentUser = intent.getStringExtra("uid").toString()
        user = intent.getSerializableExtra("user") as User
        list = ArrayList()
        messageAdapter = MessageAdapter(list, currentUser)
        binding.rv.adapter = messageAdapter
        binding.name.text = user.name
        Picasso.get().load(user.photoUrl).into(binding.image)


        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        binding.btnSend.setOnClickListener {
            val text = binding.edtSend.text.toString()
            if (text.isNotEmpty()) {
                val message = Message(binding.edtSend.text.toString(), currentUser, user.uid, getDate())
                val key = reference.push().key
                reference.child(user.uid ?: "").child("messages").child(currentUser)
                    .child(key ?: "").setValue(message)

                reference.child(currentUser).child("messages").child(user.uid ?: "")
                    .child(key ?: "").setValue(message)

            }
            binding.edtSend.setText("")
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

    }

    fun getDate(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return simpleDateFormat.format(date)
    }
}