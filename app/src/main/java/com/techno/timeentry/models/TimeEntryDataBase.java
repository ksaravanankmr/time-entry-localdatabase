package com.techno.timeentry.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {TimeEntry.class}, version = 1, exportSchema = false)
public abstract class TimeEntryDataBase extends RoomDatabase {
    private static TimeEntryDataBase INSTANCE;

    public abstract TimeEntryDao timeEntryDao();

    public static TimeEntryDataBase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), TimeEntryDataBase.class, "time_entry_database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
