package pubsub;
import com.rabbitmq.client.*;

public class Publisher{
    static final String EXCHANGE_NAME = "logs";
    static final String EXCHANGE_TYPE = "fanout";

    public static void main(String[] argv) throws Exception {
        var factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            var message = argv.length < 1 ? "info: Hello World!" : String.join(" ", argv);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("Sent '" + message + "'");
        }
    }
}