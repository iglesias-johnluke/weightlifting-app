package com.example.weightliftingapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.view.Menu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weightliftingapp.databinding.ActivityMainBinding
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract


import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var activeFragment : Fragment
    private val dataFragment = DataFragment()
    private val logFragment = LogFragment()
    private val browseWorkoutFragment = BrowseWorkoutFragment()
    private val fragmentManager = supportFragmentManager
    val FIREBASE = "firebase-log"
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseManager : DatabaseManager

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

    /**class for managing reading and writing to user data object within firebase database*/
    class DatabaseManager(userID: String) {
        val database = Firebase.database.getReference("users")
        lateinit var user : User
        val userID = userID

        /**returns true if user key is mapped to a User object within firebase, false ow*/
        fun isUserInitialized(uid:String) : Boolean{
            var isInitialized = false
            //get child for uid key within DB
            database.child(uid).get().addOnSuccessListener {
                val dataSnapshot= it
                try{//check snapshot val is of user type
                    if(  !(dataSnapshot.value is User) ){
                        throw java.lang.Error("DATASNAPSHOT NOT TYPE USER")
                    }
                    user =  dataSnapshot.value as User
                    isInitialized = true
                    Log.d("database", user.toString())
                }catch (e: java.lang.Error){//snapshot val is not type user
                    Log.d("database", "DATASNAPSHOT NOT TYPE USER")
                }
            }.addOnFailureListener{ //failed get child
                Log.d("database", "FAILED GET CHILD")
            }


            return isInitialized
        }




        /**adds a workout object to user database,
         * does not perform action if parameter is null or workoutData does not have a name
         * or if userID not initialized*/
        fun addWorkout(workoutData: WorkoutData){
            if(workoutData == null || workoutData.name == null ){
                return
            }
            database.child(userID).child("workouts").child(workoutData.name).setValue(workoutData)
        }

        /**removes all user workouts from database*/
        fun clearWorkouts(){
            database.child(userID).child("workouts").setValue(null)
        }


        /**listens for changes of userdata and updates UI*/
        fun setDataListener(textView:TextView){
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val post = dataSnapshot.getValue<String>()
                    // ...
                    textView.text = post

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                }
            }
            database.child(userID).addValueEventListener(postListener)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragments()

        // Initialize Firebase Auth
        auth = Firebase.auth

        /*user signs out, which closes main activity, relaunches sign-in activity*/
        binding.signoutButton.setOnClickListener{
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    createSignInIntent()

                    finish()
                }
        }




    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser == null){ //if user not logged in, show sign-in activity
            createSignInIntent()
        }
    }

    /** launches sign-in activity */
    private fun createSignInIntent(){
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
            databaseManager = DatabaseManager(user!!.uid)

            val workoutData = WorkoutData(name = "NY WORKOUT", date = "2022-01-27")

            val exercise = ExerciseData(name = "John's exercise", muscleGroup = PUSH_MUSCLE_GROUP)
            val exerciseMap = HashMap<String, Any>()
            exerciseMap.put("John exercise", exercise)

            val exercise2 = ExerciseData(name = "Kady's exercise", muscleGroup = NONE_MUSCLE_GROUP )
            exerciseMap.put(exercise2.name!!, exercise2)


            workoutData.exercises = exerciseMap
            databaseManager.clearWorkouts()
            Log.d("firebase", "CLEARED")


//            databaseManager.setDataListener(binding.appBarLayoutText)



            Log.d(FIREBASE, "SIGNED IN")


        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.d(FIREBASE, "FAILED SIGN IN")
        }
    }

    open fun emailLink() {
        // [START auth_fui_email_link]
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName( /* yourPackageName= */
                "com.example.weightliftingapp\n",  /* installIfNotAvailable= */
                true,  /* minimumVersion= */
                null)
            .setHandleCodeInApp(true) // This must be set to true
            .setUrl("https://google.com") // This URL needs to be whitelisted
            .build()

        val providers = listOf(
            EmailBuilder()
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)
                .build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_email_link]
    }

    open fun catchEmailLink() {
        val providers: List<IdpConfig> = emptyList()

        // [START auth_fui_email_link_catch]
        if (AuthUI.canHandleIntent(intent)) {
            val extras = intent.extras ?: return
            val link = extras.getString("email_link_sign_in")
            if (link != null) {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(providers)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
        // [END auth_fui_email_link_catch]
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



    data class ExerciseData(
        val name: String? = null,
        val reps: Int? = null,
        val weight: Int? = null,
        val muscleGroup: String? = null,

    )

    data class WorkoutData(
        val name: String? = null,
        //exercises map has exercise names as the keys and exercise objects as the values
        var exercises: HashMap<String, Any>? = null,
        //date string is in format "yyyy-mm-dd"
        val date: String? = null
    )




}