package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.FromItemBinding
import com.example.chatapp.databinding.ToItemBinding
import com.example.chatapp.models.Message

class MessageAdapter(val list: ArrayList<Message>, val currentUserUid: String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_FROM = 1
    val TYPE_TO = 0
    inner class ToVh(val itemRvBinding: ToItemBinding): RecyclerView.ViewHolder(itemRvBinding.root){
        fun onBind(message: Message){
            itemRvBinding.message.text = message.text
        }
    }

    inner class FromVh(val itemRvBinding: FromItemBinding): RecyclerView.ViewHolder(itemRvBinding.root){
        fun onBind(message: Message){
            itemRvBinding.message.text = message.text
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       if (viewType==0){
           return ToVh(ToItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
       }else{
           return FromVh(FromItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
       }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ToVh){
            holder.onBind(list[position])
        }else if (holder is FromVh){
            holder.onBind(list[position])
        }
    }
    override fun getItemViewType(position: Int): Int {
        if (list[position].fromUserUid == currentUserUid) {
            return TYPE_FROM
        } else {
            return TYPE_TO
        }
    }
}