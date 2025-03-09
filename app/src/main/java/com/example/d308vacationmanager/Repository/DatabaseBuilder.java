package com.example.d308vacationmanager.Repository;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.d308vacationmanager.DAO.ExcursionDAO;
import com.example.d308vacationmanager.DAO.LogDAO;
import com.example.d308vacationmanager.DAO.VacationDAO;
import com.example.d308vacationmanager.Entities.Excursion;
import com.example.d308vacationmanager.Entities.Log;
import com.example.d308vacationmanager.Entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class, Log.class}, version = 2, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    public abstract LogDAO LogDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "VacationDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}