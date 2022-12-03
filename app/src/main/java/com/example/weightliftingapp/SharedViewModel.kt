package com.example.weightliftingapp

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SharedViewModel : ViewModel() {
    lateinit var databaseManager: DatabaseManager


    fun isdbInit() = ::databaseManager.isInitialized

    /**initializes dbmanager*/
    fun setDatabaseManager(){
        val user = FirebaseAuth.getInstance().currentUser
        databaseManager = DatabaseManager(user!!.uid)

    }

}