package com.comakeit.inter_act;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilities {

    /** Utility function for converting any string to Camel Case. Example - "tHis is A sTrING"  becomes "This Is A String"
     * @param init String to be converted to Camel Case
     * @return String in Camel Case
     */
    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }

    /** Utility function for converting an Interaction into a JSON object
     * @param interaction Interaction to be converted to JSON Object
     * @return
     */
    public static JSONObject createJsonReport(Interaction interaction) {
        JSONObject report = new JSONObject();
        try {
            report.put("toUserId", interaction.getToUserId());
            report.put("fromUserId", interaction.getFromUserId());
            report.put("toUserEmail", interaction.getToUserEmail());
            report.put("fromUserEmail", interaction.getFromUserEmail());
            report.put("eventDate", interaction.getEventDate());
            report.put("eventName", interaction.getEventName());
            report.put("observation", interaction.getObservation());
            report.put("context", interaction.getContext());
            report.put("recommendation", interaction.getRecommendation());
            report.put("type", interaction.getType());
            report.put("rating", interaction.getRating());
            report.put("anonymous", interaction.isAnonymous());
            return report;
        } catch (JSONException e) {
            Log.e("UTILITY JSON CREATOR", "Exception " + e.toString());
            return null;
        }
    }

    //Overloading above method to include id
    public static JSONObject createJsonReport(Interaction interaction, Long id){
        JSONObject report = new JSONObject();
        try {
            report.put("id", id);
            report.put("toUserId", interaction.getToUserId());
            report.put("fromUserId", interaction.getFromUserId());
            report.put("toUserEmail", interaction.getToUserEmail());
            report.put("fromUserEmail", interaction.getFromUserEmail());
            report.put("createdAt", interaction.getCreatedAt());
            report.put("eventDate", interaction.getEventDate());
            report.put("eventName", interaction.getEventName());
            report.put("observation", interaction.getObservation());
            report.put("context", interaction.getContext());
            report.put("recommendation", interaction.getRecommendation());
            report.put("type", interaction.getType());
            report.put("rating", interaction.getRating());
            report.put("anonymous", interaction.isAnonymous());
            return report;
        } catch (JSONException e) {
            Log.e("UTILITY JSON CREATOR", "Exception " + e.toString());
            return null;
        }
    }

    //More overloading for Action object
    public static JSONObject createJsonReport(Action action){
        JSONObject actionJSON = new JSONObject();
        try{
            actionJSON.put("actionDescription", action.getDescription());
            actionJSON.put("progress", action.getProgress());
            actionJSON.put("reportId", action.getInteractionID());
            return actionJSON;
        } catch (JSONException e){
            Log.e("UTILITY JSON CREATOR", e.toString());
            return null;
        }
    }

    //Parse String containing Actions
//    public static List<Action> getActions(String jsonString){
//    }

    //Run this function in any activity to ensure that the application contains the details of the user who is currently using the app. If not, redirect them to the logout screen
    public static boolean runSafetyNet(){
        return UserDetails.getUserID() != -1;
    }

}
