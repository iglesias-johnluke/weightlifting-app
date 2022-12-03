package com.example.weightliftingapp

import android.R
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.weightliftingapp.databinding.FragmentDataBinding
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var pieChart: PieChart
lateinit var barChart: BarChart
lateinit var month: Button
lateinit var year: Button
lateinit var allT: Button
lateinit var total: TextView
lateinit var exper : TextView
/**
 * A simple [Fragment] subclass.
 * Use the [DataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DataFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentDataBinding

    lateinit var sharedViewModel :SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //val view: View = inflater.inflate(com.example.weightliftingapp.R.layout.fragment_data, container, false)
        binding = FragmentDataBinding.inflate(inflater,container,false)
        pieChart = binding.pie
        barChart = binding.bar
        month =binding.month
        year=binding.year
        allT=binding.all
        total=binding.total
        exper = binding.exper

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        makePie()
        //fillPie()
        //fillBar()


        return binding.root
    }

    override fun onResume(){

            super.onResume()

        if (sharedViewModel.isdbInit() == false) {
            sharedViewModel.setDatabaseManager()

        }
        //Log.i("list",ExpandableListData.data["CHEST"].toString())
        sharedViewModel.databaseManager.getUserData(::fillBar, ::fillPie)

        month.setOnClickListener {
            sharedViewModel.databaseManager.getUserData(::monthClick)
        }
        year.setOnClickListener {
            sharedViewModel.databaseManager.getUserData(::yearClick)
        }
        allT.setOnClickListener {
            sharedViewModel.databaseManager.getUserData(::allClick)
        }

    }

    fun monthClick(data: HashMap<String,Any>){

        val curdate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM")
        val mon = formatter.format(curdate)
        Log.i("current date:",mon.toString())
        var totalW = 0
        var totalE = 0

        val workouts = data["workouts"] as HashMap<String, Any>

        for (w in workouts.keys){

            val wrkt = workouts[w] as HashMap<String, Any>
            val exercises = wrkt["exercises"] as HashMap<String, Any>
            val date = wrkt["date"] as String
            Log.i("dates:",date.substring(0,7))
            if(date.substring(0,7) == mon.toString()){
                totalW+=1
                totalE+=exercises.keys.size
            }

        }

        total.text="Total Workouts: "+totalW.toString()
        var ex = totalE.toDouble()/totalW
        exper.text = "Exercises/Workout: "+ex.toString()

    }

    fun yearClick(data: HashMap<String,Any>){

        val curdate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy")
        val ye = formatter.format(curdate)
        //Log.i("current date:",ye.toString())
        var totalW = 0
        var totalE = 0

        val workouts = data["workouts"] as HashMap<String, Any>

        for (w in workouts.keys){

            val wrkt = workouts[w] as HashMap<String, Any>
            val exercises = wrkt["exercises"] as HashMap<String, Any>
            val date = wrkt["date"] as String
            Log.i("dates:",date.substring(0,7))
            if(date.substring(0,4) == ye.toString()){
                totalW+=1
                totalE+=exercises.keys.size
            }

        }



        total.text="Total Workouts: "+totalW.toString()
        var ex = totalE.toDouble()/totalW
        exper.text = "Exercises/Workout: "+ex.toString()

    }

    fun allClick(data: HashMap<String,Any>){

        var totalW = 0
        var totalE = 0

        val workouts = data["workouts"] as HashMap<String, Any>

        for (w in workouts.keys){

            val wrkt = workouts[w] as HashMap<String, Any>
            val exercises = wrkt["exercises"] as HashMap<String, Any>

                totalW+=1
                totalE+=exercises.keys.size


        }



        total.text="Total Workouts: "+totalW.toString()
        var ex = totalE.toDouble()/totalW
        exper.text = "Exercises/Workout: "+ex.toString()

    }

    fun fillBar(data: HashMap<String, Any>){

        var types = ExpandableListData.data
        var muscleCounts = HashMap<String,Long>()

        val workouts = data["workouts"] as HashMap<String, Any>

        for (w in workouts.keys) {
            val wrkt = workouts[w] as HashMap<String, Any>
            val exercises = wrkt["exercises"] as HashMap<String, Any>

            for (e in exercises.keys) {
                val exercise = exercises[e] as HashMap<String, Any>

                for (type in types.keys){

                    if (types[type]!!.contains(e)){
                        when (val count = muscleCounts[type])
                        {
                            null -> muscleCounts[type] = exercise["sets"] as Long
                            else -> muscleCounts[type] = count + exercise["sets"] as Long
                        }
                    }

                }

            }
        }


        var i = 0
        var bars = ArrayList<BarEntry>()
        var muscles = ArrayList<String>()

        for (t in muscleCounts.keys){
            bars.add(BarEntry(i.toFloat(), muscleCounts[t]!!.toFloat()))
            muscles.add(t)
            i++
        }



        var colors = ArrayList<Int>()

        for (n in ColorTemplate.MATERIAL_COLORS){
            colors.add(n)
        }

        var set = BarDataSet(bars,"Muscle")

        set.colors=colors

        var dat = BarData(set)


        barChart.data=dat

        barChart.setBackgroundColor(Color.WHITE)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(muscles)
        barChart.xAxis.granularity=1F
    }

    fun makePie(){
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12F)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText="Muscle Group Balance"
        pieChart.setCenterTextSize(18F)


    }
    fun fillPie(data: HashMap<String, Any>){

        var pushes = 0
        var pulls = 0
        var legs = 0
        var none = 0

        val workouts = data["workouts"] as HashMap<String, Any>

        for (w in workouts.keys) {
            val wrkt = workouts[w] as HashMap<String, Any>
            val exercises = wrkt["exercises"] as HashMap<String, Any>

            for (e in exercises.keys) {
                val exercise = exercises[e] as HashMap<String, Any>
                if (exercise["muscleGroup"].toString() == "push"){
                    pushes++
                }
                else if (exercise["muscleGroup"].toString() == "pull"){
                    pulls++
                }
                else if (exercise["muscleGroup"].toString() == "legs"){
                    legs++
                }
                else{
                    none++
                }

            }
        }


        var slices = ArrayList<PieEntry>()

        var total = pushes+pulls+legs+none
        /*Log.i("total:",total.toString())
        Log.i("pushes", pushes.toString())
        Log.i("none", none.toString())
        Log.i("pushes/total",(pushes.toDouble()/total).toString())
        Log.i("none/total",(none.toDouble()/total).toString())*/
        if (pushes!=0) {
            slices.add(PieEntry((pushes.toDouble() / total).toFloat(), "Push"))
        }
        if(pulls!=0) {
            slices.add(PieEntry((pulls.toDouble() / total).toFloat(), "Pull"))
        }
        if(legs!=0) {
            slices.add(PieEntry((legs.toDouble() / total).toFloat(), "Legs"))
        }
        if(none!=0) {
            slices.add(PieEntry((none.toDouble() / total).toFloat(), "Other"))
        }

        var colors = ArrayList<Int>()

        for (n in ColorTemplate.MATERIAL_COLORS){
            colors.add(n)
        }

        var set = PieDataSet(slices,"Type")
        set.setColors(colors)

        var pdata = PieData(set)
        pdata.setDrawValues(true)
        pdata.setValueFormatter(PercentFormatter(pieChart))
        pdata.setValueTextSize(12F)
        pdata.setValueTextColor(Color.BLACK)

        pieChart.data = pdata

        pieChart.invalidate()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DataFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}