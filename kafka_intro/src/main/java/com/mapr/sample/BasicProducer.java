package com.mapr.sample;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Random;

public class BasicProducer {
    private static Random rand = new Random();
    private static long records_processed = 0L;

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);

        // configure the producer options

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // create a record to send


            // send the record

        }
    }
}
