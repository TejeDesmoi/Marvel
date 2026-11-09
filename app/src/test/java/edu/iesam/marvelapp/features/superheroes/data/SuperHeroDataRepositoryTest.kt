package edu.iesam.marvelapp.features.superheroes.data

import edu.iesam.marvelapp.features.superheroes.data.local.xml.SuperHeroXmlLocalDataSource
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class SuperHeroDataRepositoryTest {
    private val xmlLocalDataSource: SuperHeroXmlLocalDataSource = mockk()

    private lateinit var repository: SuperHeroDataRepository

    @Before
    fun setup() {
        repository = SuperHeroDataRepository(xmlLocalDataSource)
    }

    private fun hero(id: String) = SuperHero(
        id = id,
        name = "Hero $id",
        slug = "hero-$id",
        urlImage = "https://example.com/$id.png"
    )

    @Test
    fun `getAll devuelve la lista del data source`() {
        //Given
        val heroes = listOf(hero("1"), hero("2"))
        every { xmlLocalDataSource.getAll() } returns heroes

        //When
        val result = repository.getAll()

        //Then
        assertEquals(heroes, result)
        verify(exactly = 1) { xmlLocalDataSource.getAll() }
    }

    @Test
    fun `save delega en el data source con la lista recibida`() {
        //Given
        val heroes = listOf(hero("1"))
        every { xmlLocalDataSource.save(heroes) } just runs

        //When
        repository.save(heroes)

        //Then
        verify(exactly = 1) { xmlLocalDataSource.save(heroes) }
    }

    @Test
    fun `findById devuelve el heroe del data source`() {
        //Given
        val h1 = hero("1")
        every { xmlLocalDataSource.findById("1") } returns h1

        //When
        val result = repository.findById("1")

        //Then
        assertEquals(h1, result)
        verify(exactly = 1) { xmlLocalDataSource.findById("1") }
    }

    @Test
    fun `findById devuelve null si el data source no lo encuentra`() {
        //Given
        every { xmlLocalDataSource.findById("99") } returns null

        //When
        val result = repository.findById("99")

        //Then
        assertNull(result)
    }

    @Test
    fun `deleteAll delega en el data source`() {
        //Given
        every { xmlLocalDataSource.deleteAll() } just runs

        //When
        repository.deleteAll()

        //Then
        verify(exactly = 1) { xmlLocalDataSource.deleteAll() }
    }

    @Test
    fun `deleteById delega en el data source con el id recibido`() {
        //Given
        every { xmlLocalDataSource.deleteById("1") } just runs

        //When
        repository.deleteById("1")

        //Then
        verify(exactly = 1) { xmlLocalDataSource.deleteById("1") }
    }
}