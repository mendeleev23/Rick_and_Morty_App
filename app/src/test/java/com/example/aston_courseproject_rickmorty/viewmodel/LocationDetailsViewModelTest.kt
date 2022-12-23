package com.example.aston_courseproject_rickmorty.viewmodel

import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.MainCoroutineRule
import com.example.aston_courseproject_rickmorty.model.dto.LocationDto
import com.example.aston_courseproject_rickmorty.model.repository.LocationDetailsRepository
import com.example.aston_courseproject_rickmorty.model.retrofit.ApiState
import com.example.aston_courseproject_rickmorty.utils.InternetConnectionChecker
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mockito

@ExperimentalPagingApi
class LocationDetailsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutinesRule = MainCoroutineRule()

    var mockLocationDetailsRepository: LocationDetailsRepository = mock()
    var mockInternetConnectionChecker: InternetConnectionChecker = mock()
    lateinit var SUT: LocationDetailsViewModel
    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()
    @Before
    fun setUp() {

        Mockito.reset(mockLocationDetailsRepository)
        Mockito.reset(mockInternetConnectionChecker)

        whenever(mockInternetConnectionChecker.isOnline()).thenReturn(true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getLocation() = runTest {

        val locationId = 2
        SUT = LocationDetailsViewModel(locationId, mockLocationDetailsRepository, mockInternetConnectionChecker, testDispatcher)
        whenever(mockLocationDetailsRepository.getLocation(locationId)).thenReturn(flow {
            emit(ApiState.success(LocationDto(2, "Abadango", "Cluster", "unknown", "6")))
        }.flowOn(testDispatcher))

        val expected = "expected"
            val result = SUT.getLocationValue()

        Assert.assertEquals(result, expected)
    }


    @Test
    fun getCharacters() {
    }
}