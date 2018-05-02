package com.andiag.core.util;

public class StringUtils {

    public static String toTitleCase(String s) {
        if (s != null) {
            final String actionableDelimiters = " '-/";
            StringBuilder sb = new StringBuilder();
            boolean capNext = true;
            for (char c : s.toCharArray()) {
                c = (capNext) ? Character.toUpperCase(c) : Character.toLowerCase(c);
                sb.append(c);
                capNext = (actionableDelimiters.indexOf(c) >= 0);
            }
            return sb.toString();
        }
        return null;
    }

}
