package com.example.weightliftingapp

import CustomExpandableListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment

class ExerciseCatalog : Fragment() {
    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var expandableListView =
            requireActivity().findViewById<ExpandableListView>(R.id.expendableList)

        if (expandableListView != null) {
            val listData = ExpandableListData.data
            var titleList = ArrayList(listData.keys)
            var adapter = CustomExpandableListAdapter(
                requireContext(),
                titleList as ArrayList<String>,
                listData
            )
            expandableListView.setAdapter(adapter)

        }
        return inflater.inflate(R.layout.fragment_browse_exercisecatalog, container, false)
    }
}