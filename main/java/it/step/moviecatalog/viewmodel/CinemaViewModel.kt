package it.step.moviecatalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.step.moviecatalog.configuration.ApiManager
import it.step.moviecatalog.model.Cinema
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CinemaViewModel : ViewModel() {

    private var apiService = ApiManager.movieService
    val cinema = MutableLiveData<Cinema?>(null)
    val cinemaList = MutableLiveData<List<Cinema>?>(emptyList())
    val searchedCinemas = MutableLiveData<List<Cinema>?>(emptyList())

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(false)

    fun getAllCinemas() {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.getAllCinemas()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            cinemaList.postValue(response.body())
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                        }
                    } else {
                        _isLoading.value = false
                    }
                }

            }catch (e: Exception){
                _isLoading.value = false
            }

        }
    }

    fun findCinemaByName(name : String) {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.findCinemaByName(name)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            searchedCinemas.postValue(response.body())
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                        }
                    } else {
                        _isLoading.value = false
                    }
                }

            }catch (e: Exception){
                _isLoading.value = false
            }

        }
    }

    fun getCinemaByID(id : Int) {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.getCinemaByID(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            cinema.postValue(response.body())
                            _isLoading.value = false
                        } else {
                            _isLoading.value = false
                        }
                    } else {
                        _isLoading.value = false
                    }
                }

            }catch (e: Exception){
                _isLoading.value = false
            }

        }
    }
}