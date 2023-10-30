package it.step.moviecatalog.service

import it.step.moviecatalog.model.Cinema
import it.step.moviecatalog.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie")
    suspend fun getAllMovies() : Response<List<Movie>>

    @GET("movie/{id}")
    suspend fun getMovieByID(@Path("id") id : String) : Response<Movie>

    @GET("movie/find/{title}")
    suspend fun findMovieByTitle(@Path("title") title : String) : Response<List<Movie>>

    @GET("movie/genre/{genre}")
    suspend fun findMovieByGenre(@Path("genre") genre : String) : Response<List<Movie>>

    @GET("movie/type/{type}")
    suspend fun findMovieByType(@Path("type") type : String) : Response<List<Movie>>

    @GET("cinema")
    suspend fun getAllCinemas() : Response<List<Cinema>>

    @GET("cinema/{id}")
    suspend fun getCinemaByID(@Path("id") id : Int) : Response<Cinema>

    @GET("cinema/find/{name}")
    suspend fun findCinemaByName(@Path("name") title : String) : Response<List<Cinema>>

}