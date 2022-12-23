package com.example.aston_courseproject_rickmorty.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.example.aston_courseproject_rickmorty.R

class LocationFilterDialog(val context: Context, private val applyClickListener: ApplyClickListener) {

    private lateinit var filterDialog: Dialog

    private fun loadFilterDialog() {
        filterDialog = Dialog(context, R.style.Dialog)
        filterDialog.setTitle(context.resources.getString(R.string.filter_location))
        filterDialog.setContentView(R.layout.dialog_location_filter)

        val buttonApply = filterDialog.findViewById<Button>(R.id.buttonApply)
        buttonApply.setOnClickListener {
            applyClickListener.onApplyClick(filterDialog)
        }

        val checkType = filterDialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkDimension = filterDialog.findViewById<CheckBox>(R.id.checkBoxDimension)
        val editType = filterDialog.findViewById<EditText>(R.id.editTextType)
        val editDimension = filterDialog.findViewById<EditText>(R.id.editTextDimension)
        editType.visibility = View.INVISIBLE
        editDimension.visibility = View.INVISIBLE

        checkType.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                editType.visibility = View.VISIBLE
            } else {
                editType.visibility = View.INVISIBLE
            }
        }
        checkDimension.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                editDimension.visibility = View.VISIBLE
            } else {
                editDimension.visibility = View.INVISIBLE
            }
        }
    }

    fun showDialog(typeState: Filter, dimensionState: Filter) {
        loadFilterDialog()
        setCurrentState(typeState, dimensionState)
        filterDialog.show()
    }

    private fun setCurrentState(typeState: Filter, dimensionState: Filter) {
        val checkType = filterDialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkDimension = filterDialog.findViewById<CheckBox>(R.id.checkBoxDimension)
        val editType = filterDialog.findViewById<EditText>(R.id.editTextType)
        val editDimension = filterDialog.findViewById<EditText>(R.id.editTextDimension)

        checkType.isChecked = typeState.isApplied
        editType.setText(typeState.stringToFilter)
        checkDimension.isChecked = dimensionState.isApplied
        editDimension.setText(dimensionState.stringToFilter)
    }


    interface ApplyClickListener {

        fun onApplyClick(dialog: Dialog)
    }
}