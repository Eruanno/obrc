package org.example.channel;

import org.example.Aggregator;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;

import static org.example.utils.ParseDouble.parseIntegerFixed;

public class ChannelAggregator implements Aggregator {

    @Override
    public String calculate(String absoluteFilePath) throws IOException {
        FastHashSet cities = new FastHashSet(2048, 0.5f);

        try (var raf = new RandomAccessFile(absoluteFilePath, "r"); var channel = raf.getChannel()) {
            final FileChannelReader fileChannelReader = new FileChannelReader(channel);
            while (true) {
                fileChannelReader.readNextLine();
                if (fileChannelReader.isHasNewLine()) {
                    String city = new String(fileChannelReader.getCity());
                    int temperature = parseIntegerFixed(fileChannelReader.getTemperature());
                    cities.put(city, temperature);
                } else if (fileChannelReader.isEOF()) {
                    break;
                }
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
        return 7;
    }
}
