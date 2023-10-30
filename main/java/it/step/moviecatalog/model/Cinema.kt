package it.step.moviecatalog.model

data class Cinema(
    val id_cinema: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val photo: String
)