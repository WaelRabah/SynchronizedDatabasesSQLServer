const express = require("express");
const App = express();
const cors = require("cors");
var mysql = require('mysql')
const amqp = require('amqplib/callback_api')

var connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'root',
    database: 'ho'
  })

  connection.connect(err=>console.log(err))


  App.use(cors());
  App.use(express.json());

  App.get('/products',(req,res)=>{
        connection.query("select * from product",(err,results,fields)=>{

            return res.status(200).json(results)
        })
  })
  App.post('/products',(req,res)=>{

        const {body} = req
        amqp.connect('amqp://localhost', function(error0, connection) {

            if (error0) {
                throw error0;
              }
              connection.createChannel(function(error1, channel) {
                if (error1) {
                    throw error1;
                  }

                  var queue = 'client'
                  var msg = `${body.date} ${body.region} ${body.product} ${body.qty} ${body.cost} ${body.amt} ${body.tax} ${body.total}`;
              
                  channel.assertQueue(queue, {
                    durable: false
                  });
              
                  channel.sendToQueue(queue, Buffer.from(msg));
                  console.log(" [x] Sent %s", msg);
                  res.status(200).send("success")
              });
        });

    

  })
  App.listen(5000,()=>{
      console.log('server running on port 5000')
  })