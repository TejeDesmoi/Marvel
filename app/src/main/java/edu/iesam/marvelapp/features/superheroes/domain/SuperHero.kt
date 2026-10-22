package edu.iesam.marvelapp.features.superheroes.domain

import android.media.Image

data class SuperHero(
    val id: String,
    val name: String,
    val slug: String,
    val urlImage: String
)