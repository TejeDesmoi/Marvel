package edu.iesam.marvelapp.features.superheroes.data

import edu.iesam.marvelapp.features.superheroes.data.local.xml.SuperHeroXmlLocalDataSource
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import edu.iesam.marvelapp.features.superheroes.domain.SuperHeroRepository

/**
 * Clase encargada de gestionar la feunte de datos: xml, base de datos, API, etc.
 */

class SuperHeroDataRepository(private val xmlLocalDataSource: SuperHeroXmlLocalDataSource) :
    SuperHeroRepository {

    override fun getAll(): List<SuperHero> {
        return xmlLocalDataSource.getAll()
    }

    override fun save(superheroes: List<SuperHero>) {
        TODO("Not yet implemented")
    }
    override fun findById(id: String): SuperHero? {
        TODO("Not yet implemented")
    }
}