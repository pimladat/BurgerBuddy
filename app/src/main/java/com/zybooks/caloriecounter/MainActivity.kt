package com.zybooks.caloriecounter

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat


private const val KEY_TOTAL_MINUTES = "totalMinutes"
private const val KEY_EXERCISE = "exerciseType"

class MainActivity : AppCompatActivity() {

    private lateinit var totalCaloriesTextView: TextView
    private lateinit var totalExerciseTextView: TextView
    private lateinit var calculateButton: Button
    private lateinit var spinner: Spinner
    private var totalMinutes = 0.0
    private var exerciseType = R.string.total_minutes_running
    private var caloriesEaten = 0
    private var itemEaten = ""

    // Initialize calories for each food item
    val HAMBURGER_CALORIES = 390
    val CHEESEBURGER_CALORIES = 480
    val DOUBLE_DOUBLE_CALORIES = 670
    val FRENCH_FRIES_CALORIES = 370
    val MILKSHAKE_CALORIES = 580


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect layout elements using findViewById
        totalCaloriesTextView = findViewById(R.id.total_calories_text_view)
        totalExerciseTextView = findViewById(R.id.total_exercise_text_view)
        calculateButton = findViewById(R.id.calc_button)
        spinner = findViewById<Spinner>(R.id.spinner_size)

        // Create adapter for spinner that drops down into list of food items
        val adapter = ArrayAdapter.createFromResource(this, R.array.sizes_array, R.layout.spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Make total exercise text view a context menu
        registerForContextMenu(findViewById(R.id.total_exercise_text_view))

        // Listen to spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                itemEaten = parent.getItemAtPosition(position) as String
                Toast.makeText(this@MainActivity, itemEaten, Toast.LENGTH_SHORT).show()

                // Instantiate alert dialog to display protein-style tip alert
                val alertDialog = AlertDialogFragment()

                // Set calories eaten to selected item's calories
                // Change total calories textview to display selected item's calories
                when (itemEaten) {
                    "Hamburger" -> {
                        caloriesEaten = HAMBURGER_CALORIES
                        totalCaloriesTextView.text = getString(R.string.total_calories, HAMBURGER_CALORIES)
                    }
                    "Cheeseburger" -> {
                        caloriesEaten = CHEESEBURGER_CALORIES
                        totalCaloriesTextView.text = getString(R.string.total_calories, CHEESEBURGER_CALORIES)
                    }
                    "Double-Double" -> {
                        caloriesEaten = DOUBLE_DOUBLE_CALORIES
                        totalCaloriesTextView.text = getString(R.string.total_calories, DOUBLE_DOUBLE_CALORIES)
                    }
                    "French Fries" -> {
                        caloriesEaten = FRENCH_FRIES_CALORIES
                        totalCaloriesTextView.text = getString(R.string.total_calories, FRENCH_FRIES_CALORIES)
                    }
                    "Milkshake" -> {
                        caloriesEaten = MILKSHAKE_CALORIES
                        totalCaloriesTextView.text = getString(R.string.total_calories, MILKSHAKE_CALORIES)
                    }
                }

                // If Double-double selected, display tip about protein style in alert dialog
                if (itemEaten == "Double-Double") {
                    alertDialog.show(supportFragmentManager, "alertDialog")
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        // Instantiate error dialog to display error if user tries to choose protein style for fries or milkshake
        val errorDialog = ErrorDialogFragment()

        // Protein style switch
        val mySwitch = findViewById<SwitchCompat>(R.id.protein_style_switch)
        // If selected item is hamburger, cheeseburger, or double-double, checking protein-style switch reduces calories by 150
        mySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && (itemEaten == "Hamburger" || itemEaten == "Cheeseburger" || itemEaten == "Double-Double")) {
                caloriesEaten -= 150
                totalCaloriesTextView.text = getString(R.string.total_calories, caloriesEaten)

            // Unselecting switch increases calories by 150
            } else if (!isChecked && (itemEaten == "Hamburger" || itemEaten == "Cheeseburger" || itemEaten == "Double-Double")) {
                caloriesEaten += 150
                totalCaloriesTextView.text = getString(R.string.total_calories, caloriesEaten)

            // Otherwise, switch cannot be checked for fries or shakes
            } else {
                totalCaloriesTextView.text = getString(R.string.total_calories, caloriesEaten)
                errorDialog.show(supportFragmentManager, "errorDialog")
                mySwitch.isChecked = false
            }
        }

        if(savedInstanceState != null) {
            totalMinutes = savedInstanceState.getDouble(KEY_TOTAL_MINUTES)
            exerciseType = savedInstanceState.getInt(KEY_EXERCISE)
            displayTotal()
        }
    }

    /**
     * Creates appbar_menu that contains setting icon
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    /**
     * If settings selected, start SettingsActivity
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Creates context menu for exercise text view
     */
    override fun onCreateContextMenu(menu: ContextMenu?,
                                     v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    /**
     * Changes exercise display depending on exercise chosen from context menu
     */
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.running -> {
                // Set textview to selected exercise type
                totalExerciseTextView.setText(R.string.total_minutes_running)
                // Set exerciseType field to selected exercise type
                exerciseType = R.string.total_minutes_running
                true
            }
            R.id.bicycling -> {
                totalExerciseTextView.setText(R.string.total_minutes_bicycling)
                exerciseType = R.string.total_minutes_bicycling
                true
            }
            R.id.yoga -> {
                totalExerciseTextView.setText(R.string.total_minutes_yoga)
                exerciseType = R.string.total_minutes_yoga
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    /**
     * Stores total minutes of exercise and exercise type
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(KEY_TOTAL_MINUTES, totalMinutes)
        outState.putInt(KEY_EXERCISE, exerciseType)
    }

    /**
     * Calls ExerciseCalculator and passes in caloriesEaten and exerciseType
     * Calls display total to display total minutes needed for each exercise and food item
     */
    fun calculateClick(view: View) {
        val calc = ExerciseCalculator(caloriesEaten, exerciseType)
        totalMinutes = calc.totalMinutes
        displayTotal()
    }

    /**
     * Changes textview to display total minutes of exercise
     */
    private fun displayTotal() {
        val totalText = getString(exerciseType) + " " + getString(R.string.total_miles, totalMinutes)
        totalExerciseTextView.text = totalText
    }


}