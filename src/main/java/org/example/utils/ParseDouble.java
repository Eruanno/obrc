package org.example.utils;

/**
 * This is a small helper class for parsing char sequences and converting them into int, long, and double. This implementation is optimized for
 * speed not functionality. It is only able to parse plain numbers with base 10, e.g. 100828171. In case of parsing problems it will fall
 * back to the JDK but will lose the speed advantage of course.
 *
 * @author René Schwietzke
 */
public final class ParseDouble {
    private static final int DIGITOFFSET = 48;

    public static int parseIntegerFixed(final byte[] data) {
        final int offset = 0;
        final int end = data.length - 1;
        return parseIntegerFixed(data, offset, end);
    }

    /**
     * Parses a double but ends up with an int, only because we know
     * the format of the results -99.9 to 99.9
     */
    public static int parseIntegerFixed(final byte[] data, final int offset, final int end) {
        final int length = end - offset; // one is missing, we care for that later

        // we know the first three pieces already 9.9
        int p0 = data[end];
        int p1 = multiplyBy10(data[end - 2]);
        int value = p0 + p1 - (DIGITOFFSET + DIGITOFFSET * 10);

        // we are 9.9
        if (length == 2) {
            return value;
        }

        // ok, we are either -9.9 or 99.9 or -99.9
        if (data[offset] != (byte) '-') {
            // we are 99.9
            value += data[end - 3] * 100 - DIGITOFFSET * 100;
            return value;
        }

        // we are either -99.9 or -9.9
        if (length == 3) {
            // -9.9
            return -value;
        }

        // -99.9
        value += data[end - 3] * 100 - DIGITOFFSET * 100;
        return -value;
    }

    private static int multiplyBy10(byte value) {
        int intValue = value & 0xFF;
        return (intValue << 3) + (intValue << 1);
    }
}