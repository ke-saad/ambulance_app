package com.example.ambulanceapp.beans

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "nearestAmbulance")
data class NearestAmbulance(
    @field:Element(name = "id", required = false)
    var id: Long? = null,

    @field:Element(name = "registrationNumber", required = false)
    var registrationNumber: String? = null,

    @field:Element(name = "model", required = false)
    var model: String? = null,

    @field:Element(name = "status", required = false)
    var status: String? = null,

    @field:Element(name = "latitude", required = false)
    var latitude: Double = 0.0,

    @field:Element(name = "longitude", required = false)
    var longitude: Double = 0.0,

    @field:Element(name = "distance", required = false)
    var distance: Double = 0.0,

    @field:Element(name = "phone", required = false)
    var phone: String? = null
)