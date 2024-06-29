package org.example.baseline;

import org.example.Aggregator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BaselineAggregator implements Aggregator {

    @Override
    public String calculate(String absoluteFilePath) throws IOException {
        Map<String, TemperatureAggregator> stationTemperatures = new HashMap<>();

        try (BufferedReader buffReader = new BufferedReader(new FileReader(absoluteFilePath))) {
            while (buffReader.ready()) {
                String line = buffReader.readLine();
                String[] tokens = line.split(";");
                String city = tokens[0];
                double temperature = Double.parseDouble(tokens[1]);
                stationTemperatures.computeIfAbsent(city, k -> new TemperatureAggregator()).add(temperature);
            }
        }

        return new TreeMap<>(stationTemperatures).toString();
    }

    @Override
    public int order() {
        return 1;
    }
}
