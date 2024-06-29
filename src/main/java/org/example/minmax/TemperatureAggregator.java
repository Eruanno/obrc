package org.example.minmax;

import java.util.Locale;

class TemperatureAggregator {
    private double min, max, sum;
    private int count;

    TemperatureAggregator(double temperature) {
        this.min = this.max = this.sum = temperature;
        this.count = 1;
    }

    void add(double temperature) {
        if (temperature < this.min) {
            this.min = temperature;
        } else if (temperature > this.max) {
            this.max = temperature;
        }
        this.sum += temperature;
        this.count += 1;
    }

    @Override
    public String toString() {
        double average = this.sum / this.count;
        return String.format(Locale.US, "%.1f/%.1f/%.1f", this.min, average, this.max);
    }
}
