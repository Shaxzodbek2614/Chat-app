package com.example.chatapp.models

class Message {
    var text:String? = null
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

    override fun toString(): String {
        return "Message(text=$text, fromUserUid=$fromUserUid, toUserUid=$toUserUid, date=$date)"
    }


}