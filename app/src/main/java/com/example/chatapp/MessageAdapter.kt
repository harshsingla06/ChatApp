package com.example.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val messages:ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val Item_receive=1
    val Item_sent=2
    class SentViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val sentMessage= view.findViewById<TextView>(R.id.txt)

    }
    class ReceivedViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val receivedMessage= view.findViewById<TextView>(R.id.txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            val view: View= LayoutInflater.from(parent.context).inflate(R.layout.received, parent,false)
            return ReceivedViewHolder(view)
        }
        else {
            val view: View= LayoutInflater.from(parent.context).inflate(R.layout.sent, parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currMessage=messages[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currMessage.senderId)){
            return Item_sent
        }
        else return Item_receive
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass==SentViewHolder::class.java){
            val currMessage= messages[position]
            val viewHolder=holder as SentViewHolder
            holder.sentMessage.text=currMessage.message
        }
        else {
            val currMessage= messages[position]
            val viewHolder=holder as ReceivedViewHolder
            holder.receivedMessage.text=currMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}