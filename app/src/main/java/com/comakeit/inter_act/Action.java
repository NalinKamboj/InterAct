package com.comakeit.inter_act;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Action implements Parcelable {

    private static final String TAG = "ACTION CLASS";
    private Long actionID;
    private Long interactionID;
    private String description;
    private int progress;   //0 to 4
    private Date createdAtDate;
    private String createdAt;

    //Default constructor
    public Action(){
        this.actionID = (long) -1;
        this.interactionID = (long) -1;
        this.description = "";
        this.progress = -1;
        this.createdAt = "";
        this.createdAtDate = null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//        String createdAtString = simpleDateFormat.format(createdAtDate);

        dest.writeLong(actionID);
        dest.writeLong(interactionID);
        dest.writeString(description);
        dest.writeInt(progress);
        dest.writeString(createdAt);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    //Overload constructor to implement parcel
    public Action(Parcel in){
        this.actionID = in.readLong();
        this.interactionID = in.readLong();
        this.description = in.readString();
        this.progress = in.readInt();
        this.createdAt = in.readString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date createdAtDate = new Date();
        try{
            createdAtDate = simpleDateFormat.parse(this.createdAt);
        } catch (ParseException ex){
            Log.e(TAG, ex.toString());
        }
        this.createdAtDate = createdAtDate;
    }

    //De-serialize the object using parcel
    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>(){
        public Action createFromParcel(Parcel in){
            return new Action(in);
        }

        @Override
        public Action[] newArray(int i) {
            return new Action[i];
        }
    };


    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public Long getInteractionID() {
        return interactionID;
    }

    public void setInteractionID(Long interactionID) {
        this.interactionID = interactionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    public void setCreatedAtDate(Date createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
