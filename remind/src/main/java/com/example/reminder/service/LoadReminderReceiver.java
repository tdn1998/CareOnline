package com.example.reminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.reminder.model.Reminder;

import java.util.ArrayList;

public final class LoadReminderReceiver extends BroadcastReceiver {

    private OnAlarmsLoadedListener mListener;

    @SuppressWarnings("unused")
    public LoadReminderReceiver(){}

    public LoadReminderReceiver(OnAlarmsLoadedListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final ArrayList<Reminder> reminders =
                intent.getParcelableArrayListExtra(LoadReminderService.ALARMS_EXTRA);
        mListener.onAlarmsLoaded(reminders);
    }

    public void setOnAlarmsLoadedListener(OnAlarmsLoadedListener listener) {
        mListener = listener;
    }

    public interface OnAlarmsLoadedListener {
        void onAlarmsLoaded(ArrayList<Reminder> reminders);
    }

}
