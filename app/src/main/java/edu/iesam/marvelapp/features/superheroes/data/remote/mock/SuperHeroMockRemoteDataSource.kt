package edu.iesam.marvelapp.features.superheroes.data.remote.mock

import edu.iesam.marvelapp.features.superheroes.domain.SuperHero

class SuperHeroMockRemoteDataSource {

    fun obtainAll(): List<SuperHero>{
        return listOf(
            SuperHero("1","superhero1","sh1","https://1"),
            SuperHero("2","superhero2","sh2","https://2"),
            SuperHero("3","superhero3","sh3","https://3"),
            SuperHero("4","superhero4","sh4","https://4")
        )
    }
}