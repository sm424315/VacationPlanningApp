package com.smcferro.d308vacationmanager.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smcferro.d308vacationmanager.Entities.Log;
import com.smcferro.d308vacationmanager.R;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private LogViewModel logViewModel;
    private LogAdapter logAdapter;
    private List<Log> allLogs = new ArrayList<>();
    private List<Log> filteredLogs = new ArrayList<>();
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_report);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLogs);
        searchInput = findViewById(R.id.searchLogs);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logAdapter = new LogAdapter(filteredLogs);
        recyclerView.setAdapter(logAdapter);

        // Set up ViewModel
        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        logViewModel.getAllLogs().observe(this, logs -> {
            allLogs.clear();
            allLogs.addAll(logs);
            filterLogs(""); // Display all logs initially
        });

        // Search functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLogs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Filter logs by vacationTitle
    private void filterLogs(String query) {
        filteredLogs.clear();
        for (Log log : allLogs) {
            if (log.getVacationTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredLogs.add(log);
            }
        }
        logAdapter.notifyDataSetChanged();
    }
}
