package com.example.ambulanceapp.beans

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "ambulance")
data class Ambulance(
    @field:Element(name = "id")
    var id: Long = 0,

    @field:Element(name = "registrationNumber")
    var registrationNumber: String? = null,

    @field:Element(name = "model")
    var model: String? = null,

    @field:Element(name = "status")
    var status: String? = null,

    @field:Element(name = "latitude")
    var latitude: Double = 0.0,

    @field:Element(name = "longitude")
    var longitude: Double = 0.0,

    @field:Element(name = "specialty")
    var specialty: String? = null,

    @field:Element(name = "phone")
    var phone: String? = null,
)