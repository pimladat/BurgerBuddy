package com.zybooks.caloriecounter

import java.lang.Math.ceil

const val CALORIES_PER_MIN_RUNNING = 9.2
const val CALORIES_PER_MIN_BICYCLING = 8.4
const val CALORIES_PER_MIN_YOGA = 4.8

/**
 * Takes in calories of item and type of exercise
 * @returns the number of minutes of each exercise to needed to burn off calories eaten
 */
class ExerciseCalculator {
    constructor(caloriesEaten: Int, exerciseType: Int) {
        this.caloriesEaten = caloriesEaten
        this.exerciseType = exerciseType
    }

    // Setter
    var exerciseType = 0
        set(value) {
            field = value
        }

    // Setter
    var caloriesEaten = 0
        set(value) {
            field = if (value >= 0) value else 0
        }

    val totalMinutes: Double
        // Returns number of minutes of each exercise needed to burn off each food item
        get() {
            return if (exerciseType == R.string.total_minutes_running)
                ceil(caloriesEaten / CALORIES_PER_MIN_RUNNING)
            else if(exerciseType == R.string.total_minutes_bicycling)
                ceil(caloriesEaten / CALORIES_PER_MIN_BICYCLING)
            else
                ceil(caloriesEaten / CALORIES_PER_MIN_YOGA)
        }

}