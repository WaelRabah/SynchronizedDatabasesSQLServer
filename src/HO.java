import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.sql.*;
public class HO {
    private final static String QUEUE_NAME = "HO";

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    private final static String dbName="HO";
    private String siteName;
    public String getDbName() {
        return dbName;
    }
private void createDB()
{
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
public HO() throws Exception
{
    this.setSiteName("HO");
    this.createDB();
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(this.getSiteName()+" Received '" + message + "'");
        Product p = null;
            p =parseObjectFromString(message);
            this.sendDataToDB(p);

    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
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
