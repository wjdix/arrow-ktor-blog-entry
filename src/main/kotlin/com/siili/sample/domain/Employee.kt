package com.siili.sample.domain

import io.requery.*

@Entity(immutable = true)
data class Employee (

    @get:Key @get:Generated val id: Long,

    @get:Column(name = "first_name") val firstName: String,
    @get:Column(name = "last_name") val lastName: String

): Persistable
