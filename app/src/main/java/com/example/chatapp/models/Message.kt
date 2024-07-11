package com.example.chatapp.models

import android.net.Uri
import java.io.Serializable

class Message :Serializable{
    var text:String? = null
    var imageUri:String? = null
    var fromUserUid:String? = null
    var toUserUid:String? = null
    var date:String? = null

    constructor()
    constructor(text: String?, fromUserUid: String?, toUserUid: String?, date: String?) {
        this.text = text
        this.fromUserUid = fromUserUid
        this.toUserUid = toUserUid
        this.date = date
    }

    constructor(
        text: String?,
        imageUri: String?,
        fromUserUid: String?,
        toUserUid: String?,
        date: String?
    ) {
        this.text = text
        this.imageUri = imageUri
        this.fromUserUid = fromUserUid
        this.toUserUid = toUserUid
        this.date = date
    }


    override fun toString(): String {
        return "Message(text=$text, fromUserUid=$fromUserUid, toUserUid=$toUserUid, date=$date)"
    }


}