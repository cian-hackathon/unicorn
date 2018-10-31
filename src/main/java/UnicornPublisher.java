import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.util.Arrays;
import java.util.List;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

public class UnicornPublisher {

    // use the default project id
    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    /**
     * Publish messages to a topic.
     *
     * @param args topic name, number of messages
     */
    public static void main(String[] args) throws Exception {
        String topicId = "unicornTopic";

        ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);

        ObjectMapper mapper = new ObjectMapper();

        Unicorn shadowFax = new Unicorn("shadowFax");
        Unicorn happyMcFabulous = new Unicorn("happyMcFabulous");

        List<Unicorn> unicorns = Arrays.asList(shadowFax, happyMcFabulous);

        Publisher publisher = Publisher.newBuilder(topicName).build();

        while (true) {
            // Create a publisher instance with default settings bound to the topic
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
                        System.out.println("Published with message id: " + messageId);
                    }

                    public void onFailure(Throwable t) {
                        System.out.println("Failed to publish: " + t);
                    }
                }, directExecutor());

                Thread.sleep(5000);
            }
        }
    }
}
