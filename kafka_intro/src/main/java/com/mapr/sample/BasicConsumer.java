package com.mapr.sample;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.io.IOException;


public class BasicConsumer {

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.OFF);

        // configure consumer options

        // Subscribe to one or more topics (pattern matching okay)

        // poll for messages

        // iterate through messages
    }

}


