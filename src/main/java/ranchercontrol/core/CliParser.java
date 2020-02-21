package ranchercontrol.core;

import bobthebuildtool.pojos.error.InvalidInput;

import java.util.Map;

import static bobthebuildtool.services.Functions.isNullOrEmpty;

public enum CliParser {;

    public static String orDefault(final String value, final String defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String getMandatoryString(final Map<String, String> map, final String name)
            throws InvalidInput {
        final String value = map.get(name);
        if (isNullOrEmpty(value)) throw new InvalidInput("Variable " + name + " must be set");
        return value;
    }

}
