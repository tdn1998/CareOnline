package com.example.reminder.data;

import android.content.Context;

import androidx.test.InstrumentationRegistry;

import com.example.reminder.model.Reminder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DatabaseWriteTest {

    private final Context context = InstrumentationRegistry.getTargetContext();
    private long id;

    @Before
    public void insert() throws Exception {
        id = DatabaseHelper.getInstance(context).addAlarm(new Reminder());
        Assert.assertTrue(id != -1);
    }

    @Test
    public void update() throws Exception {
        final Reminder reminder = new Reminder(id);
        reminder.setIsEnabled(false);
        final int rowsUpdated = DatabaseHelper.getInstance(context).updateAlarm(reminder);
        Assert.assertTrue(rowsUpdated == 1);
    }

    @After
    public void delete() throws Exception {
        final int rowsDeleted = DatabaseHelper.getInstance(context).deleteAlarm(id);
        Assert.assertTrue(rowsDeleted == 1);
    }

}
