package io.github.pizzaserver.api.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTests {

    @Test
    public void failsParsing() {
        assertThrows(IllegalArgumentException.class, () -> Version.fromString("-1.-1"));
        assertThrows(IllegalArgumentException.class, () -> Version.fromString("0.-1"));
        assertThrows(IllegalArgumentException.class, () -> Version.fromString("-1.0"));
        assertThrows(IllegalArgumentException.class, () -> Version.fromString("0.0.-1"));
        assertThrows(VersionParseException.class, () -> Version.fromString("0 .0.0"));
        assertThrows(VersionParseException.class, () -> Version.fromString(" 0.0.0"));
        assertThrows(VersionParseException.class, () -> Version.fromString("0.0."));
        assertThrows(VersionParseException.class, () -> Version.fromString("a"));
        assertThrows(VersionParseException.class, () -> Version.fromString("a.b"));
    }

    @Test
    public void versionParsing() {
        assertEquals(Version.fromString("0.0"), new Version(0, 0));
        assertEquals(Version.fromString("1.18"), new Version(1, 18));
        assertEquals(Version.fromString("0.0.0"), new Version(0, 0, 0));
        assertEquals(Version.fromString("1.18.2"), new Version(1, 18, 2));

        assertEquals("0.0.0", new Version(0, 0).toString());
        assertEquals("1.18.0", new Version(1, 18).toString());
        assertEquals("0.0.0", new Version(0, 0, 0).toString());
        assertEquals("1.18.2", new Version(1, 18, 2).toString());
    }

    @Test
    public void constructorParamOrder() {
        Version a = new Version(1, 2, 3);
        assertEquals(a.getMajor(), 1);
        assertEquals(a.getMinor(), 2);
        assertEquals(a.getPatch(), 3);

        Version b = new Version(1, 2);
        assertEquals(b.getMajor(), 1);
        assertEquals(b.getMinor(), 2);
        assertEquals(b.getPatch(), 0);
    }

    @Test
    public void equals() {
        assertNotEquals(new Version(1, 2, 3), new Version(0, 0, 0));
        assertNotEquals(new Version(1, 2, 3), new Version(1, 0, 0));
        assertNotEquals(new Version(1, 2, 3), new Version(1, 2, 0));
        assertEquals(new Version(1, 2, 3), new Version(1, 2, 3));
    }

}
