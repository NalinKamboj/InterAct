package com.comakeit.inter_act;

import android.app.Application;

public class UserDetails extends Application{
    private String userName;
    private String userEmail;
    private int userID;
    public static String ACCESS_TOKEN;
    private String userPassword;
//    private static int userID;
//    protected static HashMap<String, Integer> employeesMap;

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean checkPassword(String word){
        return word.trim().equals(userPassword);
    }

    public void setUserPassword(String word){
        userPassword = word.trim();
    }

    public UserDetails(){
        userID = -1;
        userName="";
        userEmail="";
        ACCESS_TOKEN = null;
//        employeesMap = new HashMap<String, Integer>();
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserPassword(){
        return userPassword;
    }
}
