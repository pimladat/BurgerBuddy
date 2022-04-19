package com.zybooks.caloriecounter
/**
 * Displays error dialog when user attempts to select protein style for fries or milkshake
 */
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ErrorDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?)
            : Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.error)
        builder.setMessage(R.string.error_message)
        builder.setPositiveButton(R.string.ok, null)
        return builder.create()
    }
}