package org.example.utils;

import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.example.utils.ParseDouble.parseIntegerFixed;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParseDoubleTest {

    @Test
    void testParseIntegerFixed() {
        assertEquals(10, parseIntegerFixed("1.0".getBytes(UTF_8)));
        assertEquals(-10, parseIntegerFixed("-1.0".getBytes(UTF_8)));
        assertEquals(95, parseIntegerFixed("9.5".getBytes(UTF_8)));
        assertEquals(-95, parseIntegerFixed("-9.5".getBytes(UTF_8)));
        assertEquals(257, parseIntegerFixed("Ségou;25.7".getBytes(UTF_8), 6, 10));
    }
}
