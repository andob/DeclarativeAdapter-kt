package ro.andreidobrescu.declarativeadapterktsample.model

import java.io.Serializable

class Restaurant
(
    val name     : String,
    val location : String,
    val rating   : Int,
    val image    : String
) : Serializable