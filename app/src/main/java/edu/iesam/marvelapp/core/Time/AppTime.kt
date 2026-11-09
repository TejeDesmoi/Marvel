package edu.iesam.marvelapp.core.Time

interface AppTime{
    fun currentTimeToLong(): Long
}

class LocalAppTime: AppTime {
    override fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }
}