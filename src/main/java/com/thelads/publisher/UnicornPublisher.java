package com.thelads.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import com.thelads.model.Unicorn;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

@Component
@EnableScheduling
public class UnicornPublisher {

    // use the default project id
    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();
    private static final String TOPIC_ID = "unicornTopic";

    // required to avoid java.util.ConcurrentModificationException when
    // removing a unicorn
    private final List<Unicorn> unicorns = new CopyOnWriteArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private Publisher publisher;

    public UnicornPublisher() throws IOException {
        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, TOPIC_ID);
        publisher = Publisher.newBuilder(topicName).build();

        Unicorn shadowFax = new Unicorn("ShadowFax");
        Unicorn happyMcFabulous = new Unicorn("HappyMcFabulous");

        unicorns.add(shadowFax);
        unicorns.add(happyMcFabulous);
    }

    @Scheduled(fixedRate = 5000)
    public void run() throws Exception {

        for (Unicorn unicorn : unicorns) {
            unicorn.move();
            // convert message to bytes
            ByteString data = ByteString.copyFromUtf8(mapper.writeValueAsString(unicorn));
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build();

            // Schedule a message to be published. Messages are automatically batched.
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            ApiFutures.addCallback(future, new ApiFutureCallback<String>() {
                public void onSuccess(String messageId) {
                    System.out.println(unicorn.getName() + ": Published with message id: " + messageId);
                }

                public void onFailure(Throwable t) {
                    System.out.println(unicorn.getName() + ": Failed to publish. Reason: " + t);
                }
            }, directExecutor());
        }
    }

    public void addUnicorn(Unicorn unicorn) {
        if (unicorns
            .stream()
            .anyMatch(u -> u.getName().equals(unicorn.getName()))) {
            throw new IllegalArgumentException("Unicorn with the name " + unicorn.getName() + " is already alive.");
        }
        unicorns.add(unicorn);
    }

    public List<Unicorn> getUnicorns() {
        return unicorns;
    }
}
