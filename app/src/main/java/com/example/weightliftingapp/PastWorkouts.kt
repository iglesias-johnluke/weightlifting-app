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

    override fun onResume() {
        super.onResume()
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        Log.d("organizeWorkouts mainActivity", requireActivity().toString())
        Log.i("organizeWorkouts", sharedViewModel.databaseManager.toString())
        sharedViewModel.databaseManager.getUserData(::organizeWorkouts)

//        val user = FirebaseAuth.getInstance().currentUser
//        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
//        sharedViewModel.databaseManager = DatabaseManager(user!!.uid)
//        Log.d("organizeWorkouts mainActivity", requireActivity().toString())
//        Log.i("organizeWorkouts", sharedViewModel.databaseManager.toString())
//        sharedViewModel.databaseManager.getUserData(::organizeWorkouts)

    }

    private fun organizeWorkouts(hashMap: HashMap<String, Any>) {
        Log.i("organizeWorkouts user data", hashMap.toString())
    }

}