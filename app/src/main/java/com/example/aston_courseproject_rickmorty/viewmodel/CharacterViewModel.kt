package com.example.aston_courseproject_rickmorty.viewmodel

import android.app.Dialog
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.view.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class CharacterViewModel(
    private val dataSource: Flow<PagingData<CharacterForListDto>>
) : ViewModel() {

    val statusFilter = MutableLiveData<Filter>()
    val speciesFilter = MutableLiveData<Filter>()
    val typeFilter = MutableLiveData<Filter>()
    val genderFilter = MutableLiveData<Filter>()

    val characters: Flow<PagingData<CharacterForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun onApplyClick(dialog: Dialog) {
        val checkSpecies = dialog.findViewById<CheckBox>(R.id.checkBoxSpecies)
        val checkType = dialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkStatus = dialog.findViewById<CheckBox>(R.id.checkBoxStatus)
        val checkGender = dialog.findViewById<CheckBox>(R.id.checkBoxGender)

        val editSpecies = dialog.findViewById<EditText>(R.id.editTextSpecies)
        val editType = dialog.findViewById<EditText>(R.id.editTextType)
        val radioGroupStatus = dialog.findViewById<RadioGroup>(R.id.radioGroupStatus)
        val radioGroupGender = dialog.findViewById<RadioGroup>(R.id.radioGroupGender)

        when(val checkedRadioButtonId = radioGroupStatus.checkedRadioButtonId) {
            -1 -> {
                statusFilter.value = Filter(checkStatus.isChecked, "")
            }
            else -> {
                val selectedRadioButton = dialog.findViewById<RadioButton>(checkedRadioButtonId)
                var status = selectedRadioButton.text.toString()
                if (status == "Unknown") status = status.lowercase()
                statusFilter.value = Filter(checkStatus.isChecked, status)
            }
        }
        when(val checkedRadioButtonId = radioGroupGender.checkedRadioButtonId) {
            -1 -> {
                genderFilter.value = Filter(checkGender.isChecked, "")
            }
            else -> {
                val selectedRadioButton = dialog.findViewById<RadioButton>(checkedRadioButtonId)
                var gender = selectedRadioButton.text.toString()
                if (gender == "Unknown") gender = gender.lowercase()
                genderFilter.value = Filter(checkGender.isChecked, gender)
            }
        }
        speciesFilter.value = Filter(checkSpecies.isChecked, editSpecies.text.toString())
        typeFilter.value = Filter(checkType.isChecked, editType.text.toString())

        dialog.dismiss()
    }
}