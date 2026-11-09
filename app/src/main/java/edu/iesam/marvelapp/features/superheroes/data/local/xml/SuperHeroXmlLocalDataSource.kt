package edu.iesam.marvelapp.features.superheroes.data.local.xml

import android.content.Context
import edu.iesam.marvelapp.R
import edu.iesam.marvelapp.core.Cache.AppCache
import edu.iesam.marvelapp.core.serialization.JsonSerializer
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero

/**
 * Conector para gestionar datos de un fichero XML a traves de SharedPreferences
 *
 */


class SuperHeroXmlLocalDataSource(
    private val context: Context,
    private val serializer: JsonSerializer<List<SuperHero>>,
    private val appCache: AppCache
) {

    //Creo la fuente de datos XML: Shared preferences
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.local_source_superhero), Context.MODE_PRIVATE
    )


    //Metodo guardar lista de superhero
    fun save(superHeroes: List<SuperHero>) {
        //apply{ } Scope Function
        sharedPref.edit().apply {
            putString(keySuperHero, serializer.toJson(superHeroes))
            apply()
        }
        appCache.updateTime(keySuperHero)
    }

    //Metodo para recuperar una lista de superhero
    public fun getAll(): List<SuperHero> {
        if (!appCache.isExpired(keySuperHero,ttl)) {
            val json: String? = sharedPref.getString(keySuperHero, null)
            json?.let {
                return serializer.fromJson(it)
            }
        }

        return emptyList()

    }

    fun findById(id: String): SuperHero? {
        val superheroes: List<SuperHero> = getAll()
        val superhero: SuperHero? = superheroes.find {
            it.id == id
        }
        return superhero
    }

    fun deleteById(id: String) {
        save(getAll().filterNot { it.id == id })
    }

    fun deleteAll() {
        sharedPref.edit().apply {
            remove(keySuperHero)
            apply()
        }
        appCache.updateTime(keySuperHero)
    }

    companion object {
        private val keySuperHero: String = "SuperHero"
        private val ttl: Long = 5 * 60 * 1000L
    }
}