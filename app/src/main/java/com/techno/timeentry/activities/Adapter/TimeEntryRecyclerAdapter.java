package com.techno.timeentry.activities.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.techno.timeentry.R;
import com.techno.timeentry.activities.timeentryformactivity.TimeEntryFormActivity;
import com.techno.timeentry.application.TimeEntryApplication;
import com.techno.timeentry.models.TimeEntry;
import com.techno.timeentry.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TimeEntryRecyclerAdapter extends RecyclerView.Adapter<TimeEntryRecyclerAdapter.MyViewHolder> {

    Context context;
    TimeEntryRecyclerViewContract.TimeEntryRecyclerItemPresenter presenter;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements TimeEntryRecyclerViewContract.TimeEntryRecyclerItemView {
        @BindView(R.id.tv_date_info)
        TextView tvDateInfo;
        @BindView(R.id.tv_in_time_info)
        TextView tvInTimeInfo;
        @BindView(R.id.tv_out_time_info)
        TextView tvOutInfo;
        @BindView(R.id.tv_project_info)
        TextView tvProjectInfo;
        @BindView(R.id.tv_topic_info)
        TextView tvTopicInfo;
        @BindView(R.id.tv_time_spent_info)
        TextView tvSpentInfo;
        @BindView(R.id.tv_note_info)
        TextView tvNoteInfo;
        @BindView(R.id.ib_delete)
        ImageButton ibDelete;
        @BindView(R.id.ib_edit)
        ImageButton ibEdit;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void showDetails(TimeEntry timeEntry) {
            Context applicationContext = TimeEntryApplication.getContext();
            tvDateInfo.setText(Utils.getFormattedDateTime(timeEntry.getDate(), Utils.DATE_FORMAT));
            tvInTimeInfo.setText(applicationContext.getString(R.string.text_in_time_label) + Utils.getFormattedDateTime(timeEntry.getTimeIn(), Utils.TIME_FORMAT));
            tvOutInfo.setText(applicationContext.getString(R.string.text_out_time_label) + Utils.getFormattedDateTime(timeEntry.getTimeOut(), Utils.TIME_FORMAT));
            tvProjectInfo.setText(applicationContext.getString(R.string.text_project_label) + getSelectedProjectFromTopic(timeEntry.getTopic()));
            tvTopicInfo.setText(applicationContext.getString(R.string.text_topic_label) + timeEntry.getTopic());

            if (timeEntry.getNote() == null || timeEntry.getNote().isEmpty()) {
                tvNoteInfo.setVisibility(View.GONE);
            } else {
                tvNoteInfo.setVisibility(View.VISIBLE);
                tvNoteInfo.setText(applicationContext.getString(R.string.text_note_label) + " : " + String.valueOf(timeEntry.getNote()));
            }

            long timeSpent = Utils.getTimeSpent(timeEntry.getTimeIn(), timeEntry.getTimeOut());
            if (timeSpent > 0) {
                tvSpentInfo.setVisibility(View.VISIBLE);
                tvSpentInfo.setText(applicationContext.getString(R.string.text_time_spent_label) + Utils.getFormattedTimeSpent(timeSpent));
            } else {
                tvSpentInfo.setVisibility(View.GONE);
            }
        }

        @Override
        public void loadTimeEntryActivity(TimeEntry timeEntry, EventDay eventDay) {
            Intent intent = new Intent(TimeEntryApplication.getContext(), TimeEntryFormActivity.class);
            intent.putExtra("mode", Utils.MODE_UPDATE);
            intent.putExtra("date", eventDay.getCalendar().getTimeInMillis());
            intent.putExtra("entry_id", timeEntry.getId());
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            TimeEntryApplication.getContext().startActivity(intent);
        }
    }

    public TimeEntryRecyclerAdapter(Context context, TimeEntryRecyclerViewContract.TimeEntryRecyclerItemPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public TimeEntryRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_entry, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        presenter.onBindTimeEntryItemAtPosition(position, holder);

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
                builder.setTitle(context.getString(R.string.text_alert_delete_title))
                        .setMessage(context.getString(R.string.text_alert_delete_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.onDeleteClicked(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();


            }
        });

        holder.ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEditClicked(position, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return presenter.getTimeEntriesCount();
    }

    static String getSelectedProjectFromTopic(String selectedTopic) {
        for (int i = 0; i < 3; i++) {

            int topicArrayResId = R.array.bluetooth_array;

            if (i == 0) {
                topicArrayResId = R.array.bluetooth_array;
            } else if (i == 1) {
                topicArrayResId = R.array.biometric_array;
            } else if (i == 2) {
                topicArrayResId = R.array.sensor_array;
            }

            ArrayList<String> topicArray = new ArrayList<>(Arrays.asList(TimeEntryApplication.getContext().getResources().getStringArray(topicArrayResId)));
            if (topicArray.contains(selectedTopic)) {
                ArrayList<String> projectArray = new ArrayList<>(Arrays.asList(TimeEntryApplication.getContext().getResources().getStringArray(R.array.projects_array)));
                return projectArray.get(i);
            }
        }
        return "";
    }


}