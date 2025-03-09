package com.example.d308vacationmanager.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.d308vacationmanager.DAO.ExcursionDAO;
import com.example.d308vacationmanager.DAO.LogDAO;
import com.example.d308vacationmanager.DAO.VacationDAO;
import com.example.d308vacationmanager.Entities.Excursion;
import com.example.d308vacationmanager.Entities.Log;
import com.example.d308vacationmanager.Entities.Vacation;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;
    private final LogDAO mLogDAO;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;
    private List<Log> mAllLogs;


    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
        mLogDAO = db.LogDAO();

    }
    public VacationDAO getmVacationDAO(){
        return mVacationDAO;
    }
    ////// vacations

    public List<Vacation> getVacations() {
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllVacations;
    }

    public void insert (Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    ////// excursions

    public List<Excursion> getExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID) {
        return null;
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    ///// clear DB
    public void clearAllData() {
        databaseExecutor.execute(() -> {
            mVacationDAO.deleteAll();

            mExcursionDAO.deleteAll();

            mVacationDAO.resetVacationsSequence();

            mExcursionDAO.resetExcursionsSequence();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    ///// Log Insertion
    public void insert(Log log) {
        databaseExecutor.execute(() -> {
            mLogDAO.insert(log);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<List<Log>> getAllLogs() {
        return (LiveData<List<Log>>) mLogDAO.getAllLogs(); // Room automatically runs LiveData queries on a background thread.
    }


    public LiveData<List<Log>> searchLogs(String keyword) {
        databaseExecutor.execute(() -> {
            mAllLogs = (List<Log>) mLogDAO.searchLogs(keyword);
        });
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
        return (LiveData<List<Log>>) mAllLogs;
    }
}
