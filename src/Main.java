import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Main {
    public static   Product parseObjectFromString(String s)  {

        System.out.println(s);
        String [] fields = s.split(" ");

        return new Product(fields[0],fields[1],fields[2],Integer.parseInt(fields[3]),Float.parseFloat(fields[4]),Float.parseFloat(fields[5]),Float.parseFloat(fields[6]),Float.parseFloat(fields[7]));
    }

    public static void main (String [] args) throws Exception
    {

        HO ho = new HO();
        BO bo1= new BO();
        BO bo2= new BO();
        Client c = new Client();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("client", false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Client"+" Received '" + message + "'");


            Product p = null;

            p=Main.parseObjectFromString(message);
            System.out.println(p);
            c.sendData(p);

        };
        channel.basicConsume("client", true, deliverCallback, consumerTag -> { });


    }

}
