package com.example.weightliftingapp

class User(id :Int) {
    private var id = id
    var workouts = mutableListOf<Workout>()

    fun getId():Int{
        return id
    }


    override fun toString(): String {
        return "User(workouts=$workouts)"
    }


}