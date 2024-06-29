package org.example.fastset;

import org.example.Aggregator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.example.utils.ParseDouble.parseIntegerFixed;

public class FastSetAggregator implements Aggregator {

    @Override
    public String calculate(String absoluteFilePath) throws IOException {
        FastHashSet cities = new FastHashSet(2048, 0.5f);

        try (BufferedReader buffReader = new BufferedReader(new FileReader(absoluteFilePath))) {
            while (buffReader.ready()) {
                String line = buffReader.readLine();
                int index = line.indexOf(';');
                String city = line.substring(0, index);
                int temperature = parseIntegerFixed(line.substring(index + 1).getBytes(UTF_8));
                cities.put(city, temperature);
            }
        }

        final var result = new TreeMap<String, TemperatureAggregator>();
        for (TemperatureAggregator measurementsAggregator : cities.keys()) {
            result.put(measurementsAggregator.getCity(), measurementsAggregator);
        }
        return result.toString();
    }

    @Override
    public int order() {
        return 6;
    }
}
