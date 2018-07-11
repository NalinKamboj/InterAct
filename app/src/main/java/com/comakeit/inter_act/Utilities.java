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
            report.put("acknowledged", interaction.isAcknowledged());
            report.put("anonymous", interaction.isAnonymous());
            return report;
        } catch (JSONException e) {
            Log.e("UTILITY JSON CREATOR", "Exception " + e.toString());
            return null;
        }
    }
}
