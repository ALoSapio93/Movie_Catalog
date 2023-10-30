package it.step.moviecatalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.step.moviecatalog.configuration.ApiManager
import it.step.moviecatalog.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel : ViewModel() {


    private var apiService = ApiManager.movieService
    val movie = MutableLiveData<Movie?>(null)
    val searchedMovies = MutableLiveData<List<Movie>?>(emptyList())
    val genreMovies = MutableLiveData<List<Movie>?>(emptyList())

    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData(false)

    val moviesList = MutableLiveData<List<Movie>?>(null)
    val seriesList = MutableLiveData<List<Movie>?>(null)
    val gamesList = MutableLiveData<List<Movie>?>(null)


    fun getAllMoviesByCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
            val response = apiService.getAllMovies()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        genreMovies.postValue(response.body())
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

    fun findByGenre(genre: String) {
        _isLoading.value = true
        viewModelScope.launch {

            try {
                val response = apiService.findMovieByGenre(genre)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            genreMovies.postValue(response.body())
                            _isLoading.value = false
                        } else {
                            //TODO
                            _isLoading.value = false
                        }
                    } else {
                        //TODO
                        _isLoading.value = false
                    }
                }
            }catch (e: Exception){
                _isLoading.value = false
            }

        }
    }


    fun getByID(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getMovieByID(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            movie.postValue(response.body())
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

    fun initMovieList() {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.findMovieByType("movie")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            moviesList.postValue(response.body())
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

    fun initSeriesList() {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.findMovieByType("series")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            seriesList.postValue(response.body())
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

    fun initGamesList() {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.findMovieByType("game")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            gamesList.postValue(response.body())
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

    fun initSearchList() {
        _isLoading.value = true
        viewModelScope.launch {

            try {

                val response = apiService.getAllMovies()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            searchedMovies.postValue(response.body())
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