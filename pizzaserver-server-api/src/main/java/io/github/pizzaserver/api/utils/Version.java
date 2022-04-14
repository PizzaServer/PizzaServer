package io.github.pizzaserver.api.utils;

import io.github.pizzaserver.commons.utils.Check;

/**
 * Version number akin to semantic versioning.
 * The minecraft client only supports 16 bit version numbers.
 */
public class Version {

    // nonnegative integers
    private short major;
    private short minor;
    private short patch;

    /**
     * Parses a version number string.
     * Semantic versioning prerelease strings (eg. '+example' suffix) are not supported.
     *
     * @param s Version string input
     */
    public static Version fromString(String s) {
        short major = 0;
        short minor = 0;
        short patch = 0;

        int nextDot = s.indexOf('.');
        if (nextDot < 1) {
            throw new VersionParseException("Version string is missing first separator dot");
        } else {
            String majorString = s.substring(0, nextDot);
            try {
                major = Short.parseShort(majorString);
                s = s.substring(nextDot + 1);
            } catch (NumberFormatException e) {
                throw new VersionParseException("Failed to parse major version number '" + majorString + "'", e);
            }
        }
        nextDot = s.indexOf('.');
        if (nextDot < 1) {
            // only minor version is given, patch number is zero

            try {
                minor = Short.parseShort(s);
            } catch (NumberFormatException e) {
                throw new VersionParseException("Failed to parse minor version number '" + s + "'", e);
            }
        } else {
            String minorString = s.substring(0, nextDot);
            try {
                minor = Short.parseShort(minorString);
                s = s.substring(nextDot + 1);
            } catch (NumberFormatException e) {
                throw new VersionParseException("Failed to parse minor version number '" + minorString + "'", e);
            }

            try {
                patch = Short.parseShort(s);
            } catch (NumberFormatException e) {
                throw new VersionParseException("Failed to parse patch version number '" + s + "'", e);
            }
        }

        return new Version(major, minor, patch);
    }

    public Version(int major, int minor, int patch) {
        Check.checkArgument(major >= 0, "Major version number must not be negative");
        Check.checkArgument(minor >= 0, "Minor version number must not be negative");
        Check.checkArgument(patch >= 0, "Patch version number must not be negative");

        Check.checkArgument(major <= Short.MAX_VALUE, "Major version number overflows 16 bit signed value");
        Check.checkArgument(minor <= Short.MAX_VALUE, "Minor version number overflows 16 bit signed value");
        Check.checkArgument(patch <= Short.MAX_VALUE, "Patch version number overflows 16 bit signed value");

        this.major = (short) major;
        this.minor = (short) minor;
        this.patch = (short) patch;
    }

    /**
     * Creates a version with patch number 0.
     *
     * @param major Major version number
     * @param minor Minor version number
     */
    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    public short getMajor() {
        return this.major;
    }

    public short getMinor() {
        return this.minor;
    }

    public short getPatch() {
        return this.patch;
    }

    /**
     * Exact comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version other) {
            return other.major == this.major && other.minor == this.minor && other.patch == this.patch;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }
}


