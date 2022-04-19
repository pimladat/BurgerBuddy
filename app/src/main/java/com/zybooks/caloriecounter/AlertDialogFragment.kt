package com.zybooks.caloriecounter
/**
 * Creates alert dialog fragment when user chooses the double-double burger
 * Displays tip about replacing burger buns with lettuce (protein-style)
 */

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?)
            : Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.tip)
        builder.setMessage(R.string.tip_message)
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }
}