package workqueues;

import com.rabbitmq.client.*;

public class TaskSender {

    static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");

        for (int i = 0; i < 5; i++) {
            try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

                var message = "I am message numero " + i;

                channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("Sent '" + message + "'");
            }
        }
    }
}
