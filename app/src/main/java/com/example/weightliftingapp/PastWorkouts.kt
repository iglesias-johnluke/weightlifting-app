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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val user = FirebaseAuth.getInstance().currentUser
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.databaseManager = DatabaseManager(user!!.uid)
        val firebaseData = sharedViewModel.databaseManager.getUserData(::organizeWorkouts)
        val rootview: View =
            inflater.inflate(R.layout.fragment_browse_pastworkouts, container, false)
        val recyclerView: RecyclerView = rootview.findViewById(R.id.pastworkoutrecycler)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val data = ArrayList<PastWorkoutViewModel>()
        val viewModel = PastWorkoutViewModel(
            "Monday, December 1",
            "Exercise - Sets - Reps - Weight",
            "Bench Press - 6 sets - 8 reps - 135 lbs"
        )
        data.add(viewModel)
        val adapter = PastWorkoutsAdapter(data)
        recyclerView.adapter = adapter
        return rootview
    }

    private fun organizeWorkouts(hashMap: HashMap<String, Any>) {
        Log.i("data: ", hashMap.toString())
    }

}