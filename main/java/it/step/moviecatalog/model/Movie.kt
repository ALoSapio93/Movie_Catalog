package it.step.moviecatalog.model

import java.util.Date

data class Movie(
    var imdbID : String,
    var title: String,
    var year: String?,
    var rated: String?,
    var released: Date?,
    var runtime: String?,
    var gerne: String?,
    var director: String?,
    var writer: String?,
    var actors: String?,
    var plot: String?,
    var language: String?,
    var awards: String?,
    var poster: String?,
    var metascore: String?,
    var imdbrating: String?,
    var imdbvotes: String?,
    var type: String?,
    var dvd: Date?,
    var boxoffice: String?,
    var production: String?,
    var website: String?,
    var response: Boolean?,
    var totalseason: Int?,
)
