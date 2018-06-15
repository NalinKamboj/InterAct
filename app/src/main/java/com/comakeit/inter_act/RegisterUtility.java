package com.comakeit.inter_act;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUtility {
    private static Pattern sPattern;
    private static Matcher sMatcher;

    //Email RegEx
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Validate email with Regular Expression
     *
     * @param email
     * @return true for Valid email and false for Invalid email
     */
    public static boolean validate(String email){
        sPattern = Pattern.compile(EMAIL_REGEX);
        sMatcher = sPattern.matcher(email);
        return sMatcher.matches();
    }

    /**
     * Check for NULL String Object
     * @param txt
     * @return
     */

    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length() > 0 ? true : false;
    }

}
