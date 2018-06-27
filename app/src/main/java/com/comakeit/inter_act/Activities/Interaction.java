package com.comakeit.inter_act.Activities;

import android.content.Context;

import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.sql.DatabaseHelper;

import java.util.Calendar;

public class Interaction {
    private String fromUserEmail, toUserEmail, eventName, IAType, message;
    int interactionID;
    private boolean isAnonymous;
    private Calendar eventCalendar, IACalendar;
    protected UserDetails mUserDetails;

    Interaction(Context context){
        /*TODO Obtain username, hardcoding username for now. PRIORITY: HIGHEST
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccounts();
        List<String> possibleEmails = new LinkedList<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        for(Account account: accounts){
            if(emailPattern.matcher(account.name).matches()){
                possibleEmails.add(account.name);
                Log.i("Report EMAIL: ",account.name);
            }
        }
        if(!possibleEmails.isEmpty() && possibleEmails.get(0)!=null){
            fromUser = possibleEmails.get(0);
            IACalendar = Calendar.getInstance();
            interactionID = fromUser + IACalendar.get(Calendar.DAY_OF_MONTH)+IACalendar.get(Calendar.MONTH) + IACalendar.get(Calendar.YEAR) + IACalendar.getTime();
            Log.i("Reporting Report ID: ", interactionID);
        }else{
            Toast.makeText(context, "USERNAME NOT FOUND", Toast.LENGTH_SHORT).show();
            fromUser = "";
        }
        */
        this.interactionID = -1;
        this.fromUserEmail = UserDetails.getUserEmail();
        String[] parts = fromUserEmail.split("@");
        this.IACalendar = Calendar.getInstance();
//        Log.i("Reporting Report ID: ", interactionID);
        this.toUserEmail = "";
        this.eventName = "";
        this.IAType = "";
        this.message = "";
        this.isAnonymous = false;
        this.eventCalendar = null;
    }

    public boolean validateReport(Interaction report, Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        if(databaseHelper.checkUser(report.toUserEmail.toUpperCase()))
            return !(report.eventName.equals("") || report.IAType.equals("") || report.message.equals("") || eventCalendar == null);
        else
            return false;
    }

    public void publishReport(Context context, Interaction report){
        //validating report again
        if(!report.validateReport(report, context))
            return;
    }

    /* All getters and setters */
    public String getToUser() {
        return toUserEmail;
    }

    public void setToUser(String toUser) {
        this.toUserEmail = toUser;
    }

    public void setInteractionID(int ID) {
        this.interactionID = ID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getIAType() {
        return IAType;
    }

    public void setIAType(String IAType) {
        this.IAType = IAType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Calendar getEventCalendar() {
        return eventCalendar;
    }

    public void setEventCalendar(Calendar eventCalendar) {
        this.eventCalendar = eventCalendar;
    }

    public Calendar getIACalendar() {
        return IACalendar;
    }

    public String getFromUserEmail(){
        return this.fromUserEmail;
    }

    public void setIACalendar(Calendar IACalendar) {
        this.IACalendar = IACalendar;
    }
}
