package pl.fox.photosorter.utils;

import java.util.Arrays;
import java.util.Properties;

public class ArgParser {

    private static final String FLAG_PREFIX = "-D";

    public static Properties parseArgs(String[] args) {
        var properties = new Properties();
        Arrays.stream(args)
                .filter(arg -> arg.startsWith(FLAG_PREFIX))
                .map(arg -> arg.split("="))
                .filter(argArr -> argArr.length == 2)
                .forEach(arg -> {
                    var property = arg[0].substring(FLAG_PREFIX.length()).trim();
                    properties.put(property, arg[1]);
                });
        return properties;
    }

}
