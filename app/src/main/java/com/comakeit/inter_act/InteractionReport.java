package com.comakeit.inter_act;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

public class InteractionReport {
    private String fromUser, toUser, interactionID, eventName, IAType, message;
    private boolean isAnonymous;
    private Calendar eventCalendar, IACalendar;

    InteractionReport(Context context){
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

        this.fromUser = "nalin.1997@gmail.com";
        String[] parts = fromUser.split("@");
        this.IACalendar = Calendar.getInstance();
        this.interactionID = parts[0] + IACalendar.get(Calendar.DAY_OF_MONTH)+IACalendar.get(Calendar.MONTH) + IACalendar.get(Calendar.YEAR) + IACalendar.get(Calendar.HOUR)
                + IACalendar.get(Calendar.MINUTE) + IACalendar.get(Calendar.SECOND);
        Log.i("Reporting Report ID: ", interactionID);
        this.toUser = "";
        this.eventName = "";
        this.IAType = "";
        this.message = "";
        this.isAnonymous = false;
        this.eventCalendar = null;
    }

    public boolean validateReport(InteractionReport report){
        return !(report.toUser.equals("") || report.eventName.equals("") || report.IAType.equals("") || report.message.equals("") || eventCalendar == null);
    }

    public void sendReport(Context context, InteractionReport report){
        //validating report again
        if(!report.validateReport(report))
            return;

    }

    /* All getters and setters */
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getInteractionID() {
        return interactionID;
    }

    public void setInteractionID(String interactionID) {
        this.interactionID = interactionID;
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

    public void setIACalendar(Calendar IACalendar) {
        this.IACalendar = IACalendar;
    }
}
