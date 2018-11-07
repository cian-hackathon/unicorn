package com.thelads.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PubsubMessage

import com.thelads.model.Unicorn
import java.util.concurrent.CopyOnWriteArrayList
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import com.google.common.util.concurrent.MoreExecutors.directExecutor

@Component
@EnableScheduling
class UnicornPublisher {

    // required to avoid java.util.ConcurrentModificationException when removing a unicorn
    private val unicorns = CopyOnWriteArrayList<Unicorn>()
    private val mapper = ObjectMapper()
    private var publisher: Publisher

    init {
        val topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID)
        publisher = Publisher.newBuilder(topicName).build()

        val shadowFax = Unicorn(name = "ShadowFax")
        val happyMcFabulous = Unicorn(name = "HappyMcFabulous")

        unicorns.add(shadowFax)
        unicorns.add(happyMcFabulous)
    }

    // convert message to bytes
    // Schedule a message to be published. Messages are automatically batched.
    val pubSubMessagePublisher: (Unicorn) -> Unit = { unicorn ->
        val data = ByteString.copyFromUtf8(mapper.writeValueAsString(unicorn))
        val pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build()
        val future = publisher.publish(pubsubMessage)
        ApiFutures.addCallback(future, object : ApiFutureCallback<String> {
            override fun onSuccess(messageId: String) {
                println(unicorn.name + ": Published with message id: " + messageId)
            }

            override fun onFailure(t: Throwable) {
                println(unicorn.name + ": Failed to publish. Reason: " + t)
            }
        }, directExecutor())
    }

    @Scheduled(fixedRate = 5000)
    fun run() {
        for (unicorn in unicorns) {
            unicorn.move()
            pubSubMessagePublisher.invoke(unicorn)
        }
    }

    fun addUnicorn(unicorn: Unicorn) {
        if (unicorns.any { u -> u.name == unicorn.name }) {
            throw IllegalArgumentException("Unicorn with the name " + unicorn.name + " is already alive.")
        }
        unicorns.add(unicorn)
    }

    fun getUnicorns(): MutableList<Unicorn> {
        return unicorns
    }

    fun getUnicornByName(name: String): Unicorn? {
        return unicorns.firstOrNull { unicorn -> unicorn.name == name }
    }

    companion object {
        // use the default project id
        private val PROJECT_ID = ServiceOptions.getDefaultProjectId()
        private const val TOPIC_ID = "unicornTopic"
    }
}
