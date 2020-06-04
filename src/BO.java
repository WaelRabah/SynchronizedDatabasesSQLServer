import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;

public class BO {
    private final static String Send_QUEUE_NAME = "HO";
    private static int bo_counter = 0 ;
    private  String receive_queue ;
    private String dbName;
    private String siteName;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getReceive_queue() {
        return receive_queue;
    }

    public void setReceive_queue(String receive_queue) {
        this.receive_queue = receive_queue;
    }
    private void createDB()
    {

        setReceive_queue("BO"+bo_counter);
        setDbName("BO"+bo_counter);
        try{
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection con= DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=root");
            Statement stmt=con.createStatement();
            Boolean result = stmt.execute("CREATE DATABASE if not exists "+this.getDbName());

            con.close();
        }catch(Exception e){ System.out.println(e);}
        try{
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+this.getDbName(),"root","root");
            Statement stmt=con.createStatement();

            String sql = "create table if not exists product(\n" +
                    "\t\t\t\tid Varchar(100) primary key,\n" +
                    "                date varchar(50),\n" +
                    "                region varchar(50),\n" +
                    "                product varchar(50),\n" +
                    "                qty float,\n" +
                    "                cost float ,\n" +
                    "                amt float,\n" +
                    "                tax float,\n" +
                    "                total float\n" +
                    "                );";
            stmt.executeUpdate(sql);
            con.close();
        }catch(Exception e){ System.out.println(e);}

    }
    private void sendOldData()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+this.getDbName(),"root","root");
            Statement stmt=con.createStatement();

            String sql ="select * from product";
           ResultSet products = stmt.executeQuery(sql);
           while (products.next())
           {
               String id = products.getString(1);
               String date = products.getString(2);
               String region = products.getString(3);
               String product = products.getString(4);
               int qty = products.getInt(5);
               float cost = products.getFloat(6);
               float amt = products.getFloat(7);
               float tax = products.getFloat(8);
               float total = products.getFloat(9);

               Product p = new Product(id,date,region,product,qty,cost,amt,tax,total);
               this.sendDataToHo(p);
           }
           stmt.close();
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
    public BO () throws Exception
    {
        bo_counter ++;
        this.setSiteName("bo"+BO.bo_counter);
        this.createDB();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(this.receive_queue, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(this.getSiteName()+" Received '" + message + "'");


            Product p = null;

            p = parseObjectFromString(message);
            if (this.testConnection()) {

                try {
                    this.sendOldData();
                    this.sendDataToHo(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                this.sendDataToDB(p);
            }









        };
        channel.basicConsume(this.receive_queue, true, deliverCallback, consumerTag -> { });
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
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void sendDataToHo( Product p) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(BO.Send_QUEUE_NAME, false, false, false, null);
            String message = p.toString();
            channel.basicPublish("", BO.Send_QUEUE_NAME, null, message.getBytes());
        }
    }
    private Boolean sendDataToDB(Product p)
    {

        try{
            Class.forName("com.mysql.jdbc.Driver");

            java.sql.Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+this.getDbName(),"root","root");

            String sql = "insert into product (id,Date,Region,Product,Qty,Cost,Amt,Tax,Total) values (?,?,?,?,?,?,?,?,?);";
            PreparedStatement stmt=con.prepareStatement(sql);
            stmt.setString(1,p.getId());
            stmt.setString(2,p.getDate());
            stmt.setString(3,p.getRegion());
            stmt.setString(4,p.getProduct());
            stmt.setFloat(5,p.getQty());
            stmt.setFloat(6,p.getCost());
            stmt.setFloat(7,p.getAmt());
            stmt.setFloat(8,p.getTax());
            stmt.setFloat(9,p.getTotal());
            stmt.execute();
            con.close();


        }catch(Exception e){ System.out.println(e);}
        return true;
    }
    public  Product parseObjectFromString(String s)  {

        String [] fields = s.split(" ");

        return new Product(fields[0],fields[1],fields[2],fields[3],Integer.parseInt(fields[4]),Float.parseFloat(fields[5]),Float.parseFloat(fields[6]),Float.parseFloat(fields[7]),Float.parseFloat(fields[8]));
    }

}
