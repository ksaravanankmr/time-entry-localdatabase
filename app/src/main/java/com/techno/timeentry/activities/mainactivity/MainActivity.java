package com.techno.timeentry.activities.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.R;
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
    @BindView(R.id.tv_in_time_info)
    TextView tvInTimeInfo;
    @BindView(R.id.tv_out_time_info)
    TextView tvOutInfo;
    @BindView(R.id.tv_note_info)
    TextView tvNoteInfo;
    @BindView(R.id.tv_date_info)
    TextView tvDateInfo;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.ib_edit)
    ImageButton ibEdit;

    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainActivityPresenter(this);
    }

    @OnClick(R.id.ib_delete)
    void deleteTimeEntry() {
        presenter.onDeleteClicked();
        rlTimeEntryDetails.setVisibility(View.GONE);
    }

    @OnClick(R.id.ib_edit)
    void editTimeEntry() {
        Intent intent = new Intent(this, TimeEntryFormActivity.class);
        intent.putExtra("mode", Utils.MODE_UPDATE);
        intent.putExtra("date", presenter.selectedDate.getCalendar().getTimeInMillis());
        startActivity(intent);
    }


    @Override
    public void setEventsOnCalendarView(List<EventDay> events) {
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(eventDay -> {
            presenter.onDateClicked(eventDay);
        });
    }

    @Override
    public void showTimeEntryDetails(TimeEntry timeEntry) {
        tvDateInfo.setText(Utils.getFormattedDateTime(timeEntry.getTimeIn(), "EEE," + Utils.DATE_FORMAT));
        tvInTimeInfo.setText(getString(R.string.text_in_time_label) + Utils.getFormattedDateTime(timeEntry.getTimeIn(), Utils.TIME_FORMAT));
        tvOutInfo.setText(getString(R.string.text_out_time_label) + Utils.getFormattedDateTime(timeEntry.getTimeOut(), Utils.TIME_FORMAT));
        tvNoteInfo.setText(getString(R.string.text_note_label) + " : " + timeEntry.getNote());

        rlTimeEntryDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadTimeEntryFormActivity(EventDay eventDay) {
        rlTimeEntryDetails.setVisibility(View.GONE);

        Intent intent = new Intent(this, TimeEntryFormActivity.class);
        intent.putExtra("mode", Utils.MODE_INSERT);
        intent.putExtra("date", eventDay.getCalendar().getTimeInMillis());
        startActivity(intent);
    }
}
