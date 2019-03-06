package com.techno.timeentry.activities.mainactivity;

import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.models.TimeEntry;

import java.util.List;

public interface MainActivityContract {

    interface Presenter {
        void onDateClicked(EventDay eventDay);
    }

    interface View {

        void setEventsOnCalendarView(List<EventDay> events);

        void showTimeEntryDetails(List<TimeEntry> timeEntries);

        void loadTimeEntryFormActivity(EventDay eventDay);

    }
}
