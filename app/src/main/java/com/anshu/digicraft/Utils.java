package com.anshu.digicraft;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    public static String formatTimestamp(String isoDate) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(isoDate);

            SimpleDateFormat localFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return localFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }

}
