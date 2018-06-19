package com.comakeit.inter_act;

import android.app.Application;

import java.util.HashMap;

public class UserDetails extends Application{
    protected static String ACCESS_TOKEN;
    protected static String userID;
    protected static HashMap<String, Integer> employees;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserDetails.userName = userName;
    }

    private static String userName;

    public UserDetails(){
        userID = null;
        ACCESS_TOKEN = null;
        employees = null;
    }

    public String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }

    public void setACCESS_TOKEN(String ACCESS_TOKEN) {
        ACCESS_TOKEN = ACCESS_TOKEN;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        userID = userID;
    }
}
