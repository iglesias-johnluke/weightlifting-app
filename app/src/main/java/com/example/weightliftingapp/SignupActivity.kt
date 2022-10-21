package com.example.weightliftingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.weightliftingapp.databinding.ActivitySignupBinding

lateinit var binding: ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
    }

    fun registerOnClick(view: View){
        finish()
    }
}