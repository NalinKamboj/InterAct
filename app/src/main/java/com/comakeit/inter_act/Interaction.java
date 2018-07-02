package com.comakeit.inter_act;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.comakeit.inter_act.sql.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Interaction implements Parcelable {
    private String fromUserEmail;
    private String toUserEmail;
    private String eventName;
    private String description;
    private String mContext;
    int IAType; //0 for FB and 1 for AP
    int interactionID;
    private boolean isAnonymous;
    private Calendar eventCalendar, IACalendar;
    protected UserDetails mUserDetails;

    //Default constructor
    public Interaction(){
        this.interactionID = -1;
        this.fromUserEmail = UserDetails.getUserEmail();
        String[] parts = fromUserEmail.split("@");
        this.IACalendar = Calendar.getInstance();
//        Log.i("Reporting Report ID: ", interactionID);
        this.toUserEmail = "";
        this.eventName = "";
        this.IAType = 0;
        this.description = "";
        this.isAnonymous = false;
        this.eventCalendar = null;
    }

    //Parcel methods - TODO Passing TIME variables as an intent extra for now... (Maybe write a string and convert it back to Calendar :"( )
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromUserEmail);
        dest.writeString(toUserEmail);
        dest.writeString(eventName);
        dest.writeString(description);
        dest.writeString(mContext);
        dest.writeInt(IAType);
        dest.writeByte((byte) (isAnonymous ? 1 : 0));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        Date eventDate = eventCalendar.getTime();
        Date IADate = IACalendar.getTime();
        String eventDateString = simpleDateFormat.format(eventDate);
        String IADateString = simpleDateFormat.format(IADate);
        Log.i("PARCEL BEFORE WRITE", "EVENT:" + eventDateString + "\n IA:" + IADateString);
        dest.writeString(eventDateString);
        dest.writeString(IADateString);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Overloading constructor to implement parcel
    public Interaction(Parcel in){
        this.fromUserEmail = in.readString();
        this.toUserEmail = in.readString();
        this.eventName = in.readString();
        this.description = in.readString();
        this.mContext = in.readString();
        this.IAType = in.readInt();
        this.isAnonymous = in.readByte() != 0;
        String eventTime = in.readString();
        String IATime = in.readString();

        //Formatting the TIME strings and storing them in Calendar
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Calendar eventCal = Calendar.getInstance();
        Calendar IACal = Calendar.getInstance();
        try{
            eventCal.setTime(simpleDateFormat.parse(eventTime));
            IACal.setTime(simpleDateFormat.parse(IATime));
            this.eventCalendar = eventCal;
            this.IACalendar = IACal;
        } catch (ParseException e) {
            Log.e("INTERACTION CONSTRUCTOR", "COULD NOT PARSE TIME STRING");
        }

    }

    //De-serialize the object using Parcel
    public static final Parcelable.Creator<Interaction> CREATOR = new Parcelable.Creator<Interaction>() {
        public Interaction createFromParcel(Parcel in) {
            return new Interaction(in);
        }

        @Override
        public Interaction[] newArray(int i) {
            return new Interaction[i];
        }
    };


    public boolean validateReport(Interaction report, Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        if(databaseHelper.checkUser(report.toUserEmail.toUpperCase()))
            return !(report.eventName.equals("") || report.description.equals("") || eventCalendar == null);
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

    public String getContext() {
        return mContext;
    }

    public void setContext(String context) {
        mContext = context;
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

    public int getIAType() {
        return IAType;
    }

    public void setIAType(int IAType) {
        this.IAType = IAType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setFromUserEmail(String email) {
        this.fromUserEmail = email;
    }

    public void setToUserEmail(String email) {
        this.toUserEmail = email;
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
