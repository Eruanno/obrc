package org.example;

import org.example.baseline.BaselineAggregator;
import org.example.channel.ChannelAggregator;
import org.example.fastset.FastSetAggregator;
import org.example.getandput.GetAndPutAggregator;
import org.example.indexof.IndexOfAggregator;
import org.example.minmax.MinMaxAggregator;
import org.example.nocopy.NoCopyAggregator;
import org.example.nostring.NoStringAggregator;
import org.example.useint.UseIntAggregator;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String BILLION_FILE_PATH = "H:/billion_measurements.txt";
    private static final String MILLION_FILE_PATH = "H:/million_measurements.txt";

    public static void main(String[] args) {
        List<Aggregator> aggregators = List.of(new BaselineAggregator(), new IndexOfAggregator(),
                new GetAndPutAggregator(), new MinMaxAggregator(), new UseIntAggregator(), new FastSetAggregator(),
                new ChannelAggregator(), new NoStringAggregator(), new NoCopyAggregator());
        List<Result> results = aggregators.stream()
                                          .map(aggregator -> benchmark(aggregator, BILLION_FILE_PATH))
                                          .toList();
        print(results);
    }

    private static Result benchmark(Aggregator aggregator, String absoluteFilePath) {
        String benchmarkName = aggregator.getClass().getSimpleName();
        System.out.printf("Benchmark %s started.\n", benchmarkName);
        long startTime = System.nanoTime();
        try {
            aggregator.calculate(absoluteFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.printf("Benchmark %s ended.\n", benchmarkName);
        return new Result(benchmarkName, duration);
    }

    private static void print(List<Result> results) {
        System.out.printf("%-20s\t%-10s\t%-10s\n", "Class name", "time [ms]", "Improvement");
        long max = results.stream().mapToLong(Result::duration).max().orElse(-1);
        results.forEach(result -> print(result, max));
    }

    private static void print(Result result, long max) {
        double percent = ((double) result.duration() / (double) max) * 100;
        System.out.printf("%-20s\t%-10d\t%-10.2f%%\n", result.name, result.duration(), percent);
    }

    private record Result(String name, long duration) {
    }
}
