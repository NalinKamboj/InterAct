package com.comakeit.inter_act.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comakeit.inter_act.Activities.Interaction;
import com.comakeit.inter_act.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";
    //Interaction table name
    private static final String TABLE_INTERACTION = "interactions";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //Interactions table Columns names
    private static final String COLUMN_INTERACTION_ID = "interaction_id";
    private static final String COLUMN_FROM_USER_ID = "from_user_id";
    private static final String COLUMN_TO_USER_ID = "to_user_id";
    private static final String COLUMN_EVENT_NAME = "event_name";
    private static final String COLUMN_EVENT_TIMESTAMP = "event_timestamp";
    private static final String COLUMN_IS_ANONYMOUS = "is_anonymous";
    private static final String COLUMN_DESCRIPTION = "description";     //Suggestion will be a part of description in case of feedback
    private static final String COLUMN_INTERACTION_TIMESTAMP = "interaction_timestamp";
    private static final String COLUMN_ACKNOWLEDGEMENT = "acknowledges";
    private static final String COLUMN_ACK_TIMESTAMP = "ack_timestamp";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private String CREATE_INTERACTION_TABLE = "CREATE TABLE " + TABLE_INTERACTION + "(" + COLUMN_INTERACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_FROM_USER_ID
            + " INTEGER," + COLUMN_TO_USER_ID + " INTEGER," + COLUMN_EVENT_NAME + " TEXT," + COLUMN_EVENT_TIMESTAMP + " TEXT," + COLUMN_IS_ANONYMOUS + " INTEGER," + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_INTERACTION_TIMESTAMP + " TEXT," + COLUMN_ACKNOWLEDGEMENT + " TEXT, " + COLUMN_ACK_TIMESTAMP + " TEXT)";
    private String DROP_INTERACTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_INTERACTION;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_INTERACTION_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_INTERACTION_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(UserDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, UserDetails.getUserName());
        values.put(COLUMN_USER_EMAIL, UserDetails.getUserEmail().toUpperCase());
        values.put(COLUMN_USER_PASSWORD, UserDetails.getUserPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        String getID = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + " = '" + user.getUserEmail().toUpperCase();
        Cursor cursor = db.rawQuery(getID, null);
        if(cursor!=null)
            cursor.moveToFirst();
        int id = 0;
        try{
            id = cursor.getInt(0);
        } catch (java.lang.NullPointerException e){
            Log.e("ADD USER DB HELPER", "NPE at userID cursor");
        }
        UserDetails.setUserID(id);
        db.close();
    }

    /**
     * This method is to create an InterAction Record
     * @param interaction @Nonnull InterAction to be added to DB
     * @param userDetails   @Nonnull Details of user creating the InterAction
     */
    public void addTnterAction(Interaction interaction, UserDetails userDetails){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<UserDetails> getAllUser() {         //TODO This fucntion will wreck havoc cuz UserDetails has all static members! PRIORITY: HIGH
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<UserDetails> userList = new ArrayList<UserDetails>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserDetails user = new UserDetails();
                UserDetails.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                UserDetails.setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                UserDetails.setUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                UserDetails.setUserPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(UserDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_EMAIL, user.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, user.getUserPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserID())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(UserDetails user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserID())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}
