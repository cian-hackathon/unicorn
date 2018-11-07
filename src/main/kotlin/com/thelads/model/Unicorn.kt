package com.thelads.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import java.util.Random

class Unicorn(var distance: Double = 0.0,
              var healthPoints: Int = 100,
              var latitude: Double = 53.3419,
              var longitude: Double = -6.2827,
              var magicPoints: Int = 100,
              var name: String,
              var statusTime: String = LocalDateTime.now().toString(),
              var isAlive: Boolean = true){

    @JsonIgnore
    private val random = Random()

    fun move() {
        this.statusTime = LocalDateTime.now().toString()
        this.healthPoints = nextPoints(healthPoints)
        this.magicPoints = nextPoints(magicPoints)
        val bearing = random.nextInt(360).toDouble()
        val distanceToTravel = Random().nextInt(MAX_DISTANCE - MIN_DISTANCE).toDouble() + MIN_DISTANCE + Math.random()
        this.distance += distanceToTravel
        this.latitude = nextLatitude(latitude, bearing, distanceToTravel)
        this.longitude = nextLongitude(longitude, bearing, distanceToTravel)
    }

    private fun nextLatitude(currentLatitude: Double, bearing: Double, distance: Double): Double {
        return currentLatitude + distance * Math.cos(bearing) / LATITUDE_OFFSET
    }

    private fun nextLongitude(currentLongitude: Double, bearing: Double, distance: Double): Double {
        return currentLongitude + distance * Math.sin(bearing) / LONGITUDE_OFFSET
    }

    private fun nextPoints(points: Int): Int {
        var y = random.nextInt(2)

        if (random.nextInt(2) % 2 == 0) {
            y *= -1
        }

        return points + y
    }

    companion object {
        private const val LATITUDE_OFFSET = 110540.0
        private const val LONGITUDE_OFFSET = 111320.0
        private const val MAX_DISTANCE = 31
        private const val MIN_DISTANCE = 29
    }
}
