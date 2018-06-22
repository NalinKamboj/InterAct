package com.comakeit.inter_act;

import android.app.Application;

public class UserDetails extends Application{
    private static String userName;
    private static String userEmail;
    private static int userID;
    public static String ACCESS_TOKEN;
    private static String userPassword;
//    private static int userID;
//    protected static HashMap<String, Integer> employeesMap;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String name) {
        userName = name;
    }
    public boolean checkPassword(String word){
        return word.trim().equals(userPassword);
    }

    public static void setUserPassword(String word){
        userPassword = word.trim();
    }

    public UserDetails(){
        userID = -1;
        userName="";
        userEmail="";
        ACCESS_TOKEN = null;
    }

    public static String getUserEmail(){
        return userEmail;
    }

    public static void setUserEmail(String email) {
        userEmail = email;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int id) {
        userID = id;
    }

    public static String getUserPassword(){
        return userPassword;
    }
}
