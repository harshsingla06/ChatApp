package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.jar.Attributes

class SignUp : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp : Button
    private lateinit var mDbRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        name=findViewById(R.id.edt_name)
        edtEmail=findViewById(R.id.email)
        edtPassword=findViewById(R.id.password)
        btnSignUp=findViewById(R.id.edt_signUp)

        btnSignUp.setOnClickListener {
            val name = name.text.toString()
            val email = edtEmail.text.toString()
            val password= edtPassword.text.toString()

            signUp(name,email,password)
        }
    }
    private fun signUp(name: String,email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,mAuth.currentUser!!.uid,email)
                    // jumping to home
                    val intent = Intent(this@SignUp, MainActivity::class.java )
                    finish()
                    startActivity(intent)
                } else {
                    //failed
                    Toast.makeText(this,"Sign Up failed, please check your network connection and try again",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDatabase(name:String, uid: String , email:String){
        mDbRef= FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email, uid))
    }
}