package com.example.reminder.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.reminder.data.DatabaseHelper;
import com.example.reminder.model.Reminder;
import com.example.reminder.service.ReminderReceiver;
import com.example.reminder.service.LoadReminderService;
import com.example.reminder.simplealarms.R;
import com.example.reminder.util.ViewUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class AddEditReminderFragment extends Fragment {

    private EditText mLabel;
    private ChipGroup grp;
    private Chip Mon, Tue, Wed, Thurs, Fri, Sat, Sun;
    private FloatingActionButton fab_save,fab_delete;
    private TimePicker mTimePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_edit_remind, container, false);

        setHasOptionsMenu(true);

        final Reminder reminder = getAlarm();

        mLabel = v.findViewById(R.id.edit_alarm_label);
        mLabel.setText(reminder.getLabel());

        mTimePicker = v.findViewById(R.id.edit_alarm_time_picker);
        ViewUtils.setTimePickerTime(mTimePicker, reminder.getTime());

        Mon = v.findViewById(R.id.mon);
        Tue = v.findViewById(R.id.tue);
        Wed = v.findViewById(R.id.wed);
        Thurs = v.findViewById(R.id.thrus);
        Fri = v.findViewById(R.id.fri);
        Sat = v.findViewById(R.id.sat);
        Sun = v.findViewById(R.id.sun);

        grp = v.findViewById(R.id.chipGroup);
        fab_save=v.findViewById(R.id.save);
        fab_delete=v.findViewById(R.id.delete);

        fab_clicked();
        checkedchip(reminder);
        return v;
    }

    private void fab_clicked() {
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(getAlarm());
            }
        });
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(getAlarm());
            }
        });
    }

    public static AddEditReminderFragment newInstance(Reminder reminder) {

        Bundle args = new Bundle();
        args.putParcelable(AddEditReminderActivity.ALARM_EXTRA, reminder);

        AddEditReminderFragment fragment = new AddEditReminderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void checkedchip(Reminder reminder) {
        if (reminder.getDay(Reminder.MON)) {
            Mon.setChecked(true);
        }
        if (reminder.getDay(Reminder.TUES)) {
            Tue.setChecked(true);
        }
        if (reminder.getDay(Reminder.WED)) {
            Wed.setChecked(true);
        }
        if (reminder.getDay(Reminder.THURS)) {
            Thurs.setChecked(true);
        }
        if (reminder.getDay(Reminder.FRI)) {
            Fri.setChecked(true);
        }
        if (reminder.getDay(Reminder.SAT)) {
            Sat.setChecked(true);
        }
        if (reminder.getDay(Reminder.SUN)) {
            Sun.setChecked(true);
        }
    }

    private Reminder getAlarm() {
        assert getArguments() != null;
        return getArguments().getParcelable(AddEditReminderActivity.ALARM_EXTRA);
    }

    private void save(Reminder reminder) {

        if (mLabel.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Write Reminder Label", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Mon.isChecked() && !Tue.isChecked() && !Wed.isChecked()
                && !Thurs.isChecked() && !Fri.isChecked() && !Sat.isChecked() && !Sun.isChecked()) {
            Toast.makeText(getActivity(), "Select Day/Days", Toast.LENGTH_SHORT).show();
            return;
        }

        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        reminder.setTime(time.getTimeInMillis());

        reminder.setLabel(mLabel.getText().toString());

        if (Mon.isChecked()) {
            reminder.setDay(Reminder.MON, true);
        } else {
            reminder.setDay(Reminder.MON, false);
        }
        if (Tue.isChecked()) {
            reminder.setDay(Reminder.TUES, true);
        } else {
            reminder.setDay(Reminder.TUES, false);
        }
        if (Wed.isChecked()) {
            reminder.setDay(Reminder.WED, true);
        } else {
            reminder.setDay(Reminder.WED, false);
        }
        if (Thurs.isChecked()) {
            reminder.setDay(Reminder.THURS, true);
        } else {
            reminder.setDay(Reminder.THURS, false);
        }
        if (Fri.isChecked()) {
            reminder.setDay(Reminder.FRI, true);
        } else {
            reminder.setDay(Reminder.FRI, false);
        }
        if (Sat.isChecked()) {
            reminder.setDay(Reminder.SAT, true);
        } else {
            reminder.setDay(Reminder.SAT, false);
        }
        if (Sun.isChecked()) {
            reminder.setDay(Reminder.SUN, true);
        } else {
            reminder.setDay(Reminder.SUN, false);
        }

        final int rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(reminder);
        final int messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;

        Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();

        ReminderReceiver.setReminderAlarm(getContext(), reminder);

        Objects.requireNonNull(getActivity()).finish();
    }

    private void delete(Reminder reminder) {

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.DeleteAlarmDialogTheme);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel any pending notifications for this alarm
                ReminderReceiver.cancelReminderAlarm(getContext(), reminder);

                final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(reminder);
                int messageId;
                if (rowsDeleted == 1) {
                    messageId = R.string.delete_complete;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                    LoadReminderService.launchLoadAlarmsService(getContext());
                    getActivity().finish();
                } else {
                    messageId = R.string.delete_failed;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_remind_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            save(getAlarm());
        } else if (itemId == R.id.action_delete) {
            delete(getAlarm());
        }
        return super.onOptionsItemSelected(item);
    }
}
