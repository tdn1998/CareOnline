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
    public void insert() {
        id = DatabaseHelper.getInstance(context).addAlarm(new Reminder());
        Assert.assertTrue(id != -1);
    }

    @Test
    public void update() {
        final Reminder reminder = new Reminder(id);
        reminder.setIsEnabled(false);
        final int rowsUpdated = DatabaseHelper.getInstance(context).updateAlarm(reminder);
        Assert.assertEquals(1, rowsUpdated);
    }

    @After
    public void delete() {
        final int rowsDeleted = DatabaseHelper.getInstance(context).deleteAlarm(id);
        Assert.assertEquals(1, rowsDeleted);
    }

}
