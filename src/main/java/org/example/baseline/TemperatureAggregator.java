package org.example.baseline;

import java.util.Locale;

class TemperatureAggregator {
    private double min, max, sum;
    private int count;

    TemperatureAggregator() {
    }

    void add(double temperature) {
        if (this.count == 0) {
            this.min = this.max = this.sum = temperature;
            this.count = 1;
        } else {
            this.min = Math.min(this.min, temperature);
            this.max = Math.max(this.max, temperature);
            this.sum += temperature;
            this.count += 1;
        }
    }

    @Override
    public String toString() {
        double average = this.sum / this.count;
        return String.format(Locale.US, "%.1f/%.1f/%.1f", this.min, average, this.max);
    }
}
