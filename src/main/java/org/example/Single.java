package org.example;

import org.example.nostring.NoStringAggregator;

import java.io.IOException;

public class Single {
    private static final String BILLION_FILE_PATH = "H:/billion_measurements.txt";
    private static final String MILLION_FILE_PATH = "H:/million_measurements.txt";

    public static void main(String[] args) throws IOException {
        Aggregator aggregator = new NoStringAggregator();
        aggregator.calculate(BILLION_FILE_PATH);
    }
}
