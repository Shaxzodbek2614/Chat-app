package com.example.chatapp.utils

import com.google.firebase.database.FirebaseDatabase

object MyData {
    val firebasedatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebasedatabase.getReference("users")
}