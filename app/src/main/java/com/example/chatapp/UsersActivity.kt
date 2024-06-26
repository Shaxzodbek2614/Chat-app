package com.example.chatapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.databinding.ActivityUsersBinding
import com.example.chatapp.models.User
import com.example.chatapp.utils.MyData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UsersActivity : AppCompatActivity() {
    lateinit var list:ArrayList<User>
    private val binding by lazy { ActivityUsersBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        MyData.databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list = ArrayList()
                val children = snapshot.children
                for (child in children) {
                    val user = child.getValue(User::class.java)
                    list.add(user!!)
                }
                binding.rv.adapter = UserAdapter(list)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}