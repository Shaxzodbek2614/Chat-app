package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.FromItemBinding
import com.example.chatapp.databinding.ToItemBinding
import com.example.chatapp.models.Message
import com.squareup.picasso.Picasso

class MessageAdapter(val rvAction: RvAction,val list: ArrayList<Message>, val currentUserUid: String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_FROM = 1
    val TYPE_TO = 0

    inner class ToVh(val itemRvBinding: ToItemBinding): RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(message: Message) {
            if (message.imageUri != null && message.text != "") {
                itemRvBinding.card.visibility = View.VISIBLE
                itemRvBinding.tvSms.visibility = View.VISIBLE
                itemRvBinding.tvSms.text = message.text
                Picasso.get().load(message.imageUri).into(itemRvBinding.image)
            } else if (message.imageUri != null && message.text!!.isBlank()) {

                itemRvBinding.card.visibility = View.VISIBLE
                itemRvBinding.tvSms.visibility = View.GONE
                Picasso.get().load(message.imageUri).into(itemRvBinding.image)
            } else if (message.imageUri == null && message.text != "") {
                itemRvBinding.card.visibility = View.GONE
                itemRvBinding.tvSms.visibility = View.VISIBLE
                itemRvBinding.tvSms.text = message.text
                Picasso.get().load(message.imageUri).into(itemRvBinding.image)
            }
            itemRvBinding.card.setOnClickListener {
                rvAction.imageClick(message)
            }
        }
    }

    inner class FromVh(val itemRvBinding: FromItemBinding): RecyclerView.ViewHolder(itemRvBinding.root){
        fun onBind(message: Message){
            if (message.imageUri != null && message.text != "") {
                itemRvBinding.card.visibility = View.VISIBLE
                itemRvBinding.tvSms.visibility = View.VISIBLE
                itemRvBinding.tvSms.text = message.text
                Picasso.get().load(message.imageUri).into(itemRvBinding.image)
            }else if (message.imageUri != null && message.text!!.isBlank()){
                itemRvBinding.card.visibility = View.VISIBLE
                itemRvBinding.tvSms.visibility = View.GONE
                Picasso.get().load(message.imageUri).into(itemRvBinding.image)
            } else if (message.imageUri == null && message.text != ""){
                itemRvBinding.card.visibility = View.GONE
                itemRvBinding.tvSms.visibility = View.VISIBLE
                itemRvBinding.tvSms.text = message.text
                Picasso.get().load(message.imageUri).into(itemRvBinding.image)
            }
            itemRvBinding.card.setOnClickListener {
                rvAction.imageClick(message)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==0){
            ToVh(ToItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            FromVh(FromItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
        return if (list[position].fromUserUid == currentUserUid) {
            TYPE_FROM
        } else {
            TYPE_TO
        }
    }
    interface RvAction{
        fun imageClick(message: Message)
    }
}