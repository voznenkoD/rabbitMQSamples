package workqueues;

import static workqueues.TaskSender.TASK_QUEUE_NAME;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

public class WorkerReceiver {

    public static void main(String[] argv) throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection();
        var channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Ready to receive.");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println("Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println("Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}