package com.example.weightliftingapp


import CustomExpandableListAdapter
import MyAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weightliftingapp.databinding.ActivityMainBinding
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract


import com.google.firebase.auth.FirebaseAuth
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.example.weightliftingapp.ExpandableListData.data
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var activeFragment: Fragment
    private val dataFragment = DataFragment()
    private val logFragment = LogFragment()
    private val exerciseCatalogFragment = ExerciseCatalog()
    private val pastWorkoutsFragment = PastWorkouts()
    private val browseWorkoutFragment = BrowseWorkoutFragment()
    private var expandableListView: ExpandableListView? = null
    private var adapter2: ExpandableListAdapter? = null
    private var titleList: List<String>? = null
    private val fragmentManager = supportFragmentManager
    val FIREBASE = "firebase-log"
    private lateinit var auth: FirebaseAuth
    lateinit var sharedViewModel : SharedViewModel



    val PUSH_MUSCLE_GROUP = "push"
    val PULL_MUSCLE_GROUP = "pull"
    val LEGS_MUSCLE_GROUP = "legs"
    val NONE_MUSCLE_GROUP = "none"



    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragments()

        // Initialize Firebase Auth
        auth = Firebase.auth

        /*user signs out, which closes main activity, relaunches sign-in activity*/
        binding.signoutButton.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    createSignInIntent()


                }
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) { //if user not logged in, show sign-in activity
            createSignInIntent()
        }
        else{
            //setFragments()
        }
    }

    /** launches sign-in activity */
    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(signInIntent)
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
            sharedViewModel.setDatabaseManager()
            //sharedViewModel.databaseManager.demo()
            sharedViewModel.databaseManager.setDataListener()

            //setFragments()
            Log.d(FIREBASE, "SIGNED IN")

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.d(FIREBASE, "FAILED SIGN IN")
        }
    }




    //initializes nav bar and fragment views
    private fun setFragments() {
        //add all fragments to main container, hide all fragments except dataFragment
        Log.i("setFragments", "call")
        activeFragment = dataFragment
        fragmentManager.beginTransaction().add(R.id.main_container, logFragment, "logFragment")
            .hide(logFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.main_container, browseWorkoutFragment, "browseWorkoutFragment")
            .hide(browseWorkoutFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.main_container, dataFragment, "dataFragment")
            .commit()

        binding.bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.data_page -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(dataFragment)
                        .commit()
                    activeFragment = dataFragment
                    binding.appBarLayoutText.text = getString(R.string.data_icon)
                    dataFragment.showPieChartAndEnableStatTabs()
                }
                R.id.log_page -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(logFragment)
                        .commit()
                    activeFragment = logFragment
                    binding.appBarLayoutText.text = getString(R.string.log_icon)

                    val exerciseMap = HashMap<String, Any>()

                    var addExercise = findViewById<Button>(R.id.logAddExercise)
                    var logWorkout = findViewById<Button>(R.id.logWorkout)

                    addExercise.setOnClickListener {
                        if(exerciseFormIsValid()){
                            val date = findViewById<View>(R.id.logWorkoutDate) as EditText
                            val workoutDate = date.text.toString()

                            val type = findViewById<View>(R.id.logWorkoutType) as EditText
                            val workoutType = type.text.toString()

                            val name = findViewById<View>(R.id.logExerciseName) as EditText
                            val exerciseName = name.text.toString()

                            val sets = findViewById<View>(R.id.logExerciseSets) as EditText
                            val exerciseSets = sets.text.toString()?.toInt()

                            val reps = findViewById<View>(R.id.logExerciseReps) as EditText
                            val exerciseReps = reps.text.toString()?.toInt()

                            val weight = findViewById<View>(R.id.logExerciseWeight) as EditText
                            val exerciseWeight = weight.text.toString()?.toInt()


                            val exercise = DatabaseManager.Exercise(
                                name = exerciseName, muscleGroup = workoutType,
                                reps = exerciseReps, weight = exerciseWeight,
                                sets = exerciseSets
                            )

                            Snackbar.make(binding.root, "$exerciseName added to $workoutType workout", Snackbar.LENGTH_SHORT).show()

                            exerciseMap[exerciseName] = exercise

                            findViewById<EditText>(R.id.logExerciseName).text.clear()
                            findViewById<EditText>(R.id.logExerciseSets).text.clear()
                            findViewById<EditText>(R.id.logExerciseReps).text.clear()
                            findViewById<EditText>(R.id.logExerciseWeight).text.clear()

                        }else{
                            Toast.makeText(this, "Please fill all exercise fields", Toast.LENGTH_LONG).show()

                        }



                    }

                    logWorkout.setOnClickListener {

                        val date = findViewById<View>(R.id.logWorkoutDate) as EditText
                        val workoutDate = date.text.toString()

                        val type = findViewById<View>(R.id.logWorkoutType) as EditText
                        val workoutType = type.text.toString()

                        val workoutData =
                            DatabaseManager.Workout(name = "$workoutType workout", date = workoutDate)

                        if( exerciseMap.isNotEmpty() && workoutNameAndTypeIsValid()){
                            workoutData.exercises = exerciseMap
                            if(::sharedViewModel.isInitialized == false){
                                sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
                            }
                            sharedViewModel.databaseManager.addWorkout(workoutData)
                            findViewById<TextInputEditText>(R.id.logWorkoutDate).text?.clear()
                            findViewById<TextInputEditText>(R.id.logWorkoutType).text?.clear()
                            exerciseMap.clear()
                        }else{
                            Toast.makeText(this, "Please enter workout date & type and add an exercise", Toast.LENGTH_LONG).show()
                        }


                    }
                }
                R.id.browse_page -> {
                    fragmentManager.beginTransaction().hide(activeFragment)
                        .show(browseWorkoutFragment).commit()
                    activeFragment = browseWorkoutFragment
                    binding.appBarLayoutText.text = getString(R.string.browse_icon)

                    tabLayout = findViewById(R.id.tabLayout)
                    viewPager = findViewById(R.id.viewPager)
                    Log.i("Current tabCount: ", tabLayout.tabCount.toString())

                    if (tabLayout.tabCount == 0) {
                        tabLayout.addTab(tabLayout.newTab().setText("Past Workouts"))
                        tabLayout.addTab(tabLayout.newTab().setText("Exercise Catalog"))
                        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
                        var adapter = MyAdapter(
                            this, supportFragmentManager,
                            tabLayout.tabCount
                        )
                        viewPager.adapter = adapter
                        viewPager.addOnPageChangeListener(
                            TabLayout.TabLayoutOnPageChangeListener(
                                tabLayout
                            )
                        )
                        tabLayout.addOnTabSelectedListener(object :
                            TabLayout.OnTabSelectedListener {
                            override fun onTabSelected(tab: TabLayout.Tab) {
                                Log.i("Tab Selected", tab.position.toString() + tab.text)
                                if (tab.position == 1) {
                                    title = "KotlinApp"
                                    expandableListView = findViewById(R.id.expendableList)
                                    if (expandableListView != null) {
                                        val listData = data
                                        titleList = ArrayList(listData.keys)
                                        adapter2 = CustomExpandableListAdapter(
                                            applicationContext,
                                            titleList as ArrayList<String>,
                                            listData
                                        )
                                        expandableListView!!.setAdapter(adapter2)


                                    }
                                }
                                viewPager.currentItem = tab.position
                            }

                            override fun onTabUnselected(tab: TabLayout.Tab) {}
                            override fun onTabReselected(tab: TabLayout.Tab) {}
                        })
                    }
                }
            }
            true
        }
    }

    fun workoutNameAndTypeIsValid() : Boolean {
        val date = findViewById<View>(R.id.logWorkoutDate) as EditText
        val workoutDate = date.text.toString()

        val type = findViewById<View>(R.id.logWorkoutType) as EditText
        val workoutType = type.text.toString()

        if(workoutDate.isEmpty() || workoutType.isEmpty()){
            return false
        }
        return true
    }

    fun exerciseFormIsValid() : Boolean{
        try{

            val name = findViewById<View>(R.id.logExerciseName) as EditText
            val exerciseName = name.text.toString()

            val sets = findViewById<View>(R.id.logExerciseSets) as EditText
            val exerciseSets = sets.text.toString().toInt()

            val reps = findViewById<View>(R.id.logExerciseReps) as EditText
            val exerciseReps = reps.text.toString().toInt()

            val weight = findViewById<View>(R.id.logExerciseWeight) as EditText
            val exerciseWeight = weight.text.toString().toInt()

            if(exerciseName.isEmpty()){
                return false
            }
            return true
        }catch (e : java.lang.NumberFormatException){
            return false
        }




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.bottom_navigation_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        //remove listener to changes for user data within firebase
        sharedViewModel.databaseManager.database.child(sharedViewModel.databaseManager.userID)
            .removeEventListener(sharedViewModel.databaseManager.eventListener)

    }





    fun onBrowseWorkoutClick(view: View) {
        fragmentManager.beginTransaction().hide(activeFragment).show(browseWorkoutFragment).commit()
        activeFragment = browseWorkoutFragment

    }


}