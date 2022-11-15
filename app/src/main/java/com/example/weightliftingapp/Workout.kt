package com.example.weightliftingapp
import com.example.weightliftingapp.*
import java.time.Duration
import java.util.Date

class Workout(name:String? = null) {
    var exercises = mutableListOf<Exercise>()
    private var name = name
    private lateinit var date : Date
    private lateinit var duration: Duration

    fun getName(): String? {
        return name
    }
    fun setName(newName:String){
        name = newName
    }

    fun getDate():Date{
        return date
    }
    fun setDate(newDate:Date){
        date = newDate
    }

    fun setDuration(newDuration:Duration){
        duration = newDuration
    }
    fun getDuration():Duration{
        return duration
    }

    override fun toString(): String {
        return "Workout(exercises=$exercises, name='$name', date=$date, duration=$duration)"
    }


}