package com.mapr.sample;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class BasicProducer {

    public static KafkaProducer producer;
    static String topic = "pjug";
    static long records_processed = 0L;

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);
        configureProducer();

        Random rand = new Random();

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String value = Integer.toString(rand.nextInt(100));
            String key = Long.toString(System.nanoTime());
            ProducerRecord rec = new ProducerRecord(topic,key,value);
            producer.send(rec,
                    new Callback() {
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            long current_time = System.nanoTime();
                            records_processed++;
//                            System.out.printf("\tkey = '%s'\n" +
//                                            "\tvalue = %s\n" +
//                                            "\ttopic = %s\n" +
//                                            "\tpartition = %d\n" +
//                                            "\toffset = %d\n",
//                                    key,
//                                    value,
//                                    metadata.topic(),
//                                    metadata.partition(),
//                                    metadata.offset());
                            System.out.println("Total records published : " + records_processed);
                        }
                    });

        }




    }

    public static void configureProducer() {
        Properties props = new Properties();
        try {
            props.load(Resources.getResource("producer.props").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(props);
    }
}
