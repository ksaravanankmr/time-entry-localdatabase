package com.techno.timeentry.activities.timeentryformactivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.techno.timeentry.R;
import com.techno.timeentry.activities.mainactivity.MainActivity;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeEntryFormActivity extends AppCompatActivity implements TimeEntryFormActivityContract.View {

    @BindView(R.id.tv_date_label)
    TextView tvDateLabel;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_in_time_label)
    TextView tvInTimeLabel;
    @BindView(R.id.tv_in_time)
    TextView tvInTime;
    @BindView(R.id.tv_out_time_label)
    TextView tvOutTimeLabel;
    @BindView(R.id.tv_out_time)
    AppCompatTextView tvOutTime;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.btn_save)
    Button btnSave;

    long dateInMillSecs;
    long inTimeInMillSecs;
    long outTimeInMillSecs;
    int timeEntryActivityMode;

    TimePickerDialog timerDialog;
    TimeEntryFormActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_entry_form);
        ButterKnife.bind(this);

        presenter = new TimeEntryFormActivityPresenter(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            dateInMillSecs = getIntent().getLongExtra("date", 0);
            timeEntryActivityMode = getIntent().getIntExtra("mode", 0);
            tvDate.setText(Utils.getFormattedDateTime(dateInMillSecs, Utils.DATE_FORMAT));

            if (timeEntryActivityMode == Utils.MODE_UPDATE) {
                btnSave.setText(getString(R.string.text_update));
                presenter.getTimeEntryFromDataBase(dateInMillSecs);
            }
        }
    }

    @OnClick({R.id.tv_in_time, R.id.tv_out_time})
    void showTimePickerDialog(TextView textView) {
        int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        if (timerDialog == null || !timerDialog.isShowing()) {
            timerDialog = new TimePickerDialog(this, R.style.MyTimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(dateInMillSecs);
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

                    if (textView.getId() == R.id.tv_in_time) {
                        inTimeInMillSecs = cal.getTimeInMillis();
                    } else if (textView.getId() == R.id.tv_out_time) {
                        outTimeInMillSecs = cal.getTimeInMillis();
                    }

                    textView.setText(Utils.getFormattedDateTime(cal.getTimeInMillis(), Utils.TIME_FORMAT));
                }
            }, hr, min, true);
            timerDialog.show();
        }
    }

    @OnClick(R.id.btn_save)
    void saveOrUpdateTimeEntry() {
        if (inTimeInMillSecs <= 0) {
            Toast.makeText(this, getString(R.string.text_alert_in_time), Toast.LENGTH_SHORT).show();
        } else {
            TimeEntry entry = new TimeEntry(dateInMillSecs, inTimeInMillSecs, outTimeInMillSecs, etNote.getText().toString());
            presenter.saveTimeEntryToDataBase(entry, timeEntryActivityMode);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showTimeEntryDetails(TimeEntry timeEntry) {
        inTimeInMillSecs = timeEntry.getTimeIn();
        outTimeInMillSecs = timeEntry.getTimeOut();
        tvInTime.setText(Utils.getFormattedDateTime(inTimeInMillSecs, Utils.TIME_FORMAT));
        tvOutTime.setText(Utils.getFormattedDateTime(outTimeInMillSecs, Utils.TIME_FORMAT));
        etNote.setText(timeEntry.getNote());
    }
}
