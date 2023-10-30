package it.step.moviecatalog.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R

class LocationPermissionDialog : DialogFragment() {

    private lateinit var mainActivity: Activity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mainActivity = requireActivity() as MainActivity

        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.no_location_perm_dialog))
                .setPositiveButton(
                    getString(R.string.yes_string)
                ) { _, _ ->
                    // Show permission dialog
                    (mainActivity as MainActivity).requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
                .setNegativeButton(
                    getString(R.string.no_string)
                ) { _, _ -> }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}