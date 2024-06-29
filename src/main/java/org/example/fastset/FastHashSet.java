package org.example.fastset;

import java.util.ArrayList;
import java.util.List;

class FastHashSet {
    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float m_fillFactor;
    /**
     * Keys and values
     */
    private TemperatureAggregator[] m_data;
    /**
     * We will resize a map once it reaches this size
     */
    private int m_threshold;
    /**
     * Current map size
     */
    private int m_size;
    /**
     * Mask to calculate the original position
     */
    private int m_mask;

    public FastHashSet(final int size, final float fillFactor) {
        final int capacity = arraySize(size, fillFactor);
        m_mask = capacity - 1;
        m_fillFactor = fillFactor;

        m_data = new TemperatureAggregator[capacity];
        m_threshold = (int) (capacity * fillFactor);
    }

    /**
     * Returns the least power of two smaller than or equal to 2<sup>30</sup> and larger than or equal to <code>Math.ceil( expected / f )</code>.
     *
     * @param expected the expected number of elements in a hash table.
     * @param f        the load factor.
     * @return the minimum possible size for a backing array.
     * @throws IllegalArgumentException if the necessary size is larger than 2<sup>30</sup>.
     */
    private static int arraySize(final int expected, final float f) {
        final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / f)));
        if (s > (1 << 30))
            throw new IllegalArgumentException(
                    "Too large (" + expected + " expected elements with load factor " + f + ")");
        return (int) s;
    }

    /**
     * Return the least power of two greater than or equal to the specified value.
     *
     * <p>Note that this function will return 1 when the argument is 0.
     *
     * @param x a long integer smaller than or equal to 2<sup>62</sup>.
     * @return the least power of two greater than or equal to the specified value.
     */
    private static long nextPowerOfTwo(long x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }

    public void put(final String city, int value) {
        int ptr = city.hashCode() & m_mask;
        TemperatureAggregator key = m_data[ptr];

        if (key == null) {
            put(new TemperatureAggregator(city, value));
            return;
        }
        if (key.getCity().equals(city)) {
            key.add(value);
            return;
        }

        while (true) {
            ptr = (ptr + 1) & m_mask;
            key = m_data[ptr];
            if (key == null) {
                put(new TemperatureAggregator(city, value));
                return;
            }
            if (key.getCity().equals(city)) {
                key.add(value);
                return;
            }
        }
    }

    private void put(TemperatureAggregator key) {
        int ptr = key.hashCode() & m_mask;
        TemperatureAggregator k = m_data[ptr];

        if (tryPutIn(key, ptr, k)) return;

        while (true) {
            ptr = (ptr + 1) & m_mask;
            k = m_data[ptr];
            if (tryPutIn(key, ptr, k)) return;
        }
    }

    private boolean tryPutIn(TemperatureAggregator key, int ptr, TemperatureAggregator k) {
        if (k == null) {
            m_data[ptr] = key;
            if (m_size >= m_threshold) rehash(m_data.length * 2);
            else ++m_size;
            return true;
        } else if (k.hashCode() == key.hashCode() && k.getCity().equals(key.getCity())) {
            m_data[ptr] = key;
            return true;
        }
        return false;
    }

    private void rehash(final int newcapacity) {
        m_threshold = (int) (newcapacity * m_fillFactor);
        m_mask = newcapacity - 1;

        final int oldcapacity = m_data.length;
        final TemperatureAggregator[] oldData = m_data;

        m_data = new TemperatureAggregator[newcapacity];

        m_size = 0;

        for (int i = 0; i < oldcapacity; i++) {
            final TemperatureAggregator oldKey = oldData[i];
            if (oldKey != null) {
                put(oldKey);
            }
        }
    }

    public List<TemperatureAggregator> keys() {
        final List<TemperatureAggregator> result = new ArrayList<>(this.m_size);

        for (final TemperatureAggregator o : m_data) {
            if (o != null) {
                result.add(o);
            }
        }
        return result;
    }
}
