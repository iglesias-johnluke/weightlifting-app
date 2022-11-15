package com.example.weightliftingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class PastWorkoutsAdapter(private val mList: List<PastWorkoutViewModel>) :
    RecyclerView.Adapter<PastWorkoutsAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pastworkout, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pastWorkoutViewModel = mList[position]

        holder.workoutDateView.text = pastWorkoutViewModel.workoutDate
        holder.workoutInfoView.text = pastWorkoutViewModel.workoutInfo
        holder.workoutDataView.text = pastWorkoutViewModel.workoutData
        // sets the image to the imageview from our itemHolder class

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(PastWorkoutsView: View) : RecyclerView.ViewHolder(PastWorkoutsView) {
        val workoutDateView = PastWorkoutsView.findViewById<TextView>(R.id.WorkoutDate)
        val workoutInfoView = PastWorkoutsView.findViewById<TextView>(R.id.WorkoutInfo)
        val workoutDataView = PastWorkoutsView.findViewById<TextView>(R.id.WorkoutData)
    }
}
