package com.smcferro.d308vacationmanager.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import com.smcferro.d308vacationmanager.Entities.Excursion;
import com.smcferro.d308vacationmanager.Entities.Vacation;
import com.smcferro.d308vacationmanager.R;
import com.smcferro.d308vacationmanager.Repository.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VacationList extends AppCompatActivity {
    private Repository repository;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public final ActivityResultLauncher<Intent> vacationLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            refreshVacationList(); // Update RecyclerView
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            vacationLauncher.launch(intent);
        });

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        // adds list of vacations to activity
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshVacationList();
    }

    public void refreshVacationList() {
        List<Vacation> allVacations = repository.getVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        VacationAdapter vacationAdapter = (VacationAdapter) recyclerView.getAdapter();
        if (vacationAdapter != null) {
            vacationAdapter.setVacations(allVacations);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // goes back to home
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        // add sample data to db
        if (item.getItemId() == R.id.mysample) {
            // vacations
            repository = new Repository(getApplication());
            Vacation vacation = new Vacation(0, "Australia", "Hyatt", "06/09/25", "06/17/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Greece", "Best Western", "06/08/25", "06/15/25");
            repository.insert(vacation);
            vacation = new Vacation(0, "Japan", "Hilton", "06/09/25", "06/16/25");
            repository.insert(vacation);


            // excursions
            Excursion excursion = new Excursion(0, "Hiking Tour", 1, "06/09/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Ghost Tour", 3, "06/12/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Snorkeling", 1, "06/14/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "City Walking Tour", 2, "06/09/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "History Museum", 2, "06/10/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Beach Day", 2, "06/11/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Horseback Riding", 1, "06/09/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Hot Springs", 3, "06/12/25");
            repository.insert(excursion);
            excursion = new Excursion(0, "Mount Fuji Day Trip", 3, "06/13/25");
            repository.insert(excursion);


            recreate();

            return true;
        } else if (item.getItemId() == R.id.deletedata) {
            repository.clearAllData();

            refreshVacationList();

            return true;
        } else if (item.getItemId() == R.id.logreports){
            Intent intent = new Intent(this, LogActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
