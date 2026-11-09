package edu.iesam.marvelapp.core.Time

import org.junit.Assert.*
import org.junit.Test

class AppTimeTest {
    private val appTime = LocalAppTime()

    @Test
    fun `currentTimeToLong devuelve la hora actual del sistema`() {
        //Given
        val before = System.currentTimeMillis()

        //When
        val result = appTime.currentTimeToLong()

        //Then
        val after = System.currentTimeMillis()
        assertTrue(result in before..after)
    }

    @Test
    fun `currentTimeToLong no devuelve un valor menor en llamadas sucesivas`() {
        //When
        val first = appTime.currentTimeToLong()
        val second = appTime.currentTimeToLong()

        //Then
        assertTrue(second >= first)
    }
}