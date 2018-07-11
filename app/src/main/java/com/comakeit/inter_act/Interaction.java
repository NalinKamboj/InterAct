package com.comakeit.inter_act;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Interaction implements Parcelable {
    private Long interactionID;
    private Long fromUserId;
    private Long toUserId;
    private String eventName;
    private String observation;
    private String context;
    private String recommendation;
    int type; //0 for FB and 1 for AP
    private boolean isAnonymous, acknowledged;
    private Date eventDateDate, createdAt, acknowledgementDate;     //eventDateDate is for DATE type format
    private String eventDate;       //eventDate string is for passing to JSON
    private String fromUserEmail, toUserEmail;

    //Default constructor
    public Interaction(){
        this.interactionID = (long) -1;
        this.fromUserId = UserDetails.getUserID();
        this.createdAt = null;
//        Log.i("Reporting Report ID: ", interactionID);
        this.toUserId = (long) -1;
        this.eventName = "";
        this.type = 0;
        this.context = "";
        this.recommendation = "";
        this.observation = "";
        this.isAnonymous = false;
        this.eventDateDate = null;
        this.acknowledgementDate = null;
    }

    //Parcel methods - TODO Passing TIME variables as an intent extra for now... (Maybe write a string and convert it back to Calendar :"( )
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String eventDateString = simpleDateFormat.format(eventDateDate);
        String createdAtString = simpleDateFormat.format(createdAt);
        Log.i("PARCEL BEFORE WRITE", "EVENT:" + eventDateString + "\n IA:" + createdAtString);

        dest.writeLong(fromUserId);
        dest.writeLong(toUserId);
        dest.writeString(createdAtString);
        dest.writeString(observation);
        dest.writeString(context);
        dest.writeString(recommendation);
        dest.writeInt(type);
        dest.writeString(eventName);
        dest.writeString(eventDateString);
        dest.writeByte((byte) (isAnonymous ? 1 : 0));
        dest.writeByte((byte) (acknowledged ? 1 : 0));
        dest.writeString(fromUserEmail);
        dest.writeString(toUserEmail);
        dest.writeString(eventDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Overloading constructor to implement parcel
    public Interaction(Parcel in){
        this.fromUserId = in.readLong();
        this.toUserId = in.readLong();
        String createdAtString = in.readString();
        this.observation = in.readString();
        this.context = in.readString();
        this.recommendation = in.readString();
        this.type = in.readInt();
        this.eventName = in.readString();
        String eventDateString = in.readString();
        this.isAnonymous = in.readByte() != 0;
        this.acknowledged = in.readByte() != 0;
        this.fromUserEmail = in.readString();
        this.toUserEmail = in.readString();
        this.eventDate = in.readString();

        //Formatting the TIME strings and storing them in Calendar
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Calendar eventCal = Calendar.getInstance();
        Calendar IACal = Calendar.getInstance();
        try{
            eventCal.setTime(simpleDateFormat.parse(eventDateString));
            IACal.setTime(simpleDateFormat.parse(createdAtString));
            this.eventDateDate = eventCal.getTime();
            this.createdAt = IACal.getTime();
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

//        if(databaseHelper.checkUser(report.toUserEmail.toUpperCase()))
//            return !(report.eventName.equals("") || report.description.equals("") || eventCalendar == null);
//        else
            return true;
    }

    /* All getters and setters */
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        context = context;
    }

    public void setInteractionID(Long ID) {
        this.interactionID = ID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getType() {
        return type;
    }

    public void setType(int IAType) {
        this.type = IAType;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public Date getEventDateDate() {
        return eventDateDate;
    }

    public void setEventDateDate(Date eventDateDate) {
        this.eventDateDate = eventDateDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = simpleDateFormat.format(eventDateDate);
        setEventDate(date);
    }

    public void setFromUserId(Long id) {
        this.fromUserId = id;
    }

    public void setToUserId(Long id) {
        this.toUserId = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Long getFromUserId(){
        return this.fromUserId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getInteractionID() {
        return interactionID;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public Date getAcknowledgementDate() {
        return acknowledgementDate;
    }

    public void setAcknowledgementDate(Date acknowledgementDate) {
        this.acknowledgementDate = acknowledgementDate;
    }

    public String getFromUserEmail() {
        return fromUserEmail;
    }

    public void setFromUserEmail(String fromUserEmail) {
        this.fromUserEmail = fromUserEmail;
    }

    public String getToUserEmail() {
        return toUserEmail;
    }

    public void setToUserEmail(String toUserEmail) {
        this.toUserEmail = toUserEmail;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
