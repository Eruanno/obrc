package org.example.fastset;

class TemperatureAggregator {
    private final String city;
    private int count, min, max, sum;

    TemperatureAggregator(String city, int temperature) {
        this.city = city;
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

    String getCity() {
        return this.city;
    }

    @Override
    public int hashCode() {
        return city.hashCode();
    }

    @Override
    public String toString() {
        return round(this.min) + "/" + round(((double) this.sum / (double) this.count)) + "/" + round(this.max);
    }

    private double round(double value) {
        return Math.round(value) / 10.0;
    }
}
