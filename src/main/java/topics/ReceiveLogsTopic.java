package topics;

import static topics.EmitLogTopic.EXCHANGE_NAME;

import com.rabbitmq.client.*;

public class ReceiveLogsTopic {

    public static void main(String[] argv) throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection();
        var channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        var queueName = channel.queueDeclare().getQueue();

        for (String bindingKey : argv) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
            System.out.println("Ready to receive for " + bindingKey);
        }

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var message = new String(delivery.getBody());
            System.out.println("Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
