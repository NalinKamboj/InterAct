package com.comakeit.inter_act.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
//    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //Interactions table Columns names
    private static final String COLUMN_INTERACTION_ID = "interaction_id";
    private static final String COLUMN_I_FROM_USER_EMAIL = "from_user_email";
    private static final String COLUMN_I_TO_USER_EMAIL = "to_user_email";
    private static final String COLUMN_I_EVENT_NAME = "event_name";
    private static final String COLUMN_I_EVENT_TIMESTAMP = "event_timestamp";
    private static final String COLUMN_I_IS_ANONYMOUS = "is_anonymous";
    private static final String COLUMN_I_CONTEXT = "context";
    private static final String COLUMN_I_INTERACTION_TYPE = "interaction_type";
    private static final String COLUMN_I_DESCRIPTION = "description";     //Suggestion will be a part of description in case of feedback
    private static final String COLUMN_I_INTERACTION_TIMESTAMP = "interaction_timestamp";
    private static final String COLUMN_I_ACKNOWLEDGEMENT = "acknowledged";
    private static final String COLUMN_I_ACK_TIMESTAMP = "ack_timestamp";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_NAME + " TEXT PRIMARY KEY, " + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //create interaction table sql query
    private String CREATE_INTERACTION_TABLE = "CREATE TABLE " + TABLE_INTERACTION + "(" + COLUMN_INTERACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_I_FROM_USER_EMAIL
            + " TEXT," + COLUMN_I_TO_USER_EMAIL + " TEXT," + COLUMN_I_EVENT_NAME + " TEXT," + COLUMN_I_EVENT_TIMESTAMP + " TIMESTAMP," + COLUMN_I_IS_ANONYMOUS + " INTEGER,"
            + COLUMN_I_DESCRIPTION + " TEXT, " + COLUMN_I_CONTEXT + " TEXT, " + COLUMN_I_INTERACTION_TYPE + " INT DEFAULT 0," + COLUMN_I_INTERACTION_TIMESTAMP
            + " DATETIME DEFAULT CURRENT_TIMESTAMP," + COLUMN_I_ACKNOWLEDGEMENT + " INTEGER DEFAULT 0, " + COLUMN_I_ACK_TIMESTAMP + " DATETIME DEFAULT NULL)";
    //drop interaction table sql query
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
        Log.i("SQL ON CREATE", CREATE_USER_TABLE);
        db.execSQL(CREATE_INTERACTION_TABLE);
        Log.i("SQL ON CREATE", CREATE_INTERACTION_TABLE);
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
//        UserDetails.setUserID(id);
        db.close();
    }

    /**
     * This method is to create an InterAction Record
     * @param interaction @Nonnull InterAction to be added to DB
     *
     */
    public long addTnterAction(Interaction interaction){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        int isAnonymous = interaction.isAnonymous()?1:0;
        Log.i("DB HELPER IA TYPE", "Anonymous " + isAnonymous);
//        String eventTime = extractTime(interaction.getEventCalendar());
//        String IATime = extractTime(interaction.getIACalendar());
//
//        values.put(COLUMN_I_TO_USER_EMAIL, interaction.getToUser().toUpperCase());
//        values.put(COLUMN_I_FROM_USER_EMAIL, UserDetails.getUserEmail().toUpperCase());
//        values.put(COLUMN_I_EVENT_NAME, interaction.getEventName());
//        values.put(COLUMN_I_EVENT_TIMESTAMP, eventTime);
//        values.put(COLUMN_I_DESCRIPTION, interaction.getDescription());
//        values.put(COLUMN_I_CONTEXT, interaction.getContext());
//        values.put(COLUMN_I_IS_ANONYMOUS, (interaction.isAnonymous() ? 1 : 0));
//        values.put(COLUMN_I_INTERACTION_TYPE, interaction.getIAType());
//        values.put(COLUMN_I_INTERACTION_TIMESTAMP, IATime);
//        Log.i("DB HELPER GEN REPORT", "TO " + interaction.getToUser().toUpperCase() + " \n ,FROM: " + UserDetails.getUserEmail().toUpperCase() + "\n ,EVENT: "
//        + interaction.getEventName().toUpperCase() + " \n ,EVENT TIME: " + eventTime + " \n ,DESC: " + interaction.getDescription() + "BLA...");

        long response = database.insert(TABLE_INTERACTION, null, values);
        database.close();
        return response;
    }

    public String extractTime(Calendar calendar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   //TODO Add LOCALE. PRIORITY: LOW
        Date date = calendar.getTime();
        Log.i("DB HELPER EXTRACT TIME", "TIME: " + simpleDateFormat.format(date));
        return simpleDateFormat.format(date);
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public void tempFunc(Context context){
        SQLiteDatabase database = this.getReadableDatabase();

        String[] columns = {
                COLUMN_I_FROM_USER_EMAIL,
                COLUMN_I_DESCRIPTION,
                COLUMN_I_CONTEXT,
                COLUMN_I_EVENT_NAME,
                COLUMN_I_EVENT_TIMESTAMP,
                COLUMN_I_IS_ANONYMOUS,
                COLUMN_I_INTERACTION_TYPE,
                COLUMN_I_INTERACTION_TIMESTAMP,
                COLUMN_I_ACKNOWLEDGEMENT,
                COLUMN_I_ACK_TIMESTAMP
        };

        String[] args = {UserDetails.getUserEmail().toUpperCase()};
        Cursor cursor = database.query(TABLE_INTERACTION,
                columns,
                COLUMN_I_TO_USER_EMAIL + "=?",
                args,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            Log.i("DB RECEIVE ", "OMG WORKED! Interaction 1 FROM: " + cursor.getString(cursor.getColumnIndex(COLUMN_I_FROM_USER_EMAIL))
                    + " DESC: " + cursor.getString(cursor.getColumnIndex(COLUMN_I_DESCRIPTION)));
        } else
            Log.i("DB RECEIVE", "NO ROWS IN IA DB");
        cursor.close();
    }

    public List<Interaction> getReceivedInteraction() { //TODO Include arguments for filtering InterActions PRIORITY: LOW
        List<Interaction> interactionList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = {
                COLUMN_I_FROM_USER_EMAIL,
                COLUMN_I_DESCRIPTION,
                COLUMN_I_CONTEXT,
                COLUMN_I_EVENT_NAME,
                COLUMN_I_EVENT_TIMESTAMP,
                COLUMN_I_IS_ANONYMOUS,
                COLUMN_I_INTERACTION_TYPE,
                COLUMN_I_INTERACTION_TIMESTAMP,
                COLUMN_I_ACKNOWLEDGEMENT,
                COLUMN_I_ACK_TIMESTAMP
        };
        String order = "datetime(" + COLUMN_I_INTERACTION_TIMESTAMP + ") DESC";

        String[] args = {UserDetails.getUserEmail().toUpperCase()};
        Log.i("CURRENT USER: ", UserDetails.getUserEmail().toUpperCase());
        Cursor cursor = database.query(TABLE_INTERACTION,
                columns,
                COLUMN_I_TO_USER_EMAIL + "=?",
                args,
                null,
                null,
                order);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                Interaction interaction = new Interaction();
                interaction.setFromUserEmail(cursor.getString(cursor.getColumnIndex(COLUMN_I_FROM_USER_EMAIL)));
                interaction.setEventName(cursor.getString(cursor.getColumnIndex(COLUMN_I_EVENT_NAME)));
//                interaction.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_I_DESCRIPTION)));
//                interaction.setIAType(cursor.getInt(cursor.getColumnIndex(COLUMN_I_INTERACTION_TYPE)));
                interaction.setContext(cursor.getString(cursor.getColumnIndex(COLUMN_I_CONTEXT)));
                interaction.setAnonymous(cursor.getInt(cursor.getColumnIndex(COLUMN_I_IS_ANONYMOUS)) == 1);

                //Retrieving time stored as string and formatting it.
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   //TODO Add LOCALE. PRIORITY: LOW
                String IAtimestamp = cursor.getString(cursor.getColumnIndex(COLUMN_I_INTERACTION_TIMESTAMP));
                String eventTimeStamp = cursor.getString(cursor.getColumnIndex(COLUMN_I_EVENT_TIMESTAMP));
                Date IADate;
                Date eventDate;
                //Writing date to Calendar
                Calendar interactionCalendar = Calendar.getInstance();
                Calendar eventCalendar = Calendar.getInstance();
                try {
                    IADate = simpleDateFormat.parse(IAtimestamp);
                    interactionCalendar.setTime(IADate);
//                    interaction.setIACalendar(interactionCalendar);
                } catch (ParseException e) {
                    IADate = null;
                }

                try {
                    eventDate = simpleDateFormat.parse(eventTimeStamp);
                    eventCalendar.setTime(eventDate);
//                    interaction.setEventCalendar(eventCalendar);
                } catch (ParseException e1) {
                    eventDate = null;
                }

                if(IADate != null  && eventDate != null){
                    interactionList.add(interaction);
//                    Log.i("DB HELPER RECV", "Latest Report - " + interaction.getFromUserEmail() + " says " + interaction.getDescription() + " \n CONTEXT "
//                            + interaction.getContext());`
                }
                cursor.moveToNext();
            }
        } else
            Log.i("DB RECEIVE", "NO ROWS IN IA DB");
        cursor.close();

        return interactionList;
    }


    public List<UserDetails> getAllUser() {         //TODO This fucntion will wreck havoc cuz UserDetails has all static members! PRIORITY: HIGH
        // array of columns to fetch
        String[] columns = {
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
        values.put(COLUMN_USER_NAME, UserDetails.getUserName());
        values.put(COLUMN_USER_EMAIL, UserDetails.getUserEmail());
        values.put(COLUMN_USER_PASSWORD, UserDetails.getUserPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_EMAIL + " = ?",
                new String[]{String.valueOf(UserDetails.getUserID())});
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
        db.delete(TABLE_USER, COLUMN_USER_EMAIL + " = ?",
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
                COLUMN_USER_EMAIL
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
                COLUMN_USER_EMAIL
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
