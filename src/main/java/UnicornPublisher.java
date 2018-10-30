import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnicornPublisher {

    // use the default project id
    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    /** Publish messages to a topic.
     * @param args topic name, number of messages
     */
    public static void main(String[] args) throws Exception {
        String topicId = "unicornTopic";

        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
        Publisher publisher = null;
        List<ApiFuture<String>> futures = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        Unicorn shadowFax = new Unicorn("shadowFax");
        Unicorn happyMcFabulous = new Unicorn("happyMcFabulous");

        List<Unicorn> unicorns = Arrays.asList(shadowFax, happyMcFabulous);

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            for (Unicorn unicorn : unicorns) {
                // convert message to bytes
                ByteString data = ByteString.copyFromUtf8(mapper.writeValueAsString(unicorn));
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .build();

                // Schedule a message to be published. Messages are automatically batched.
                ApiFuture<String> future = publisher.publish(pubsubMessage);
                futures.add(future);
            }
        } finally {
            // Wait on any pending requests
            List<String> messageIds = ApiFutures.allAsList(futures).get();

            for (String messageId : messageIds) {
                System.out.println(messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
            }
        }
    }
}
