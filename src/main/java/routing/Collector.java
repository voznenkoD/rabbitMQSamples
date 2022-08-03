package routing;
import com.rabbitmq.client.*;

public class Collector {

    static final String EXCHANGE_NAME = "direct_logs";
    static final String EXCHANGE_TYPE = "direct";

    public static void main(String[] argv) throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection();
        var channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        var queueName = channel.queueDeclare().getQueue();

        for (String severity : argv) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println("Ready to collect.");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var message = new String(delivery.getBody());
            System.out.println("Received " + delivery.getEnvelope().getRoutingKey() + ":'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
