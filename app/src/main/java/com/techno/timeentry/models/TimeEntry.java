package com.techno.timeentry.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "time_entry")
public class TimeEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "time_in")
    private long timeIn;
    @ColumnInfo(name = "time_out")
    private long timeOut;
    @ColumnInfo(name = "topic")
    private String topic;
    @ColumnInfo(name = "note")
    private String note;

    public TimeEntry(int id, long date, long timeIn, long timeOut, String topic, String note) {
        this.id = id;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.topic = topic;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(long timeIn) {
        this.timeIn = timeIn;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
