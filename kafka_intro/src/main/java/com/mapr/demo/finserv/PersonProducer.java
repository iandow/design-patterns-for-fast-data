package com.mapr.demo.finserv;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/************
 PURPOSE:

 See how to serialize POJOs.

 USAGE:

 1. compile:
    mvn package -Dmaven.test.skip=true

 2. run the consumer
    java -cp target/:target/kafka-study-1.0-jar-with-dependencies.jar com.mapr.demo.finserv.PersonConsumer

 3. run a native producer to see the streamed bytes
    bin/kafka-console-consumer.sh --zookeeper ubuntu:2181 --topic persons --from-beginning

 4. run the producer
    java -cp target/:target/kafka-study-1.0-jar-with-dependencies.jar com.mapr.demo.finserv.PersonProducer

 **********/

public class PersonProducer {

    public static KafkaProducer producer;

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);

        String topic = "persons2";
        System.out.println("Publishing to topic: " + topic);

        configureProducer();

        BufferedReader br = null;

        try {
            // generate data
            Person person1 = new Person();
            person1.setName("Ian");
            person1.setAddress("123 Main St");
            person1.setAge(34);
            List<String> hobbies = new LinkedList<>();
            hobbies.add("ski");
            hobbies.add("boat");
            hobbies.add("fly");
            person1.setHobbies(hobbies);

            //Prepare bytes to send:
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(person1);
            out.flush();
            byte[] value = bos.toByteArray();

            String key = Long.toString(System.nanoTime());
            ProducerRecord rec = new ProducerRecord(topic,key,value);
            producer.send(rec,
                    new Callback() {
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            long current_time = System.nanoTime();

                            System.out.print(".");

                            System.out.printf("\tSent: '%s'\n" +
                                            "\t\tdelay = %.2f\n" +
                                            "\t\ttopic = %s\n" +
                                            "\t\tpartition = %d\n" +
                                            "\t\toffset = %d\n",
                                    value,
                                    (current_time - Long.valueOf(key))/1e9,
                                    metadata.topic(),
                                    metadata.partition(), metadata.offset());
                        }
                    });
            producer.flush();
            producer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* Set the value for a configuration parameter.
     This configuration parameter specifies which class
     to use to serialize the value of each message.*/
    public static void configureProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers","ubuntu:9092");
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.ByteArraySerializer");

        producer = new KafkaProducer<String, byte[]>(props);
    }
}
