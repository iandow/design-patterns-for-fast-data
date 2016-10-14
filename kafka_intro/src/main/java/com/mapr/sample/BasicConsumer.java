package com.mapr.sample;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BasicConsumer {

    private static KafkaConsumer consumer;
    private static List<String> topics = Arrays.asList("pjug");

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);

        System.out.println("Consuming from stream: " + topics);
        // Specify how to convert serialized bytes for keys and values and kafka server endpoint
        configureConsumer();
        // Subscribe to one or more topics (pattern matching okay)
        consumer.subscribe(topics);
        while (true) {
            // wait up to 1000ms for messages to arrive
            ConsumerRecords<String, String> records = consumer.poll(1000);
            if (records.count() > 0) {
                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("\tconsumed: '%s'\n" +
                                    "\t\ttopic = %s\n" +
                                    "\t\tpartition = %d\n" +
                                    "\t\tkey = %s\n" +
                                    "\t\toffset = %d\n",
                            record.value(),
                            record.topic(),
                            record.partition(),
                            record.key(),
                            record.offset());
            }
        }
    }

    public static void configureConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers","ubuntu:9092");
        props.put("client.id", "blahblah");
        props.put("group.id", "groupblahblah");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
    }

}


