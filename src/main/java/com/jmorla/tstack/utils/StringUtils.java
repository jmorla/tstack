package com.jmorla.tstack.utils;

public class StringUtils {

    public static boolean hasValue(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static String concat(String... args) {
        if (args == null) {
            throw new IllegalArgumentException("Arguments array cannot be null");
        }
        
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            if (arg != null) {
                result.append(arg);
            }
        }
        return result.toString();
    }
}