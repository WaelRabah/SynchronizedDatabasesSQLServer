import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeoutException;

public class Client {
    private  static String [] QUEUE_NAMES = {"BO1","BO2"};
    public void sendData (Product product)
    {
        System.out.println("product");
        for (String queueName : QUEUE_NAMES) {
            try
            {
                this.sendDataToBo(queueName,product);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }


        }
    }
    public Boolean testConnection()
    {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return  true;
        } catch (MalformedURLException e) {
            return  false ;
        } catch (IOException e) {
            return  false ;
        }
    }




    private void sendDataToBo(String queueName , Product p) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(queueName, false, false, false, null);
            String message = p.toString();
            channel.basicPublish("", queueName, null, message.getBytes());
        }
    }

}
