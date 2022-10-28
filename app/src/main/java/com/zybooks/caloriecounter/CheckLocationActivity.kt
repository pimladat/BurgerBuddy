package com.zybooks.caloriecounter

import android.R
import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*


class CheckLocationActivity : AppCompatActivity() {
    val TAG = "CheckLocationActivity"
    private var locationTextView: TextView? = null
    private var InNOutNearbyTextView: TextView? = null
    private var state: String? = null
    private var street_address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.zybooks.caloriecounter.R.layout.check_location_activity)

        locationTextView = findViewById(com.zybooks.caloriecounter. R.id.location_text_view)
        InNOutNearbyTextView = findViewById(com.zybooks.caloriecounter.R.id.innout_nearby_text_view)

        // Calls find location() to determine user's location and display their state
        findLocation()

        // Creates and plays the In-N-Out drawing animation on click
        var animation : ImageView = findViewById(com.zybooks.caloriecounter.R.id.innout_animation)
        animation.setBackgroundResource(com.zybooks.caloriecounter.R.drawable.innout_animation)
        val innoutAnimation = animation.background as AnimationDrawable

        animation.setOnClickListener {
            if (innoutAnimation.isRunning) {
                innoutAnimation.stop()
            } else {
                innoutAnimation.start()
            }
        }
    }

    /**
     * Finds coordinates of phone's last location
     */
    @SuppressLint("MissingPermission")
    private fun findLocation() {// : Pair<Double, Double> {
        val client = LocationServices.getFusedLocationProviderClient(this)

        client.lastLocation
            .addOnSuccessListener(this) { location ->
                // Calls check US state to determine state based on given location
                checkUSState(location)
            }
    }

    /**
     * Reverse geocoding: finds US state based on given coordinates
     */
     private fun checkUSState(location: Location) {
        // Instantiate geoCoder
        val geoCoder = Geocoder(baseContext, Locale.getDefault())

        try {
            // Reverse geocoding: find location from given coordinates
            val addresses: List<Address> =
                geoCoder.getFromLocation(location.latitude, location.longitude, 1)

            // If not null,
            if (addresses.isNotEmpty()) {
                for (i in addresses) {
                    if (i.adminArea == null) {
                        Log.d(TAG, "error")
                    }
                    // Get adminArea field (aka the US state field)
                    state = i.adminArea

                    // If the state is CA, NV, AZ, UT, or Tx, show In-N-Out nearby msg
                    if (state == "California" || state == "Nevada" || state == "Arizona" || state == "Utah" || state == "Texas") {
                        InNOutNearbyTextView?.text = getString(com.zybooks.caloriecounter.R.string.innout_nearby)
                    } else {
                        // Else, show no In-N-Outs
                        InNOutNearbyTextView?.text = getString(com.zybooks.caloriecounter.R.string.no_innout_nearby)
                    }

                }
                // Update location text view with user's state
                locationTextView?.text = state
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

    }
}