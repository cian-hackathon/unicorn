package com.thelads.service

import com.google.cloud.datastore.Entity
import com.thelads.model.Unicorn
import com.thelads.model.UnicornCreationRequest
import com.thelads.publisher.UnicornPublisher
import com.thelads.util.DatastoreQuery
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
open class UnicornService {

    @Autowired
    private lateinit var unicornPublisher: UnicornPublisher

    val recentUpdates: List<Unicorn>
        get() {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            return aliveUnicornsWithLatestCoOrdinates.map { entity ->
                Unicorn(
                        name = entity.getString("name"),
                        distance = entity.getDouble("distance"),
                        latitude = entity.getDouble("latitude"),
                        longitude = entity.getDouble("longitude"),
                        statusTime = LocalDateTime.parse(entity.getTimestamp("statusTime").toString(), formatter).toString(),
                        healthPoints = entity.getLong("healthPoints").toInt(),
                        magicPoints = entity.getLong("magicPoints").toInt())
            }
        }

    private val aliveUnicornsWithLatestCoOrdinates: List<Entity>
        get() {
            val distinctNames = mutableListOf<String>()
            val entityList = mutableListOf<Entity>()

            val allTasks = DatastoreQuery().listTasks()
            allTasks.forEachRemaining { entity ->
                if (!distinctNames.contains(entity.getString("name"))) {
                    distinctNames.add(entity.getString("name"))
                    if (entity.getBoolean("alive")) {
                        entityList.add(entity)
                    }
                }
            }

            return entityList
        }

    val aliveUnicorns: List<String>
        get() = aliveUnicornsWithLatestCoOrdinates.map {
            entity -> entity.getString("name")
        }

    open fun createUnicorn(request: UnicornCreationRequest) {
        if (request.latitude == null || request.longitude == null) {
            unicornPublisher.addUnicorn(Unicorn(name = request.name))
        } else {
            unicornPublisher.addUnicorn(Unicorn(name = request.name, latitude = request.latitude,
                    longitude = request.longitude))
        }
    }

    @Async
    open fun removeUnicorn(name: String) {
        val unicornToKill = aliveUnicornsWithLatestCoOrdinates.firstOrNull { entity ->
            entity.getString("name") == name
        }

        unicornToKill?.run {
            unicornPublisher.getUnicornByName(name)?.let { unicorn ->
                unicorn.isAlive = false
                Thread.sleep(11000)
                unicornPublisher.getUnicorns().remove(unicorn)
            } ?: run {
                // The DB contains a unicorn that's no longer available in memory
                val unicorn = Unicorn(name = name, statusTime = LocalDateTime.now().toString(), isAlive = false)
                unicornPublisher.pubSubMessagePublisher.invoke(unicorn)
            }
        }
    }
}
