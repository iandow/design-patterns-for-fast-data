package com.mapr.demo.finserv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Properties;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class PersonConsumer {
    public static void main(String[] args) throws Exception {

        String topic = "persons2";
        String group = "consumer group";
        Properties props = new Properties();
        props.put("bootstrap.servers", "ubuntu:9092");
        props.put("group.id", group);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<String, byte[]>(props);

        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Subscribed to topic " + topic);

        while (true) {
            ConsumerRecords<String, byte[]> records = consumer.poll(100);
            for (ConsumerRecord<String, byte[]> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());

                //Create object from bytes:
                ByteArrayInputStream bis = new ByteArrayInputStream(record.value());
                ObjectInput in = null;
                try {
                    in = new ObjectInputStream(bis);
                    Object obj = in.readObject();
                    System.out.println("Consumed object " + obj);
                    Person p2 = (Person)obj;
                    System.out.println("Consumed person " + p2.getName());

                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        // ignore close exception
                    }
                }
            }
        }
    }
}