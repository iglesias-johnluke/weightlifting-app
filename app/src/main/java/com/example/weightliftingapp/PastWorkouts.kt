package com.example.weightliftingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class PastWorkouts : Fragment() {

    lateinit var sharedViewModel :SharedViewModel
    var pastWorkouts: ArrayList<PastWorkoutViewModel> = ArrayList()
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootview: View =
            inflater.inflate(R.layout.fragment_browse_pastworkouts, container, false)
        recyclerView = rootview.findViewById(R.id.pastworkoutrecycler)




        return rootview
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        if(sharedViewModel.isdbInit() == false){
            sharedViewModel.setDatabaseManager()
        }
        clearPastWorkoutRecyclerView()
        sharedViewModel.databaseManager.getUserData(::organizeWorkouts)

    }

    fun clearPastWorkoutRecyclerView(){
        recyclerView.adapter = null
    }

    private fun organizeWorkouts(data: HashMap<String, Any>) {
        Log.i("organizeWorkouts user data", data.toString())
        val workouts = data["workouts"] as HashMap<String, Any>
        for (w in workouts.keys) {
            val wrkt = workouts[w] as HashMap<String, Any>
            Log.i("w", w)
            Log.i("exercises", workouts[w].toString())
            Log.i("date", wrkt["date"].toString())

            val date = wrkt["date"].toString()
            val exercises = wrkt["exercises"] as HashMap<String, Any>
            val organizedExercises = organizeExercises(exercises)

            pastWorkouts.add(PastWorkoutViewModel(
                "Workout: $date",
                "Exercise - Muscle Group - Sets - Reps - Weight",
                organizedExercises
            ))
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PastWorkoutsAdapter(pastWorkouts)
        recyclerView.adapter = adapter
        Log.i("pastWorkouts", pastWorkouts.toString())



    }

    private fun organizeExercises(exercises: HashMap<String, Any>): String {
        var res = ""
        for (e in exercises.keys) {
            val exercise = exercises[e] as HashMap<String, Any>
            val exerciseStr = e + " - " + exercise["muscleGroup"].toString() + " - " + exercise["sets"].toString() + " - " + exercise["reps"].toString() + " - " + exercise["weight"].toString() + '\n'
            res += exerciseStr
        }
        return res
    }

}