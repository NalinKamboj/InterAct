package com.comakeit.inter_act;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class GeneralUser implements Parcelable {
    private String firstName, lastName, email, password;
    private int ID;
    public static HashMap <Long, GeneralUser> sUserHashMap = new HashMap<>();
//    private List<Interaction> sentInteractions, receivedInteractions;

    public GeneralUser() {
        ID = -1;
        firstName = "-1";
        lastName = "-1";
        email = "-1";
        password = "-1";
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(ID);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Overloading constructor to implement Parcel interface
    public GeneralUser(Parcel in){
        this.ID = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.password = in.readString();
    }

    //De-serialize the object using Parcel
    public static final Parcelable.Creator<GeneralUser> CREATOR = new Parcelable.Creator<GeneralUser>() {
        @Override
        public GeneralUser createFromParcel(Parcel parcel) {
            return new GeneralUser(parcel);
        }

        @Override
        public GeneralUser[] newArray(int i) {
            return new GeneralUser[i];
        }
    };

    /* Getter and Setter methods of the class */

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
