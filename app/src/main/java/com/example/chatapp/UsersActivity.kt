package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.databinding.ActivityUsersBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UsersActivity : AppCompatActivity(),UserAdapter.RvAction {
    lateinit var list:ArrayList<User>
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: com.google.firebase.database.DatabaseReference
    private val binding by lazy { ActivityUsersBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        Picasso.get().load(auth.currentUser?.photoUrl).into(binding.imageProfile)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        val myLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)

        binding.qidirish.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        binding.rv.apply {
            layoutManager = myLayoutManager
            addItemDecoration(DividerItemDecoration(this@UsersActivity, myLayoutManager.orientation))
        }

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val user = child.getValue(User::class.java)
                    if (user?.uid != auth.uid) {
                        list.add(user!!)
                    }
                }
                binding.rv.adapter = UserAdapter(this@UsersActivity,list)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun itemClick(user: User) {
        val intent = Intent(this,MassageActivity::class.java)
        intent.putExtra("user",user)
        intent.putExtra("uid",auth.uid)
        startActivity(intent)
    }
}