package com.example.weightliftingapp

data class PastWorkoutViewModel(
    val workoutDate: String,
    val workoutInfo: String,
    val workoutData: String
)


//class PastWorkoutViewModel {
//    private  lateinit var workoutDate: Date
//    private lateinit var workoutInfo: String
//    private lateinit var workoutData: List<String>
//
//    fun constructor(workoutDate: Date, workoutInfo: String, workoutData: List<String>) {
//        setWorkoutDate(workoutDate)
//        setWorkoutInfo(workoutInfo)
//        setWorkoutData(workoutData)
//    }
//
//    private fun setWorkoutData(workoutData: List<String>): List<String> {
//        return workoutData
//    }
//
//    private fun setWorkoutInfo(workoutInfo: String): String {
//        return workoutInfo
//    }
//
//    private fun setWorkoutDate(workoutDate: Date): Date {
//        return workoutDate
//    }


