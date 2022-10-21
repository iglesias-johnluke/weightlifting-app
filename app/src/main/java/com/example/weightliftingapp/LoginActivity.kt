package com.example.weightliftingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.weightliftingapp.databinding.ActivityLoginBinding
import androidx.core.content.ContextCompat.startActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)



    }


    /*authenticates user credentials*/
     fun loginOnClick(view: View){
        Log.d("clicked", "true")
        val myIntent = Intent(this@LoginActivity, MainActivity::class.java)

        startActivity(myIntent)
        finish()

        }





}