package com.techno.timeentry.activities.timeentryformactivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.techno.timeentry.R;
import com.techno.timeentry.activities.mainactivity.MainActivity;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeEntryFormActivity extends AppCompatActivity implements TimeEntryFormActivityContract.View, AdapterView.OnItemSelectedListener {

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
    @BindView(R.id.spinner_project)
    Spinner spinnerProject;
    @BindView(R.id.spinner_topic)
    Spinner spinnerTopic;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.btn_save)
    Button btnSave;

    long dateInMillSecs;
    long inTimeInMillSecs;
    long outTimeInMillSecs;
    int timeEntryActivityMode;

    int entryId;

    TimePickerDialog timerDialog;
    TimeEntryFormActivityPresenter presenter;

    boolean isFirstTimeSpinnerSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_entry_form);
        ButterKnife.bind(this);

        presenter = new TimeEntryFormActivityPresenter(this);
        initializeUI();
        getModeOfForm();

    }

    private void getModeOfForm() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            timeEntryActivityMode = getIntent().getIntExtra("mode", 0);
            dateInMillSecs = getIntent().getLongExtra("date", 0);
            tvDate.setText(Utils.getFormattedDateTime(dateInMillSecs, Utils.DATE_FORMAT));

            if (timeEntryActivityMode == Utils.MODE_UPDATE && getIntent().hasExtra("entry_id")) {
                entryId = getIntent().getIntExtra("entry_id", 0);
                btnSave.setText(getString(R.string.text_update));
                presenter.getTimeEntryFromDataBase(entryId);
            }
        }
    }

    private void initializeUI() {

        setAdapter(spinnerProject, R.array.projects_array);
        setAdapterForTopicSpinner(R.array.bluetooth_array);

        spinnerProject.setOnItemSelectedListener(this);
        spinnerTopic.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_project:

                if (!isFirstTimeSpinnerSelected) {
                    String selectedItem = spinnerProject.getSelectedItem().toString();
                    setAdapterForTopicSpinner(selectedItem);
                } else
                    isFirstTimeSpinnerSelected = false;

                break;

            case R.id.spinner_topic:

                break;
        }
    }


    private void setAdapter(Spinner spinner, int stringArrayResId) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                stringArrayResId, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setSelectedItemForSpinner(Spinner spinner, int index) {
        spinner.setSelection(index);
    }

    private void setAdapterForTopicSpinner(String project) {

        int topicArrayId = getTopicArrayForCorrespondingProject(project);
        setAdapterForTopicSpinner(topicArrayId);
    }

    private void setAdapterForTopicSpinner(int topicArrayId) {
        setAdapter(spinnerTopic, topicArrayId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        } else if (spinnerProject.getSelectedItem() == null) {
            Toast.makeText(this, getString(R.string.text_alert_project), Toast.LENGTH_SHORT).show();
        } else if (spinnerTopic.getSelectedItem() == null) {
            Toast.makeText(this, getString(R.string.text_alert_topic), Toast.LENGTH_SHORT).show();
        } else if (outTimeInMillSecs > 0 && outTimeInMillSecs <= inTimeInMillSecs) {
            Toast.makeText(this, getString(R.string.text_alert_out_time), Toast.LENGTH_SHORT).show();
        } else {
            TimeEntry entry = new TimeEntry(entryId, dateInMillSecs, inTimeInMillSecs, outTimeInMillSecs, spinnerTopic.getSelectedItem().toString(), etNote.getText().toString());
            presenter.saveTimeEntryToDataBase(entry, timeEntryActivityMode);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void showDetailsOfTimeEntryFromDataBase(TimeEntry timeEntry) {
        inTimeInMillSecs = timeEntry.getTimeIn();
        outTimeInMillSecs = timeEntry.getTimeOut();
        tvInTime.setText(Utils.getFormattedDateTime(inTimeInMillSecs, Utils.TIME_FORMAT));
        tvOutTime.setText(Utils.getFormattedDateTime(outTimeInMillSecs, Utils.TIME_FORMAT));
        setSpinnerDataForTopicDetail(timeEntry.getTopic());
        etNote.setText(timeEntry.getNote());
    }

    private int getTopicArrayForCorrespondingProject(String project) {
        int topicArrayResId = R.array.bluetooth_array;
        if (project.equals("Bluetooth")) {
            topicArrayResId = R.array.bluetooth_array;
        } else if (project.equals("Biometric Voting Machine")) {
            topicArrayResId = R.array.biometric_array;
        } else if (project.equals("Sensor technology")) {
            topicArrayResId = R.array.sensor_array;
        }

        return topicArrayResId;
    }

    private void setSpinnerDataForTopicDetail(String selectedTopic) {
        for (int i = 0; i < 3; i++) {

            int topicArrayResId = R.array.bluetooth_array;

            if (i == 0) {
                topicArrayResId = R.array.bluetooth_array;
            } else if (i == 1) {
                topicArrayResId = R.array.biometric_array;
            } else if (i == 2) {
                topicArrayResId = R.array.sensor_array;
            }

            ArrayList<String> topicArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(topicArrayResId)));
            if (topicArray.contains(selectedTopic)) {
                setSelectedItemForSpinner(spinnerProject, i);

                setAdapterForTopicSpinner(topicArrayResId);
                setSelectedItemForSpinner(spinnerTopic, topicArray.indexOf(selectedTopic));

                break;
            }
        }
    }
}
