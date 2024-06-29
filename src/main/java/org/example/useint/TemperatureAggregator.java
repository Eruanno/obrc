package org.example.useint;

class TemperatureAggregator {
    private int count, min, max, sum;

    TemperatureAggregator(int temperature) {
        this.min = this.max = this.sum = temperature;
        this.count = 1;
    }

    void add(int temperature) {
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
        return round(this.min) + "/" + round(((double) this.sum / (double) this.count)) + "/" + round(this.max);
    }

    private double round(double value) {
        return Math.round(value) / 10.0;
    }
}
