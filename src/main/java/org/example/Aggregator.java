package org.example;

import java.io.IOException;

public interface Aggregator {
    String calculate(String absoluteFilePath) throws IOException;

    int order();
}
