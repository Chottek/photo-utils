package pl.fox.photosorter.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArgParserTest {

    private static final String ARG_NAME = "chicken";
    private static final String ARG_VALUE = "butter_chicken";

    private static final String[] ARGS_VALID = { "-D%s=%s".formatted(ARG_NAME, ARG_VALUE) };
    private static final String[] ARGS_INVALID_NO_PREFIX = { "%s=%s".formatted(ARG_NAME, ARG_VALUE) };
    private static final String[] ARGS_INVALID_PREFIX = { "_S%s=%s".formatted(ARG_NAME, ARG_VALUE) };
    private static final String[] ARGS_INVALID_PAIR_SPLIT = { "-D%s:%s".formatted(ARG_NAME, ARG_VALUE) };

    @Test
    void argsAreParsedSuccessfullyOnValidInput() {
        var properties = ArgParser.parseArgs(ARGS_VALID);
        assertEquals(properties.getProperty(ARG_NAME), ARG_VALUE);
    }

    @Test
    void argsAreNotParsedOnNoFlagPrefix() {
        var properties = ArgParser.parseArgs(ARGS_INVALID_NO_PREFIX);
        assertNull(properties.getProperty(ARG_NAME));
    }

    @Test
    void argsAreNotParsedOnInvalidFlagPrefix() {
        var properties = ArgParser.parseArgs(ARGS_INVALID_PREFIX);
        assertNull(properties.getProperty(ARG_NAME));
    }

    @Test
    void argsAreNotParsedOnInvalidPairSplit() {
        var properties = ArgParser.parseArgs(ARGS_INVALID_PAIR_SPLIT);
        assertNull(properties.getProperty(ARG_NAME));
    }

}
