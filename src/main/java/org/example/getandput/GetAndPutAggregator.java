package org.example.getandput;

import org.example.Aggregator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GetAndPutAggregator implements Aggregator {

    @Override
    public String calculate(String absoluteFilePath) throws IOException {
        Map<String, TemperatureAggregator> stationTemperatures = new HashMap<>();

        try (BufferedReader buffReader = new BufferedReader(new FileReader(absoluteFilePath))) {
            while (buffReader.ready()) {
                String line = buffReader.readLine();
                int index = line.indexOf(';');
                String city = line.substring(0, index);
                double temperature = Double.parseDouble(line.substring(index + 1));
                TemperatureAggregator measurements = stationTemperatures.get(city);
                if (measurements != null) {
                    measurements.add(temperature);
                } else {
                    stationTemperatures.put(city, new TemperatureAggregator(temperature));
                }
            }
        }

        return new TreeMap<>(stationTemperatures).toString();
    }

    @Override
    public int order() {
        return 3;
    }
}
