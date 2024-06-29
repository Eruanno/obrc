package org.example.nocopy;

class TemperatureAggregator {
    private final City city;
    private int count, min, max, sum;

    TemperatureAggregator(City city, int temperature) {
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

    City getCity() {
        return this.city;
    }

    public boolean equals(final FileChannelReader fileChannelReader) {
        return this.city.equals(fileChannelReader);
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
