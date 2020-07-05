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

import java.util.List;

@RunWith(JUnit4.class)
public final class DatabaseQueryTest {

    private final Context context = InstrumentationRegistry.getTargetContext();
    private long id;

    @Before
    public void insert() {
        id = DatabaseHelper.getInstance(context).addAlarm(new Reminder());
        Assert.assertTrue(id != -1);
    }

    @Test
    public void getAlarms() {
        final List<Reminder> reminders = DatabaseHelper.getInstance(context).getAlarms();
        Assert.assertTrue(reminders.size() > 0);
    }

    @After
    public void delete() {
        final int rowsDeleted = DatabaseHelper.getInstance(context).deleteAlarm(id);
        Assert.assertEquals(1, rowsDeleted);
    }

}
