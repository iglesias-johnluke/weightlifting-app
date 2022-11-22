package com.example.weightliftingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PastWorkouts : Fragment() {

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

    fun organizeWorkouts(data: HashMap<String, Any>) {
       data.keys
    }

}