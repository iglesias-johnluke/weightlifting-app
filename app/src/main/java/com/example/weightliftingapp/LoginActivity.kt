package com.example.weightliftingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.weightliftingapp.databinding.ActivityLoginBinding
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth //firebase authenticator instance



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Initialize Firebase Auth
        auth = Firebase.auth





    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){ //if user signed in, open homepage
            val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        }

    }

    fun signupOnClick(view: View){
        val i = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(i)
    }


    /*authenticates user credentials*/
     fun loginOnClick(view: View){
        Log.d("clicked", "true")
        val myIntent = Intent(this@LoginActivity, MainActivity::class.java)

        startActivity(myIntent)
        finish()

        }





}