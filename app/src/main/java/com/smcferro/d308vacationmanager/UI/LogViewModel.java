package com.smcferro.d308vacationmanager.UI;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smcferro.d308vacationmanager.Repository.Repository;
import com.smcferro.d308vacationmanager.Entities.Log;
import java.util.List;

public class LogViewModel extends AndroidViewModel {
    private final Repository repository;
    private LiveData<List<Log>> allLogs;

    public LogViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allLogs = repository.getAllLogs();
    }

    public LiveData<List<Log>> getAllLogs() {
        return allLogs;
    }

    public void searchLogs(String vacationTitle) {
        allLogs = repository.searchLogs(vacationTitle);
    }
}
