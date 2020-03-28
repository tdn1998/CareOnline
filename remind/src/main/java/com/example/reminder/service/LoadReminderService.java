package com.example.reminder.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.reminder.data.DatabaseHelper;
import com.example.reminder.model.Reminder;

import java.util.ArrayList;
import java.util.List;

public final class LoadReminderService extends IntentService {

    private static final String TAG = LoadReminderService.class.getSimpleName();
    public static final String ACTION_COMPLETE = TAG + ".ACTION_COMPLETE";
    public static final String ALARMS_EXTRA = "alarms_extra";

    @SuppressWarnings("unused")
    public LoadReminderService() {
        this(TAG);
    }

    public LoadReminderService(String name){
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final List<Reminder> reminders = DatabaseHelper.getInstance(this).getAlarms();

        final Intent i = new Intent(ACTION_COMPLETE);
        i.putParcelableArrayListExtra(ALARMS_EXTRA, new ArrayList<>(reminders));
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

    }

    public static void launchLoadAlarmsService(Context context) {
        final Intent launchLoadAlarmsServiceIntent = new Intent(context, LoadReminderService.class);
        context.startService(launchLoadAlarmsServiceIntent);
    }

}
