package com.example.d308vacationmanager.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.d308vacationmanager.Entities.Log;

import java.util.List;

@Dao
public interface LogDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Log log);

    @Query("SELECT * FROM LOGS ORDER BY ID ASC")
    LiveData<List<Log>> getAllLogs();

    @Query("SELECT * FROM LOGS WHERE vacationTitle=:vacationtitle ORDER BY ID ASC")
    LiveData<List<Log>> searchLogs(String vacationtitle);
}