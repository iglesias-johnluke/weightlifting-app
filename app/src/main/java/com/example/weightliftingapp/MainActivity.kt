package com.example.weightliftingapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.weightliftingapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var activeFragment : Fragment
    private val dataFragment = DataFragment()
    private val logFragment = LogFragment()
    private val browseWorkoutFragment = BrowseWorkoutFragment()
    private val fragmentManager = supportFragmentManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragments()


    }

    //initializes nav bar and fragment views
    private fun setFragments(){
        //add all fragments to main container, hide all fragments except dataFragment
        activeFragment = dataFragment
        fragmentManager.beginTransaction().add(R.id.main_container, logFragment, "logFragment")
            .hide(logFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.main_container, browseWorkoutFragment, "browseWorkoutFragment")
            .hide(browseWorkoutFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.main_container, dataFragment, "dataFragment")
            .commit()

        binding.bottomNavBar.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.data_page -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(dataFragment).commit()
                    activeFragment = dataFragment
                    binding.appBarLayoutText.text = getString(R.string.data_icon)
                }
                R.id.log_page -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(logFragment).commit()
                    activeFragment = logFragment
                    binding.appBarLayoutText.text = getString(R.string.log_icon)
                }
                R.id.browse_page -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(browseWorkoutFragment).commit()
                    activeFragment = browseWorkoutFragment
                    binding.appBarLayoutText.text = getString(R.string.browse_icon)
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }




}