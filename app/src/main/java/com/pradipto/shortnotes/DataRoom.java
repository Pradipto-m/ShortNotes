package com.pradipto.shortnotes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class DataRoom extends RoomDatabase {

    private static DataRoom database;
    private static String Base_Name = "ShortNotes";

    public synchronized static DataRoom getInstance(Context context) {
        if( database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), DataRoom.class, Base_Name)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return database;
    }

    public abstract DaoBase daoBase();
}
