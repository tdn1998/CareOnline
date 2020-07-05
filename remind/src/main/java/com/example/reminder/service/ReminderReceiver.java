package com.example.reminder.service;

import android.app.AlarmManager;
import android.app.AlarmManager.AlarmClockInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.reminder.model.Reminder;
import com.example.reminder.simplealarms.R;
import com.example.reminder.util.ReminderUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.example.reminder.ui.ReminderLandingPageActivity.launchIntent;

public final class ReminderReceiver extends BroadcastReceiver {

    private static final String TAG = ReminderReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "alarm_channel";

    private static final String BUNDLE_EXTRA = "bundle_extra";
    private static final String ALARM_KEY = "alarm_key";

    @Override
    public void onReceive(Context context, Intent intent) {

        final Reminder reminder = Objects.requireNonNull(intent.getBundleExtra(BUNDLE_EXTRA)).getParcelable(ALARM_KEY);
        if(reminder == null) {
            Log.e(TAG, "Alarm is null", new NullPointerException());
            return;
        }

        final int id = reminder.notificationId();

        final NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notification);
        builder.setColor(ContextCompat.getColor(context, R.color.blue));
        builder.setContentTitle("Medicine Reminder");
        builder.setContentText(reminder.getLabel());
        builder.setTicker(reminder.getLabel());
        builder.setVibrate(new long[] {1000,500,1000,500,1000,500});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setContentIntent(launchAlarmLandingPage(context, reminder));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        assert manager != null;
        manager.notify(id, builder.build());

        //Reset Alarm manually
        setReminderAlarm(context, reminder);
    }

    //Convenience method for setting a notification
    public static void setReminderAlarm(Context context, Reminder reminder) {

        //Check whether the alarm is set to run on any days
        if(!ReminderUtils.isAlarmActive(reminder)) {
            //If alarm not set to run on any days, cancel any existing notifications for this alarm
            cancelReminderAlarm(context, reminder);
            return;
        }

        final Calendar nextAlarmTime = getTimeForNextAlarm(reminder);
        reminder.setTime(nextAlarmTime.getTimeInMillis());

        final Intent intent = new Intent(context, ReminderReceiver.class);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(ALARM_KEY, reminder);
        intent.putExtra(BUNDLE_EXTRA, bundle);

        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                reminder.notificationId(),
                intent,
                FLAG_UPDATE_CURRENT
        );

        ScheduleAlarm.with(context).schedule(reminder, pIntent);
    }

    public static void setReminderAlarms(Context context, List<Reminder> reminders) {
        for(Reminder reminder : reminders) {
            setReminderAlarm(context, reminder);
        }
    }
    private static Calendar getTimeForNextAlarm(Reminder reminder) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getTime());

        final long currentTime = System.currentTimeMillis();
        final int startIndex = getStartIndexFromTime(calendar);

        int count = 0;
        boolean isAlarmSetForDay;

        final SparseBooleanArray daysArray = reminder.getDays();

        do {
            final int index = (startIndex + count) % 7;
            isAlarmSetForDay =
                    daysArray.valueAt(index) && (calendar.getTimeInMillis() > currentTime);
            if(!isAlarmSetForDay) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                count++;
            }
        } while(!isAlarmSetForDay && count < 7);

        return calendar;

    }

    public static void cancelReminderAlarm(Context context, Reminder reminder) {

        final Intent intent = new Intent(context, ReminderReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(
                context,
                reminder.notificationId(),
                intent,
                FLAG_UPDATE_CURRENT
        );

        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert manager != null;
        manager.cancel(pIntent);
    }

    private static int getStartIndexFromTime(Calendar c) {

        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        int startIndex = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                startIndex = 0;
                break;
            case Calendar.TUESDAY:
                startIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                startIndex = 2;
                break;
            case Calendar.THURSDAY:
                startIndex = 3;
                break;
            case Calendar.FRIDAY:
                startIndex = 4;
                break;
            case Calendar.SATURDAY:
                startIndex = 5;
                break;
            case Calendar.SUNDAY:
                startIndex = 6;
                break;
        }

        return startIndex;

    }

    private static void createNotificationChannel(Context ctx) {

        final NotificationManager mgr = ctx.getSystemService(NotificationManager.class);
        if(mgr == null) return;

        final String name = ctx.getString(R.string.channel_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(mgr.getNotificationChannel(name) == null) {
                final NotificationChannel channel =
                        new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[] {1000,500,1000,500,1000,500});
                channel.setBypassDnd(true);
                mgr.createNotificationChannel(channel);
            }
        }
    }

    private static PendingIntent launchAlarmLandingPage(Context ctx, Reminder reminder) {
        return PendingIntent.getActivity(
                ctx, reminder.notificationId(), launchIntent(ctx), FLAG_UPDATE_CURRENT
        );
    }

    private static class ScheduleAlarm {

        @NonNull private final Context ctx;
        @NonNull private final AlarmManager am;

        private ScheduleAlarm(@NonNull AlarmManager am, @NonNull Context ctx) {
            this.am = am;
            this.ctx = ctx;
        }

        static ScheduleAlarm with(Context context) {
            final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if(am == null) {
                throw new IllegalStateException("AlarmManager is null");
            }
            return new ScheduleAlarm(am, context);
        }

        void schedule(Reminder reminder, PendingIntent pi) {
            am.setAlarmClock(new AlarmClockInfo(reminder.getTime(), launchAlarmLandingPage(ctx, reminder)), pi);
        }

    }

}
