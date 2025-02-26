package com.example.d308vacationmanager.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationmanager.Entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("DELETE FROM vacations")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name='vacations'")
    void resetVacationsSequence();

    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();
}