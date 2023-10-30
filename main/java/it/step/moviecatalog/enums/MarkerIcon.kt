package it.step.moviecatalog.enums

import it.step.moviecatalog.R

enum class MarkerIcon(val id: Int) {

    LOCATION_MARKER(R.drawable.ic_location),
    SELECTED_LOCATION_MARKER(R.drawable.ic_selected_location),
    MY_LOCATION_MARKER(R.drawable.ic_my_location)

//    operator fun invoke() = id

}