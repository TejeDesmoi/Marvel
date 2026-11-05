package edu.iesam.marvelapp.features.superheroes.domain

interface SuperHeroRepository {
    fun getAll(): List<SuperHero>
    fun save(superheroes: List<SuperHero>)
    fun findById(id: String): SuperHero?
}