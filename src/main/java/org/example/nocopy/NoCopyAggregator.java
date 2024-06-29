package org.example.nocopy;

import org.example.Aggregator;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;

import static org.example.utils.ParseDouble.parseIntegerFixed;

public class NoCopyAggregator implements Aggregator {

    @Override
    public String calculate(String absoluteFilePath) throws IOException {
        FastHashSet cities = new FastHashSet(2048, 0.5f);

        try (var raf = new RandomAccessFile(absoluteFilePath, "r"); var channel = raf.getChannel()) {
            final FileChannelReader fileChannelReader = new FileChannelReader(channel);
            while (true) {
                fileChannelReader.readNextLine();
                if (fileChannelReader.isHasNewLine()) {
                    int temperature = parseIntegerFixed(fileChannelReader.getData(),
                            fileChannelReader.getSemicolonPos() + 1, fileChannelReader.getNewlinePos() - 1);
                    cities.put(fileChannelReader, temperature);
                } else if (fileChannelReader.isEOF()) {
                    break;
                }
            }
        }

        final var result = new TreeMap<String, TemperatureAggregator>();
        for (TemperatureAggregator measurementsAggregator : cities.keys()) {
            result.put(measurementsAggregator.getCity().toString(), measurementsAggregator);
        }
        return result.toString();
    }

    @Override
    public int order() {
        return 9;
    }
}
