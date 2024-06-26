package com.example.chatapp.models

 class User{
     var name:String? = null
     var uid:String? = null
     var photoUrl:String? = null

     constructor()
     constructor(name: String?, uid: String?, photoUrl: String?) {
         this.name = name
         this.uid = uid
         this.photoUrl = photoUrl
     }
 }
