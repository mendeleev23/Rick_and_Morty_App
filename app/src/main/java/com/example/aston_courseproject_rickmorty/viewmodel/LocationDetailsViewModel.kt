package com.example.aston_courseproject_rickmorty.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.LocationDto
import com.example.aston_courseproject_rickmorty.model.repository.LocationDetailsRepository
import com.example.aston_courseproject_rickmorty.model.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.model.retrofit.Status
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.utils.mapper.CharacterForListDbMapper
import com.example.aston_courseproject_rickmorty.utils.mapper.CharacterForListMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LocationDetailsViewModel(
    locationID: Int,
    val repository: LocationDetailsRepository,
    internetChecker: InternetConnectionChecker,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val location = MutableStateFlow(ApiState(Status.LOADING, LocationDto(), ""))
    fun getLocationValue(): MutableStateFlow<ApiState<LocationDto>> = location
    val characters =
        MutableStateFlow(ApiState(Status.LOADING, mutableListOf<CharacterForListDto>(), ""))
    private val network: Boolean = internetChecker.isOnline()

    init {
        getLocation(locationID)
    }

    private fun getLocation(locationID: Int) {
        location.value = ApiState.loading()
        viewModelScope.launch(dispatcher) {
            val gottenLocation: Flow<ApiState<LocationDto>> = if (network) {
                repository.getLocation(locationID)
            } else {
                repository.getLocationDb(locationID)
            }
            gottenLocation
                .catch {
                    location.value = ApiState.error(it.message.toString())
                }
                .collect {
                    location.value = ApiState.success(it.data)
                    if (network) {
                        val charactersId = location.value.data?.residents ?: ""
                        getCharacters(charactersId)
                    } else {
                        val locationId = location.value.data?.id ?: 0
                        getCharactersDb(locationId)
                    }
                }
        }
    }

    private suspend fun getCharacters(needIds: String) {
        var needIdsLocal = needIds
        if (!needIdsLocal.contains(",")) needIdsLocal += ","
        repository.getCharacterList(needIdsLocal)
            .catch {
                characters.value = ApiState.error(it.message.toString())
            }
            .collect {
                repository.saveInDb(it.data!!)
                characters.value =
                    ApiState.success(CharacterForListMapper().transform(it.data))
            }
    }

    private suspend fun getCharactersDb(locationID: Int) {
        repository.getCharacterListDb(locationID)
            .catch {
                characters.value = ApiState.error(it.message.toString())
            }
            .collect {
                characters.value =
                    ApiState.success(CharacterForListDbMapper().transform(it.data!!))
            }
    }
}