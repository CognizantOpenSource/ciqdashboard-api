package com.cognizant.idashboardapi.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenUtil {
    private static final String regex = "(?s)[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private TokenUtil(){}

    public static String getUUIDStringFromToken(String token){
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(token);

        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
