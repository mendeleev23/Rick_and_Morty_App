package com.example.aston_courseproject_rickmorty.viewmodel

import android.app.Dialog
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.view.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.dto.LocationForListDto
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class LocationViewModel(
    private val dataSource: Flow<PagingData<LocationForListDto>>
) : ViewModel() {

    val typeFilter = MutableLiveData<Filter>()
    val dimensionFilter = MutableLiveData<Filter>()

    val locations: Flow<PagingData<LocationForListDto>> by lazy {
        dataSource.cachedIn(viewModelScope)
    }

    fun onApplyClick(dialog: Dialog) {
        val checkType = dialog.findViewById<CheckBox>(R.id.checkBoxType)
        val checkDimension = dialog.findViewById<CheckBox>(R.id.checkBoxDimension)
        val editType = dialog.findViewById<EditText>(R.id.editTextType)
        val editDimension = dialog.findViewById<EditText>(R.id.editTextDimension)
        typeFilter.value = Filter(checkType.isChecked, editType.text.toString())
        dimensionFilter.value = Filter(checkDimension.isChecked, editDimension.text.toString())

        dialog.dismiss()
    }
}