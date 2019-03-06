package com.techno.timeentry.activities.timeentryformactivity;

import android.arch.lifecycle.LifecycleOwner;
import android.os.AsyncTask;

import com.techno.timeentry.application.TimeEntryApplication;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.models.TimeEntryDataBase;
import com.techno.timeentry.utils.Utils;

public class TimeEntryFormActivityPresenter implements TimeEntryFormActivityContract.Presenter {

    TimeEntryFormActivityContract.View view;

    TimeEntryDataBase timeEntryDataBase;
    LifecycleOwner lifecycleOwner;

    TimeEntryFormActivityPresenter(TimeEntryFormActivity view) {
        this.view = view;
        timeEntryDataBase = TimeEntryDataBase.getAppDatabase(TimeEntryApplication.getContext());
        this.lifecycleOwner = view;
    }

    @Override
    public void saveTimeEntryToDataBase(TimeEntry timeEntry, int mode) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (mode == Utils.MODE_UPDATE) {
                    timeEntryDataBase.timeEntryDao().updateTimeEntries(timeEntry);
                } else if (mode == Utils.MODE_ADD) {
                    timeEntryDataBase.timeEntryDao().insertTimeEntries(timeEntry);
                } else {
                    timeEntryDataBase.timeEntryDao().insertTimeEntries(timeEntry);
                }
            }
        });
    }

    @Override
    public void getTimeEntryFromDataBase(int id) {
        timeEntryDataBase.timeEntryDao().findEntryById(id).observe(lifecycleOwner, timeEntry -> {
            view.showDetailsOfTimeEntryFromDataBase(timeEntry);
        });
    }
}
