package com.techno.timeentry.activities.mainactivity;

import android.arch.lifecycle.LifecycleOwner;
import android.os.AsyncTask;

import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.R;
import com.techno.timeentry.application.TimeEntryApplication;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.models.TimeEntryDataBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    TimeEntryDataBase timeEntryDataBase;
    List<TimeEntry> timEntries;
    List<EventDay> events;

    EventDay selectedDate;

    MainActivityPresenter(MainActivity view) {
        this.view = view;

        timeEntryDataBase = TimeEntryDataBase.getAppDatabase(TimeEntryApplication.getContext());
        loadTimeEntriesFromDatabase(view);
    }

    @Override
    public void onDateClicked(EventDay eventDay) {
        selectedDate = eventDay;

        if (events.contains(eventDay)) {
            TimeEntry timeEntry = getTimeEntryFromEventDay(eventDay);
            view.showTimeEntryDetails(timeEntry);
        } else {
            view.loadTimeEntryFormActivity(eventDay);
        }
    }

    @Override
    public void onDeleteClicked() {
        TimeEntry timeEntry = getTimeEntryFromEventDay(selectedDate);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                timeEntryDataBase.timeEntryDao().delete(timeEntry);
            }
        });
    }

    private void loadTimeEntriesFromDatabase(LifecycleOwner lifeCycleOwner) {
        timeEntryDataBase.timeEntryDao().getAll().observe(lifeCycleOwner, timeEntries -> {
            events = new ArrayList<>();
            timEntries = timeEntries;
            try {

                for (TimeEntry entry : timeEntries) {
                    Calendar cal = Calendar.getInstance();

                    cal.setTimeInMillis(entry.getDate());
                    events.add(new EventDay(cal, R.drawable.baseline_notes_24));
                }

                view.setEventsOnCalendarView(events);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private TimeEntry getTimeEntryFromEventDay(EventDay eventDay) {
        int elementIndex = events.indexOf(eventDay);
        return timEntries.get(elementIndex);
    }

}
