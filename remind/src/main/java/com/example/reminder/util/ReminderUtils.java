package com.example.reminder.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;

import androidx.core.app.ActivityCompat;
import android.util.SparseBooleanArray;

import com.example.reminder.data.DatabaseHelper;
import com.example.reminder.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.reminder.data.DatabaseHelper.COL_FRI;
import static com.example.reminder.data.DatabaseHelper.COL_IS_ENABLED;
import static com.example.reminder.data.DatabaseHelper.COL_LABEL;
import static com.example.reminder.data.DatabaseHelper.COL_MON;
import static com.example.reminder.data.DatabaseHelper.COL_SAT;
import static com.example.reminder.data.DatabaseHelper.COL_SUN;
import static com.example.reminder.data.DatabaseHelper.COL_THURS;
import static com.example.reminder.data.DatabaseHelper.COL_TIME;
import static com.example.reminder.data.DatabaseHelper.COL_TUES;
import static com.example.reminder.data.DatabaseHelper.COL_WED;
import static com.example.reminder.data.DatabaseHelper._ID;

public final class ReminderUtils {

    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("h:mm", Locale.getDefault());
    private static final SimpleDateFormat AM_PM_FORMAT =
            new SimpleDateFormat("a", Locale.getDefault());

    private static final int REQUEST_ALARM = 1;
    private static final String[] PERMISSIONS_ALARM = {
            Manifest.permission.VIBRATE
    };

    private ReminderUtils() { throw new AssertionError(); }

    public static void checkAlarmPermissions(Activity activity) {

        final int permission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.VIBRATE
        );

        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_ALARM,
                    REQUEST_ALARM
            );
        }

    }

    public static ContentValues toContentValues(Reminder reminder) {

        final ContentValues cv = new ContentValues(10);

        cv.put(COL_TIME, reminder.getTime());
        cv.put(COL_LABEL, reminder.getLabel());

        final SparseBooleanArray days = reminder.getDays();
        cv.put(COL_MON, days.get(Reminder.MON) ? 1 : 0);
        cv.put(COL_TUES, days.get(Reminder.TUES) ? 1 : 0);
        cv.put(COL_WED, days.get(Reminder.WED) ? 1 : 0);
        cv.put(COL_THURS, days.get(Reminder.THURS) ? 1 : 0);
        cv.put(COL_FRI, days.get(Reminder.FRI) ? 1 : 0);
        cv.put(COL_SAT, days.get(Reminder.SAT) ? 1 : 0);
        cv.put(COL_SUN, days.get(Reminder.SUN) ? 1 : 0);

        cv.put(DatabaseHelper.COL_IS_ENABLED, reminder.isEnabled());

        return cv;

    }

    public static ArrayList<Reminder> buildAlarmList(Cursor c) {

        if (c == null) return new ArrayList<>();

        final int size = c.getCount();

        final ArrayList<Reminder> reminders = new ArrayList<>(size);

        if (c.moveToFirst()){
            do {

                final long id = c.getLong(c.getColumnIndex(_ID));
                final long time = c.getLong(c.getColumnIndex(COL_TIME));
                final String label = c.getString(c.getColumnIndex(COL_LABEL));
                final boolean mon = c.getInt(c.getColumnIndex(COL_MON)) == 1;
                final boolean tues = c.getInt(c.getColumnIndex(COL_TUES)) == 1;
                final boolean wed = c.getInt(c.getColumnIndex(COL_WED)) == 1;
                final boolean thurs = c.getInt(c.getColumnIndex(COL_THURS)) == 1;
                final boolean fri = c.getInt(c.getColumnIndex(COL_FRI)) == 1;
                final boolean sat = c.getInt(c.getColumnIndex(COL_SAT)) == 1;
                final boolean sun = c.getInt(c.getColumnIndex(COL_SUN)) == 1;
                final boolean isEnabled = c.getInt(c.getColumnIndex(COL_IS_ENABLED)) == 1;

                final Reminder reminder = new Reminder(id, time, label);
                reminder.setDay(Reminder.MON, mon);
                reminder.setDay(Reminder.TUES, tues);
                reminder.setDay(Reminder.WED, wed);
                reminder.setDay(Reminder.THURS, thurs);
                reminder.setDay(Reminder.FRI, fri);
                reminder.setDay(Reminder.SAT, sat);
                reminder.setDay(Reminder.SUN, sun);

                reminder.setIsEnabled(isEnabled);

                reminders.add(reminder);

            } while (c.moveToNext());
        }

        return reminders;

    }

    public static String getReadableTime(long time) {
        return TIME_FORMAT.format(time);
    }

    public static String getAmPm(long time) {
        return AM_PM_FORMAT.format(time);
    }

    public static boolean isAlarmActive(Reminder reminder) {

        final SparseBooleanArray days = reminder.getDays();

        boolean isActive = false;
        int count = 0;

        while (count < days.size() && !isActive) {
            isActive = days.valueAt(count);
            count++;
        }

        return isActive;

    }

    public static String getActiveDaysAsString(Reminder reminder) {

        StringBuilder builder = new StringBuilder("Active Days: ");

        if(reminder.getDay(Reminder.MON)) builder.append("Monday, ");
        if(reminder.getDay(Reminder.TUES)) builder.append("Tuesday, ");
        if(reminder.getDay(Reminder.WED)) builder.append("Wednesday, ");
        if(reminder.getDay(Reminder.THURS)) builder.append("Thursday, ");
        if(reminder.getDay(Reminder.FRI)) builder.append("Friday, ");
        if(reminder.getDay(Reminder.SAT)) builder.append("Saturday, ");
        if(reminder.getDay(Reminder.SUN)) builder.append("Sunday.");

        if(builder.substring(builder.length()-2).equals(", ")) {
            builder.replace(builder.length()-2,builder.length(),".");
        }

        return builder.toString();

    }

}
