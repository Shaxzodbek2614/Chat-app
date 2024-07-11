package com.example.chatapp

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.databinding.ActivitySearchBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchActivity : AppCompatActivity() ,UserAdapter.RvAction{
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var list: ArrayList<User>
    lateinit var auth: FirebaseAuth
    lateinit var reference: com.google.firebase.database.DatabaseReference
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.search.requestFocus()
        binding.search.setQuery("", false)
        binding.search.isIconified = false
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        val myLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rv.apply {
            layoutManager = myLayoutManager
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchActivity,
                    myLayoutManager.orientation
                )
            )
        }
        binding.chiqish.setOnClickListener {
            finish()
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

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SearchActivity, "${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                binding.searchRv.adapter = RvAdapter(this@SearchActivity3, list)
                val l = ArrayList<User>()
                for (user in list) {
                    if (user.name?.lowercase()!!.contains(newText!!.lowercase()) && newText.isNotBlank()) {
                        l.add(user)
                    }
                }
                binding.rv.adapter = UserAdapter(this@SearchActivity, l)
                return true
            }
        })


    }

    override fun itemClick(user: User) {

    }
}