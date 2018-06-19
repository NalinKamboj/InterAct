package com.comakeit.inter_act;

import android.util.Log;

public class UserDetails {
    private static UserDetails sUserDetails = null;
    private String ACCESS_TOKEN;
    private String userID;

    private UserDetails(){
        if(sUserDetails!=null)
            Log.i("USER DETAILS CONSTR: ", "Instance already exists");
    }

    public static UserDetails getUserDetails(){
        if(sUserDetails == null){
            sUserDetails = new UserDetails();
        }
        return sUserDetails;
    }

    public static void setToken(String token){
        sUserDetails.ACCESS_TOKEN = token;
    }

    public static void setUserID(String id){
        sUserDetails.userID = id;
    }
}
