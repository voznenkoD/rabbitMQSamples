package routing;
import static routing.Collector.EXCHANGE_NAME;
import static routing.Collector.EXCHANGE_TYPE;

import com.rabbitmq.client.*;
import java.util.Random;

public class Emitter {

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            var severities = new String[]{"[info]", "[debug]", "[warn]", "[error]"};

            for (int i = 0; i < 1000; i++) {
                Random r=new Random();
                var severity=severities[r.nextInt(severities.length)];
                var message = "I am just a message";
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
                System.out.println("Sent " + severity + ":'" + message + "'");
            }
        }
    }
}
