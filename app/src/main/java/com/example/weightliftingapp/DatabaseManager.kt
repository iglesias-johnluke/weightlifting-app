package com.example.weightliftingapp

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

/**class for managing reading and writing to user data object within firebase database |
 * this class will be instantiated within MainActivity.onSignInResult (a successful user sign-in)
 * and will be accessible within the sharedViewModel*/
class DatabaseManager(userID: String) {
    val database = Firebase.database.getReference("users")
    val userID = userID
    lateinit var eventListener: ValueEventListener //listens for changes to user data in firebase
    val PUSH_MUSCLE_GROUP = "push"
    val PULL_MUSCLE_GROUP = "pull"
    val LEGS_MUSCLE_GROUP = "legs"
    val NONE_MUSCLE_GROUP = "none"

    /**adds a workout object to user database,
     * does not perform action if parameter is null or workoutData does not have a name
     * or if userID not initialized*/
    fun addWorkout(workoutData: Workout){
        if(workoutData == null || workoutData.name == null || userID == null ){
            return
        }
        database.child(userID).child("workouts").child(workoutData.name)
            .setValue(workoutData)
            .addOnSuccessListener {
                Log.d("firebase", "SUCCESSFULLY to add workout")
            }
            .addOnFailureListener{
                Log.d("firebase", "FAILED to add workout due to: " + it)
            }

    }

    /**removes all user workouts from database*/
    fun clearWorkouts(){
        if(userID == null){
            return
        }
        database.child(userID).child("workouts").setValue(null)
            .addOnSuccessListener {
                Log.d("firebase", "SUCCESSFULLY cleared workouts")
            }
            .addOnFailureListener{
                Log.d("firebase", "FAILED to clear workouts due to: " + it)
            }
    }

    /**explicit get method for retrieving user data, |
     * onSuccessful invokes each functionReference parameter with the user data hashmap |
     * this just is another way to retrieve user data as compared to onDataChange()
     * callback
     * */
    fun getUserData(vararg functionReferences: (data: HashMap<String, Any>) -> Unit ){
        database.child(userID).get().addOnSuccessListener {
            if(it.value != null){
                val data : HashMap<String, Any> = it.value as HashMap<String, Any>
                for(f in functionReferences){
                    f(data)
                }
                Log.d("getUserData", "Got value ${it.value}")
            }else{
                Log.d("getUserData", "it.value is null")
            }

        }.addOnFailureListener{
            Log.d("getUserData", "Error getting data", it)
        }
    }

    /**listens for changes of userdata and calls each functionReference argument given the user data |
     * function call can have any number of arguments, only accepts function references
     * as arguments | each function reference can only accept one argument (the data hashmap) */
    fun setDataListener(vararg functionReferences: (data: HashMap<String, Any>) -> Unit){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get data hashmap object and use the values to update the UI
                if(dataSnapshot.value == null){
                    return
                }
                val data = dataSnapshot.getValue<HashMap<String, Any>>()!!
                for(f in functionReferences){//call each functionReference with user data map
                    f(data)
                }
                // ...
                Log.d("firebase", "DATA CHANGED")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        this.eventListener = postListener
        database.child(userID).addValueEventListener(postListener)
    }

    /**function demonstrating how to create workoutData instance and add
     * workout to firebase; this is just for reference; uncomment last 2 statements
     * to either clear or add workouts*/
    fun demo(){
        clearWorkouts()
        val workoutData = Workout(name = "MY WORKOUT", date = "2022-01-27")

        val exercise = Exercise(
            name = "Pull-Ups", muscleGroup = PUSH_MUSCLE_GROUP,
            reps = 2, weight = 120, sets = 3
        )
        val exerciseMap = HashMap<String, Any>()
        exerciseMap.put("Pull-Ups", exercise)
        val exercise2 = Exercise(
            name = "Bench Press", muscleGroup = NONE_MUSCLE_GROUP,
            reps = 8, weight = 100, sets = 3
        )
        exerciseMap.put(exercise2.name!!, exercise2)
        workoutData.exercises = exerciseMap

        addWorkout(workoutData)
//        clearWorkouts()
    }
    data class Exercise(
        val name: String? = null,
        val reps: Int? = null,
        val weight: Int? = null,
        val sets: Int? = null,
        val muscleGroup: String? = null,

        )

    data class Workout(
        val name: String? = null,
        //exercises map has exercise names as the keys and exercise objects as the values
        var exercises: HashMap<String, Any>? = null,
        //date string is in format "yyyy-mm-dd"
        val date: String? = null
    )



}