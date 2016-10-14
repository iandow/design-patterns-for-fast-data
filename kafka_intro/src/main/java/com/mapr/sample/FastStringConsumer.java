package com.mapr.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FastStringConsumer {

    public static KafkaConsumer consumer;

    static long records_consumed = 0L;

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);

        String topic = "pjug";
        System.out.println("Consuming from stream: " + topic);

        configureConsumer();

        List<String> topics = new ArrayList<String>();
        topics.add(topic);

        // Subscribe to the topics
        consumer.subscribe(topics);

        long pollTimeOut = 5000;  // milliseconds
        boolean printme = false;
        long last_update = 0;
        long start_time = System.nanoTime();
        double latency_total = 0;
        try {
            while (true) {
                // Request unread messages from the topic.
                ConsumerRecords<String, String> records = consumer.poll(pollTimeOut);
                long current_time = System.nanoTime();
                double elapsed_time = (current_time - start_time)/1e9;
                if (records.count() == 0) {
                    if (printme) {
                        System.out.println("No messages after " + pollTimeOut / 1000 + "s. Total msgs consumed = " + records_consumed + ". Duration =" + Math.round(elapsed_time) + "s. Average ingest rate = " + Math.round(records_consumed / elapsed_time / 1000) + "Kmsgs/s" + ". Average msg latency = " + latency_total/ records_consumed + "s");
                        printme = false;
                    }
                }
                if (records.count() > 0) {
                    if (printme == false) {
                        start_time = current_time;
                        last_update = 0;
                        latency_total = 0;
                        records_consumed = 0;
                        printme = true;
                    }
                    System.out.println("ConsumerRecords count =  " + records.count());
                    for (ConsumerRecord<String, String> record : records) {
                        records_consumed++;
                        current_time = System.nanoTime();
                        // Print performance stats once per second
                        if ((Math.floor(current_time - start_time)/1e9) > last_update)
                        {
                            last_update ++;

                            System.out.println("t = " + Math.round(elapsed_time) + ". Total msgs consumed = " + records_consumed + ". Average consume rate = " + Math.round(records_consumed / elapsed_time / 1000) + "Kmsgs/s");
                        }
                    }
                    consumer.commitSync();

                }

            }

        } catch (Throwable throwable) {
            System.err.printf("%s", throwable.getStackTrace());
        }

    }

    /* Set the value for configuration parameters.*/
    public static void configureConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers","ubuntu:9092");

        props.put("enable.auto.commit","false");
        props.put("group.id", "pjug");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
    }

}


