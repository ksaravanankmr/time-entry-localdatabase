package com.techno.timeentry.activities.Adapter;

import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.models.TimeEntry;

public interface TimeEntryRecyclerViewContract {

    interface TimeEntryRecyclerItemView{
        void showDetails(TimeEntry timeEntry);
        void loadTimeEntryActivity(TimeEntry timeEntry, EventDay eventDay);
    }

    interface TimeEntryRecyclerItemPresenter{
        void onBindTimeEntryItemAtPosition(int position,TimeEntryRecyclerItemView view);
        int getTimeEntriesCount();
        void onDeleteClicked(int position);
        void onEditClicked(int position, TimeEntryRecyclerItemView view);
    }
}
