package com.example.weightliftingapp

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.weightliftingapp.databinding.FragmentDataBinding
import android.widget.TextView
import androidx.fragment.app.Fragment
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var pieChart: PieChart
lateinit var barChart: BarChart
lateinit var menu: Spinner
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



        makePie()
        fillPie()
        fillBar()


        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        return binding.root
    }

    fun fillBar(){

        var bars = ArrayList<BarEntry>()
        bars.add(BarEntry(0F,4F))
        bars.add(BarEntry(1F,6F))
        bars.add(BarEntry(2F,5F))
        bars.add(BarEntry(3F,9F))
        bars.add(BarEntry(4F,0F))
        bars.add(BarEntry(5F,2F))
        bars.add(BarEntry(6F,8F))
        bars.add(BarEntry(7F,6F))
        bars.add(BarEntry(8F,1F))
        bars.add(BarEntry(9F,7F))

        var colors = ArrayList<Int>()

        for (n in ColorTemplate.MATERIAL_COLORS){
            colors.add(n)
        }

        var set = BarDataSet(bars,"Muscle")

        set.colors=colors

        var muscles = ArrayList<String>()
        muscles.add("Chest")
        muscles.add("Shoulders")
        muscles.add("Bicep")
        muscles.add("Tricep")
        muscles.add("Back")
        muscles.add("Hamstring")
        muscles.add("Thighs")
        muscles.add("Glutes")
        muscles.add("Calves")
        muscles.add("Abs")

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
    fun fillPie(){

        var slices = ArrayList<PieEntry>()

        slices.add(PieEntry(0.3F,"Push"))
        slices.add(PieEntry(0.3F,"Pull"))
        slices.add(PieEntry(0.4F,"Legs"))

        var colors = ArrayList<Int>()

        for (n in ColorTemplate.MATERIAL_COLORS){
            colors.add(n)
        }

        var set = PieDataSet(slices,"Workout Type")
        set.setColors(colors)

        var data = PieData(set)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12F)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data

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