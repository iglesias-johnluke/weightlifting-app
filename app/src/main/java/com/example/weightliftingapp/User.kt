package com.example.weightliftingapp

class User(id :String? = null) {
    private var id = id
    var workouts = mutableListOf<Workout>()

    fun getId(): String? {
        return id
    }


    override fun toString(): String {
        return "User(workouts=$workouts)"
    }


}