package com.techno.timeentry.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TimeEntryDao {

    @Query("SELECT * FROM time_entry")
    LiveData<List<TimeEntry>> getAllEntries();

    @Query("SELECT * FROM time_entry where date LIKE :date")
    LiveData<List<TimeEntry>> getAllEntriesFordDate(long date);

    @Query("SELECT * FROM time_entry where id LIKE  :id")
    LiveData<TimeEntry> findEntryById(int id);

    @Query("SELECT COUNT(*) from time_entry")
    int countTimeEntries();

    @Insert(onConflict = REPLACE)
    void insertTimeEntries(TimeEntry... timeEntries);

    @Delete
    void delete(TimeEntry timeEntry);

    @Update(onConflict = REPLACE)
    void updateTimeEntries(TimeEntry... timeEntries);

}
