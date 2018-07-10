package com.comakeit.inter_act;

public class Utilities {

    /** Utility function for converting any string to Camel Case. Example - "tHis is A sTrING"  becomes "This Is A String"
     * @param init String to be converted to Camel Case
     * @return String in Camel Case
     */
    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }
}
