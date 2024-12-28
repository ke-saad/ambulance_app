package com.example.ambulanceapp.beans

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "hospital")
data class Hospital(
    @field:Element(name = "id")
    var id: Long = 0,

    @field:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "address")
    var address: String? = null,

    @field:Element(name = "phone")
    var phone: String? = null,

    @field:Element(name = "email")
    var email: String? = null,

    @field:Element(name = "latitude")
    var latitude: Double = 0.0,

    @field:Element(name = "longitude")
    var longitude: Double = 0.0,
    @field:ElementList(entry = "specialty", inline = true, required = false)
    var specialties: List<String>? = null
)