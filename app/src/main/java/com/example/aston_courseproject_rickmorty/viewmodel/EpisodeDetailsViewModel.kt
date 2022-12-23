package com.example.aston_courseproject_rickmorty.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeDto
import com.example.aston_courseproject_rickmorty.model.repository.EpisodeDetailsRepository
import com.example.aston_courseproject_rickmorty.model.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.model.retrofit.Status
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.example.aston_courseproject_rickmorty.utils.mapper.CharacterForListDbMapper
import com.example.aston_courseproject_rickmorty.utils.mapper.CharacterForListMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class EpisodeDetailsViewModel(
    episodeID: Int,
    val repository: EpisodeDetailsRepository,
    internetChecker: InternetConnectionChecker
) : ViewModel() {

    val episode = MutableStateFlow(ApiState(Status.LOADING, EpisodeDto(), ""))
    val characters =
        MutableStateFlow(ApiState(Status.LOADING, mutableListOf<CharacterForListDto>(), ""))
    private val network: Boolean = internetChecker.isOnline()

    init {
        getEpisode(episodeID)
    }

    private fun getEpisode(episodeID: Int) {
        episode.value = ApiState.loading()
        viewModelScope.launch {
            val gottenEpisode: Flow<ApiState<EpisodeDto>> = if (network) {
                repository.getEpisode(episodeID)
            } else {
                repository.getEpisodeDb(episodeID)
            }
            gottenEpisode
                .catch {
                    episode.value = ApiState.error(it.message.toString())
                }
                .collect {
                    episode.value = ApiState.success(it.data)
                    if (network) {
                        val charactersId = episode.value.data?.characters ?: ""
                        getCharacters(charactersId)
                    } else {
                        val episodeId = episode.value.data?.id
                        getCharactersDb(episodeId!!)
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

    private suspend fun getCharactersDb(episodeID: Int) {
        repository.getCharacterListDb(episodeID)
            .catch {
                characters.value = ApiState.error(it.message.toString())
            }
            .collect {
                characters.value =
                    ApiState.success(CharacterForListDbMapper().transform(it.data!!))
            }
    }
}