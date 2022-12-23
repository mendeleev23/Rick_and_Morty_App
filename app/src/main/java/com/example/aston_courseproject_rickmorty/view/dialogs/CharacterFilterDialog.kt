package com.example.aston_courseproject_rickmorty.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.*
import com.example.aston_courseproject_rickmorty.R


class CharacterFilterDialog(val context: Context, private val applyClickListener: ApplyClickListener) {

    private lateinit var filterDialog: Dialog

    private fun loadFilterDialog() {
        filterDialog = Dialog(context, R.style.Dialog)
        filterDialog.setTitle(context.resources.getString(R.string.filter_character))
        filterDialog.setContentView(R.layout.dialog_character_filter)

        val buttonApply = filterDialog.findViewById<Button>(R.id.buttonApply)
        buttonApply.setOnClickListener {
            applyClickListener.onApplyClick(filterDialog)
        }
        val checkSpecies = filterDialog.findViewById<CheckBox>(R.id.checkBoxSpecies)
        val checkType = filterDialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkStatus = filterDialog.findViewById<CheckBox>(R.id.checkBoxStatus)
        val checkGender = filterDialog.findViewById<CheckBox>(R.id.checkBoxGender)

        val editSpecies = filterDialog.findViewById<EditText>(R.id.editTextSpecies)
        val editType = filterDialog.findViewById<EditText>(R.id.editTextType)

        val statusRadioGroup = filterDialog.findViewById<RadioGroup>(R.id.radioGroupStatus)
        val genderRadioGroup = filterDialog.findViewById<RadioGroup>(R.id.radioGroupGender)

        editSpecies.visibility = View.INVISIBLE
        editType.visibility = View.INVISIBLE
        statusRadioGroup.visibility = View.INVISIBLE
        genderRadioGroup.visibility = View.INVISIBLE


        checkSpecies.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                editSpecies.visibility = View.VISIBLE
            } else {
                editSpecies.visibility = View.INVISIBLE
            }
        }
        checkType.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                editType.visibility = View.VISIBLE
            } else {
                editType.visibility = View.INVISIBLE
            }
        }
        checkStatus.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                statusRadioGroup.visibility = View.VISIBLE
            } else {
                statusRadioGroup.visibility = View.INVISIBLE
            }
        }
        checkGender.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                genderRadioGroup.visibility = View.VISIBLE
            } else {
                genderRadioGroup.visibility = View.INVISIBLE
            }
        }
    }

    fun showDialog(statusState: Filter, speciesState: Filter, typeState: Filter, genderState: Filter) {
        loadFilterDialog()
        setCurrentState(statusState, speciesState, typeState, genderState)
        filterDialog.show()
    }

    private fun setCurrentState(statusState: Filter, speciesState: Filter, typeState: Filter, genderState: Filter) {
        val checkSpecies = filterDialog.findViewById<CheckBox>(R.id.checkBoxSpecies)
        val checkType = filterDialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkStatus = filterDialog.findViewById<CheckBox>(R.id.checkBoxStatus)
        val checkGender = filterDialog.findViewById<CheckBox>(R.id.checkBoxGender)

        val editSpecies = filterDialog.findViewById<EditText>(R.id.editTextSpecies)
        val editType = filterDialog.findViewById<EditText>(R.id.editTextType)

        checkSpecies.isChecked = speciesState.isApplied
        editSpecies.setText(speciesState.stringToFilter)
        checkType.isChecked = typeState.isApplied
        editType.setText(typeState.stringToFilter)
        checkStatus.isChecked = statusState.isApplied
        when (statusState.stringToFilter) {
            "Alive" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonAlive)
                radioButton.isChecked = true
            }
            "Dead" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonDead)
                radioButton.isChecked = true
            }
            "unknown" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonUnknown)
                radioButton.isChecked = true
            }
        }
        checkGender.isChecked = genderState.isApplied
        when (genderState.stringToFilter) {
            "Female" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonFemale)
                radioButton.isChecked = true
            }
            "Male" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonMale)
                radioButton.isChecked = true
            }
            "Genderless" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonGenderless)
                radioButton.isChecked = true
            }
            "unknown" -> {
                val radioButton = filterDialog.findViewById<RadioButton>(R.id.radioButtonUnknownGender)
                radioButton.isChecked = true
            }
        }
    }


    interface ApplyClickListener {

        fun onApplyClick(dialog: Dialog)
    }
}