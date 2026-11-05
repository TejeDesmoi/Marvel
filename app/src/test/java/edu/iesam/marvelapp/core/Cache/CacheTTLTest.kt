package edu.iesam.marvelapp.core.Cache

import android.content.Context
import android.content.SharedPreferences
import edu.iesam.marvelapp.core.Time.AppTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CacheTTLTest {

    private val context: Context = mockk()
    private val sharedPref: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk(relaxed = true)
    private val appTime: AppTime = mockk()

    private lateinit var appCache: AppCacheTTL

    @Before
    fun setup(){
        every { context.getString(any()) } returns "app_cache"
        every { context.getSharedPreferences("app_cache", Context.MODE_PRIVATE) } returns sharedPref
        every { sharedPref.edit() } returns editor
        every { editor.putLong(any(), any()) } returns editor

        appCache = AppCacheTTL(context,appTime)
    }

    @Test
    fun `updateTime guarda la hora actual con la clave indicada` () {
        //Given
        every { appTime.currentTimeToLong() } returns 1_000L

        //When
        appCache.updateTime("heroes")

        //Then
        verify { editor.putLong("heroes", 1_000L) }
        verify { editor.apply() }

    }

    @Test
    fun `isExpired devuelve false si no ha pasado el ttl`() {
        //Given
        every { sharedPref.getLong("heroes", -1) } returns 1_000L
        every { appTime.currentTimeToLong() } returns 1_500L

        //When
        val result = appCache.isExpired("heroes", ttl = 1_000L)

        //Then
        assertFalse(result)

    }

    @Test
    fun `isExpired devuelve true si ha pasado el ttl`() {
        //Given
        every { sharedPref.getLong("heroes", -1L) } returns 1_000
        every { appTime.currentTimeToLong() } returns 5_000L

        //When
        val result = appCache.isExpired("heroes", ttl = 1_000L)

        //Then
        assertTrue(result)
    }

    @Test
    fun `isExpired devuelve true si justo esta en el ttl`() {
        //Given
        every { sharedPref.getLong("heroes", -1L) } returns 1_000
        every { appTime.currentTimeToLong() } returns 2_000L

        //When
        val result = appCache.isExpired("heroes", ttl = 1_000L)

        //Then
        assertTrue(result)
    }



    @Test
    fun `isExpired devuelve true si la clave nunca se ha guardado`() {
        //Given
        every { sharedPref.getLong("heroes", -1L) } returns -1L
        every { appTime.currentTimeToLong() } returns 5_000L

        //When
        val result = appCache.isExpired("heroes", ttl = 1_000L)

        //Then
        assertTrue(result)
    }
    }
