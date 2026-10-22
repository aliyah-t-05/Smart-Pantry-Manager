package com.example.smartpantry.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** Helpers for the optional expiry date ("yyyy-MM-dd"). */
public final class ExpiryUtils {

    public static final String FORMAT = "yyyy-MM-dd";
    private static final long DAY_MS = 24L * 60 * 60 * 1000;

    private ExpiryUtils() { }

    /** True if the item expires within {@code days} days from today (or has already expired). */
    public static boolean isExpiringSoon(String isoDate, int days) {
        if (isoDate == null || isoDate.trim().isEmpty()) return false;
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(FORMAT, Locale.US);
            fmt.setLenient(false);
            Date expiry = fmt.parse(isoDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long daysLeft = Math.round((expiry.getTime() - today.getTimeInMillis()) / (double) DAY_MS);
            return daysLeft <= days;
        } catch (ParseException e) {
            return false;
        }
    }
}
