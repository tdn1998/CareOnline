package com.example.reminder.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.reminder.adapter.ReminderAdapter;
import com.example.reminder.model.Reminder;
import com.example.reminder.service.LoadReminderReceiver;
import com.example.reminder.service.LoadReminderService;
import com.example.reminder.simplealarms.R;
import com.example.reminder.util.ReminderUtils;
import com.example.reminder.view.DividerItemDecoration;
import com.example.reminder.view.EmptyRecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.reminder.ui.AddEditReminderActivity.ADD_ALARM;
import static com.example.reminder.ui.AddEditReminderActivity.buildAddEditAlarmActivityIntent;

public class RemindActivity extends AppCompatActivity implements LoadReminderReceiver.OnAlarmsLoadedListener {

    private LoadReminderReceiver mReceiver;
    private ReminderAdapter mAdapter;
    EmptyRecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        Toolbar remind = findViewById(R.id.toolbar_remind);
        remind.setTitle("Medicine Reminder");
        setSupportActionBar(remind);

        rv_setup();
        fab_clicked();
    }

    private void rv_setup() {
        mReceiver = new LoadReminderReceiver(this);
        rv = findViewById(R.id.recycler);
        mAdapter = new ReminderAdapter();
        rv.setEmptyView(findViewById(R.id.empty_view));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(this));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void fab_clicked() {
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderUtils.checkAlarmPermissions(RemindActivity.this);
                final Intent i = buildAddEditAlarmActivityIntent(RemindActivity.this, ADD_ALARM);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadReminderService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        LoadReminderService.launchLoadAlarmsService(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remind_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_help) {
            //Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help")
                    .setMessage("This is remind the patient to take Medicine on time by giving a notification.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Reminder> reminders) {
        mAdapter.setAlarms(reminders);
    }
}
