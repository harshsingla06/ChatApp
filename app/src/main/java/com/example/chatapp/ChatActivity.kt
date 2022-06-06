package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String? = null
    var senderRoom: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()
        val name = intent.getStringExtra("name")
        val receiverUid =intent.getStringExtra("uid")

        val senderUid= FirebaseAuth.getInstance().currentUser?.uid
        mDbRef= FirebaseDatabase.getInstance().getReference()
        senderRoom=receiverUid+senderUid
        receiverRoom=senderUid+receiverUid


        findViewById<TextView>(R.id.person).text=name
        messageRecyclerView= findViewById(R.id.recycler)
        messageBox=findViewById(R.id.sendMessage)
        sendButton=findViewById(R.id.sendimg)
        messageList= ArrayList()
        messageAdapter=MessageAdapter(messageList)

        messageRecyclerView.layoutManager= LinearLayoutManager(this)
        messageRecyclerView.adapter=messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message= postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        sendButton.setOnClickListener{
            val message = messageBox.text.toString()
            val messageObject= Message(message,senderUid)
            if (message!=""){
            mDbRef.child("chats").child(senderRoom!!).child("messages").push().
            setValue(messageObject).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push().
                setValue(messageObject)
                messageBox.setText("")
            }}
        }

    }
}