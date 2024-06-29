package org.example.useint;

import org.example.Aggregator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.example.utils.ParseDouble.parseIntegerFixed;

public class UseIntAggregator implements Aggregator {

    @Override
    public String calculate(String absoluteFilePath) throws IOException {
        Map<String, TemperatureAggregator> stationTemperatures = new HashMap<>();

        try (BufferedReader buffReader = new BufferedReader(new FileReader(absoluteFilePath))) {
            while (buffReader.ready()) {
                String line = buffReader.readLine();
                int index = line.indexOf(';');
                String city = line.substring(0, index);
                int temperature = parseIntegerFixed(line.substring(index + 1).getBytes(UTF_8));
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
        return 5;
    }
}
