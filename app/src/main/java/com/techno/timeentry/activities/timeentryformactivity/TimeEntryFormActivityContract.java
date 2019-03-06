package com.techno.timeentry.activities.timeentryformactivity;

import com.techno.timeentry.models.TimeEntry;

public interface TimeEntryFormActivityContract {
    interface Presenter {
        void saveTimeEntryToDataBase(TimeEntry timeEntry, int mode);

        void getTimeEntryFromDataBase(int id);
    }

    interface View {
        void showDetailsOfTimeEntryFromDataBase(TimeEntry timeEntry);
    }
}
