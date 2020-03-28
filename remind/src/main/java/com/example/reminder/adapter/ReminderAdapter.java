package com.example.reminder.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reminder.simplealarms.R;
import com.example.reminder.model.Reminder;
import com.example.reminder.ui.AddEditReminderActivity;
import com.example.reminder.util.ReminderUtils;

import java.util.List;

public final class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private List<Reminder> mReminders;

    private String[] mDays;
    private int mAccentColor = -1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context c = parent.getContext();
        final View v = LayoutInflater.from(c).inflate(R.layout.remind_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Context c = holder.itemView.getContext();

        if(mAccentColor == -1) {
            mAccentColor = ContextCompat.getColor(c, R.color.white);
        }

        if(mDays == null){
            mDays = c.getResources().getStringArray(R.array.days_abbreviated);
        }

        final Reminder reminder = mReminders.get(position);

        holder.time.setText(ReminderUtils.getReadableTime(reminder.getTime()));
        holder.amPm.setText(ReminderUtils.getAmPm(reminder.getTime()));
        holder.label.setText(reminder.getLabel());
        holder.days.setText(buildSelectedDays(reminder));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context c = view.getContext();
                final Intent launchEditAlarmIntent =
                        AddEditReminderActivity.buildAddEditAlarmActivityIntent(
                                c, AddEditReminderActivity.EDIT_ALARM
                        );
                launchEditAlarmIntent.putExtra(AddEditReminderActivity.ALARM_EXTRA, reminder);
                c.startActivity(launchEditAlarmIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mReminders == null) ? 0 : mReminders.size();
    }

    private Spannable buildSelectedDays(Reminder reminder) {

        final int numDays = 7;
        final SparseBooleanArray days = reminder.getDays();

        final SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan span;

        int startIndex, endIndex;
        for (int i = 0; i < numDays; i++) {

            startIndex = builder.length();

            final String dayText = mDays[i];
            builder.append(dayText);
            builder.append(" ");

            endIndex = startIndex + dayText.length();

            final boolean isSelected = days.valueAt(i);
            if(isSelected) {
                span = new ForegroundColorSpan(mAccentColor);
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return builder;

    }

    public void setAlarms(List<Reminder> reminders) {
        mReminders = reminders;
        notifyDataSetChanged();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        final TextView time, amPm, label, days;

        ViewHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.ar_time);
            amPm = itemView.findViewById(R.id.ar_am_pm);
            label = itemView.findViewById(R.id.ar_label);
            days = itemView.findViewById(R.id.ar_days);

        }
    }

}
