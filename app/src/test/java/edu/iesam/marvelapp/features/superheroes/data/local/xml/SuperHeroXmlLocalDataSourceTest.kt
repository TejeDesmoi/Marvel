package edu.iesam.marvelapp.features.superheroes.data.local.xml

import android.content.Context
import android.content.SharedPreferences
import edu.iesam.marvelapp.core.Cache.AppCache
import edu.iesam.marvelapp.core.serialization.JsonSerializer
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SuperHeroXmlLocalDataSourceTest {

    private val context: Context = mockk()
    private val sharedPref: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)
    private val serializer: JsonSerializer<List<SuperHero>> = mockk()
    private val appCache: AppCache = mockk(relaxed = true)

    private lateinit var dataSource: SuperHeroXmlLocalDataSource

    private val key = "SuperHero"
    private  val ttl = 5*60*1000L

    @Before
    fun setUp() {
        every { context.getString(any()) } returns "superhero_prefs"
        every { context.getSharedPreferences("superhero_prefs", Context.MODE_PRIVATE) } returns sharedPref
        every { sharedPref.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.remove(any()) } returns editor

        dataSource = SuperHeroXmlLocalDataSource(context, serializer, appCache)
    }

    private fun hero(id: String) = SuperHero(
        id = id,
        name = "Hero $id",
        slug = "hero-$id",
        urlImage = "https://example.com/$id.png"
    )

    //save
    @Test
    fun `save guarda el json con la clave SuperHero y actualiza la cache`(){
        //Given
        val heroes = listOf(hero("1"), hero("2"))
        every { serializer.toJson(heroes) } returns "[json]"

        //When
        dataSource.save(heroes)

        //Then
        verify { editor.putString(key,"[json]") }
        verify { editor.apply() }
        verify { appCache.updateTime(key    ) }
    }

    //getAll
    @Test
    fun `getAll devuelve la lista si la cache no ha expirado`() {
        val heroes = listOf(hero("1"))
        every { appCache.isExpired(key, ttl) } returns false
        every { sharedPref.getString(key, null) } returns "[json]"
        every { serializer.fromJson("[json]") } returns heroes

        assertEquals(heroes, dataSource.getAll())
    }

    @Test
    fun `getAll devuelve vacio y no deserializa si la cache ha expirado`() {
        every { appCache.isExpired(key, ttl) } returns true

        val result = dataSource.getAll()

        assertTrue(result.isEmpty())
        verify(exactly = 0) { serializer.fromJson(any()) }
    }

    @Test
    fun `getAll devuelve vacio si no hay json guardado`() {
        every { appCache.isExpired(key, ttl) } returns false
        every { sharedPref.getString(key, null) } returns null

        assertTrue(dataSource.getAll().isEmpty())
        verify(exactly = 0) { serializer.fromJson(any()) }
    }

    //findById
    @Test
    fun `findById devuelve el heroe si existe`() {
        val h1 = hero("1"); val h2 = hero("2")
        every { appCache.isExpired(key, ttl) } returns false
        every { sharedPref.getString(key, null) } returns "[json]"
        every { serializer.fromJson("[json]") } returns listOf(h1, h2)

        assertEquals(h2, dataSource.findById("2"))
    }

    @Test
    fun `findById devuelve null si no existe`() {
        every { appCache.isExpired(key, ttl) } returns false
        every { sharedPref.getString(key, null) } returns "[json]"
        every { serializer.fromJson("[json]") } returns listOf(hero("1"))

        assertNull(dataSource.findById("99"))
    }

    //deleteById
    @Test
    fun `deleteById guarda la lista sin el heroe borrado`() {
        val h1 = hero("1"); val h2 = hero("2")
        every { appCache.isExpired(key, ttl) } returns false
        every { sharedPref.getString(key, null) } returns "[json]"
        every { serializer.fromJson("[json]") } returns listOf(h1, h2)
        every { serializer.toJson(any()) } returns "[json nuevo]"

        dataSource.deleteById("1")

        verify(exactly = 1) { serializer.toJson(listOf(h2)) }
        verify(exactly = 1) { editor.putString(key, "[json nuevo]") }
    }

    //deleteAll
    @Test
    fun `deleteAll borra la clave y actualiza la cache`() {
        dataSource.deleteAll()

        verify(exactly = 1) { editor.remove(key) }
        verify(exactly = 1) { editor.apply() }
        verify(exactly = 1) { appCache.updateTime(key) }
    }
}