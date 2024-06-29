package org.example.nocopy;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

class City {
    private final byte[] data;
    private final int hashcode;

    private City(byte[] data, int hashcode) {
        this.data = data;
        this.hashcode = hashcode;
    }

    static City of(byte[] data, int hashcode) {
        return new City(data, hashcode);
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }

    @Override
    public String toString() {
        return new String(this.data, UTF_8);
    }

    boolean equals(City city) {
        return Arrays.mismatch(data, 0, data.length, city.data, 0, city.data.length) == -1;
    }

    public boolean equals(FileChannelReader fileChannelReader) {
        return Arrays.mismatch(data, 0, data.length, fileChannelReader.getData(), fileChannelReader.getLineStartPos(),
                fileChannelReader.getSemicolonPos()) == -1;
    }
}
