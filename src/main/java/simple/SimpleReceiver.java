package simple;

import com.rabbitmq.client.*;

public class SimpleReceiver {
    private final static String QUEUE_NAME = "test_1";

    public static void main(String[] argv) throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection();
        var channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Ready to receive.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var message = new String(delivery.getBody());
            try {
                doWork(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("Done");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(10000);
        }
        System.out.println("Received '" + task + "'");
    }
}
