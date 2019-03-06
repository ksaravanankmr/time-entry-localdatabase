package com.techno.timeentry.activities.mainactivity;

import android.arch.lifecycle.LifecycleOwner;

import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.R;
import com.techno.timeentry.application.TimeEntryApplication;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.models.TimeEntryDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivity view;

    private TimeEntryDataBase timeEntryDataBase;
    private List<EventDay> events;

    MainActivityPresenter(MainActivity view) {
        this.view = view;

        timeEntryDataBase = TimeEntryDataBase.getAppDatabase(TimeEntryApplication.getContext());
        loadTimeEntriesFromDatabase(view);
    }

    @Override
    public void onDateClicked(EventDay eventDay) {

        if (events.contains(eventDay)) {
            loadTimeEntriesFromDatabaseForDate(view, eventDay.getCalendar().getTimeInMillis());
        } else {
            view.loadTimeEntryFormActivity(eventDay);
        }
    }

    private void loadTimeEntriesFromDatabaseForDate(LifecycleOwner lifeCycleOwner, long dateInMilliSecs) {
        timeEntryDataBase.timeEntryDao().getAllEntriesFordDate(dateInMilliSecs).observe(lifeCycleOwner, timeEntries -> {

            view.showTimeEntryDetails(timeEntries);

        });

    }

    private void loadTimeEntriesFromDatabase(LifecycleOwner lifeCycleOwner) {
        timeEntryDataBase.timeEntryDao().getAllEntries().observe(lifeCycleOwner, timeEntries -> {
            events = new ArrayList<>();
            try {
                boolean isTimeEntryAvailableForToday = false;
                for (TimeEntry entry : timeEntries) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(entry.getDate());
                    EventDay eventDay = new EventDay(cal, R.drawable.baseline_notes_24);
                    events.add(eventDay);

                    if (checkTodayTimeEntries(entry)) {
                        view.setTodayEvent(eventDay);
                        isTimeEntryAvailableForToday = true;
                    }

                }

                view.setEventsOnCalendarView(events);
                if (isTimeEntryAvailableForToday) {
                    loadTimeEntriesFromDatabaseForDate(view, getTodayDateInMilliSecs());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private boolean checkTodayTimeEntries(TimeEntry timeEntry) {
        return timeEntry.getDate() == getTodayDateInMilliSecs();
    }

    private long getTodayDateInMilliSecs() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();

    }

}
