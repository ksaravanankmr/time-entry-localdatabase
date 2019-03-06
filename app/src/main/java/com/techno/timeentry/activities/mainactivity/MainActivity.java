package com.techno.timeentry.activities.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.R;
import com.techno.timeentry.activities.Adapter.TimeEntryRecyclerAdapter;
import com.techno.timeentry.activities.Adapter.TimeEntryRecyclerViewPresenter;
import com.techno.timeentry.activities.timeentryformactivity.TimeEntryFormActivity;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.rl_time_entry_details)
    RelativeLayout rlTimeEntryDetails;
    @BindView(R.id.rv_time_entries)
    RecyclerView rvTimeEntries;

    TimeEntryRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;

    MainActivityPresenter presenter;
    TimeEntryRecyclerViewPresenter recyclerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);
        recyclerPresenter = new TimeEntryRecyclerViewPresenter();

        rvTimeEntries.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvTimeEntries.setLayoutManager(layoutManager);
        adapter = new TimeEntryRecyclerAdapter(this, recyclerPresenter);
        rvTimeEntries.setAdapter(adapter);
    }

    @Override
    public void setEventsOnCalendarView(List<EventDay> events) {
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(eventDay -> {
            recyclerPresenter.selectedDate = eventDay;
            presenter.onDateClicked(eventDay);
        });
    }

    @Override
    public void showTimeEntryDetails(List<TimeEntry> timeEntries) {

        recyclerPresenter.timeEntries = timeEntries;
        adapter.notifyDataSetChanged();

        if (recyclerPresenter.timeEntries.size() > 0)
            rlTimeEntryDetails.setVisibility(View.VISIBLE);
        else
            rlTimeEntryDetails.setVisibility(View.GONE);
    }

    @Override
    public void loadTimeEntryFormActivity(EventDay eventDay) {
        rlTimeEntryDetails.setVisibility(View.GONE);

        Intent intent = new Intent(this, TimeEntryFormActivity.class);
        intent.putExtra("mode", Utils.MODE_INSERT);
        intent.putExtra("date", eventDay.getCalendar().getTimeInMillis());
        startActivity(intent);
    }

    @OnClick(R.id.ib_add)
    void addButtonClicked() {
        Intent intent = new Intent(this, TimeEntryFormActivity.class);
        intent.putExtra("mode", Utils.MODE_ADD);
        intent.putExtra("date", recyclerPresenter.selectedDate.getCalendar().getTimeInMillis());
        startActivity(intent);
    }

    @Override
    public void setTodayEvent(EventDay eventDay) {
        recyclerPresenter.selectedDate = eventDay;
    }
}
