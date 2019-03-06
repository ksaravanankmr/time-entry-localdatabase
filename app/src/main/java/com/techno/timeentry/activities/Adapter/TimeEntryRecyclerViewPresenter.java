package com.techno.timeentry.activities.Adapter;

import android.os.AsyncTask;

import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.application.TimeEntryApplication;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.models.TimeEntryDataBase;

import java.util.List;

public class TimeEntryRecyclerViewPresenter implements TimeEntryRecyclerViewContract.TimeEntryRecyclerItemPresenter {
    public List<TimeEntry> timeEntries;
    public EventDay selectedDate;

    private TimeEntryDataBase timeEntryDataBase;
    @Override
    public void onBindTimeEntryItemAtPosition(int position, TimeEntryRecyclerViewContract.TimeEntryRecyclerItemView view) {
        view.showDetails(timeEntries.get(position));
        timeEntryDataBase = TimeEntryDataBase.getAppDatabase(TimeEntryApplication.getContext());
    }

    @Override
    public int getTimeEntriesCount() {
        return timeEntries.size();
    }

    @Override
    public void onDeleteClicked(int position) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                timeEntryDataBase.timeEntryDao().delete(timeEntries.get(position));
            }
        });
    }

    @Override
    public void onEditClicked(int position, TimeEntryRecyclerViewContract.TimeEntryRecyclerItemView view) {
        view.loadTimeEntryActivity(timeEntries.get(position), selectedDate);
    }
}
