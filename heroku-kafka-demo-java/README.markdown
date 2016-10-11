# heroku-kafka-demo-java

A simple heroku app that demonstrates using Kafka in java.
This demo app accepts HTTP POST requests and writes them to a topic, and has a simple page that shows the last 10 messages produced to that topic. That web page looks like this:

![webui](https://github.com/iandow/heroku-kafka-demo-java/blob/master/images/webui.png)

You'll need to [provision](#provisioning) the app.

## Provisioning with Heroku

First set your APP NAME

```
$ APP=my-app-name-$RANDOM
```

Install the kafka cli plugin:

```
$ heroku plugins:install heroku-kafka
```

Create a heroku app with Kafka attached:

```
$ heroku create $APP
$ heroku addons:create heroku-kafka:standard-0 --app $APP
$ heroku kafka:wait --app $APP
```

Create the sample topic, by default the topic will have 32 partitions.

```
$ heroku kafka:create messages --app $APP
```

Set an environment variable referencing the new topic:

```
$ heroku config:set KAFKA_TOPIC=messages
```

Deploy to Heroku and open the app:

```
$ git push heroku master
$ heroku open
```

## How to run this app without Heroku

If you already have a Kafka cluster, you can also run this app on your 
laptop. Here's how to do that:

Set the advertised.listener in server.properties on each Kafka broker:

```
advertised.listeners=PLAINTEXT://kafka1node:9092
```

Alias that in your local /etc/hosts

```
40.78.65.55 kafka1node
```

Set the following environment variables:

```
PORT=8080
KAFKA_URL=kafka://kafka1node:9092
KAFKA_TOPIC=mytest
```

On the Kafka server, create a topic like this:
```
/opt/kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 --partitions 1 --topic mytest
```

On your laptop, run the app like this:

Then open [http://localhost:8080](http://localhost:8080)


Now, to have a bit of fun, run the following command on the kafka server to add some messages to the topic you're monitoring in the webapp:

```
while true; do fortune | bin/kafka-console-producer.sh --broker-list localhost:9092 --topic mytest; done
```
