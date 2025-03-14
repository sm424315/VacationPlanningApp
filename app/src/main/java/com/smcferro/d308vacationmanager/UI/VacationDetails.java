package com.smcferro.d308vacationmanager.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smcferro.d308vacationmanager.Entities.Excursion;
import com.smcferro.d308vacationmanager.Entities.Vacation;
import com.smcferro.d308vacationmanager.Entities.Log;

import com.smcferro.d308vacationmanager.R;
import com.smcferro.d308vacationmanager.Repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VacationDetails extends AppCompatActivity {

    String title;
    String hotel;
    int vacationID;
    String setStartDate;
    String setEndDate;
    public EditText editTitle;
    public EditText editHotel;
    public TextView editStartDate;
    public TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    public final Calendar myCalendarStart = Calendar.getInstance();
    public final Calendar myCalendarEnd = Calendar.getInstance();
    List<Excursion> filteredExcursions = new ArrayList<>();
    Random rand = new Random();
    public int numAlert = rand.nextInt(99999);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        setStartDate = getIntent().getStringExtra("startdate");
        setEndDate = getIntent().getStringExtra("enddate");
        editTitle.setText(title);
        editHotel.setText(hotel);
        numAlert = rand.nextInt(99999);

        // go to excursion details
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Excursion e: repository.getExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setmExcursions(filteredExcursions);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (setStartDate != null) {
            try {
                Date startDate = sdf.parse(setStartDate);
                Date endDate = sdf.parse(setEndDate);
                myCalendarStart.setTime(startDate);
                myCalendarEnd.setTime(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editStartDate.getText().toString();
                if (info.equals("")) info = setStartDate;
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editEndDate.getText().toString();
                if (info.equals("")) info = setEndDate;
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };
    }

    public void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.vacationsave) {
            Vacation vacation;

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String startDateString = sdf.format(myCalendarStart.getTime());
            String endDateString = sdf.format(myCalendarEnd.getTime());


            try {
                Date startDate = sdf.parse(startDateString);
                Date endDate = sdf.parse(endDateString);

                if (vacationID == -1) {
                    vacation = new Vacation(0, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
                    if (vacation.isValid()){
                        repository.insert(vacation);
                        repository.insert(new Log("Created", vacation.getVacationTitle(), LocalDateTime.now().toString()));
                    }

                    finish();

                } else {
                    vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateString, endDateString);
                    if (vacation.isValid()) {
                        repository.update(vacation);
                        repository.insert(new Log("Updated", vacation.getVacationTitle(), LocalDateTime.now().toString()));
                    }
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vac : repository.getVacations()) {
                if (vac.getVacationId() == vacationID) currentVacation = vac;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " deleted", Toast.LENGTH_LONG).show();
                repository.insert(new Log("Deleted", currentVacation.getVacationTitle(), LocalDateTime.now().toString()));
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show(); /// DELETION VALIDATION
            }
        }

        if (item.getItemId() == R.id.alertstart) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + title + " is starting";
            alertPicker(dateFromScreen, alert);

            return true;
        }

        if (item.getItemId() == R.id.alertend) {
            String dateFromScreen = editEndDate.getText().toString();
            String alert = "Vacation " + title + " is ending";
            alertPicker(dateFromScreen, alert);

            return true;
        }

        if (item.getItemId() == R.id.alertfull) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + title + " is starting";
            alertPicker(dateFromScreen, alert);
            dateFromScreen = editEndDate.getText().toString();
            alert = "Vacation " + title + " is ending";
            alertPicker(dateFromScreen, alert);

            return true;
        }


        if (item.getItemId() == R.id.share) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Shared");
            StringBuilder shareData = new StringBuilder();
            shareData.append("Vacation title: " + editTitle.getText().toString() + "\n");
            shareData.append("Hotel name: " + editHotel.getText().toString() + "\n");
            shareData.append("Start Date: " + editStartDate.getText().toString() + "\n");
            shareData.append("End Date: " + editEndDate.getText().toString() + "\n");
            for (int i = 0; i < filteredExcursions.size(); i++) {
                shareData.append("Excursion " + (i + 1) + ": " + filteredExcursions.get(i).getExcursionTitle() + "\n");
                shareData.append("Excursion " + (i + 1) + " Date: " + filteredExcursions.get(i).getExcursionDate() + "\n");
            }
            sentIntent.putExtra(Intent.EXTRA_TEXT, shareData.toString());
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }

        return true;
    }

    public void alertPicker(String dateFromScreen, String alert) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = myDate.getTime();
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", alert);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        numAlert = rand.nextInt(99999);
        System.out.println("numAlert Vacation = " + numAlert);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e: repository.getExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setmExcursions(filteredExcursions);

        updateLabelStart();
        updateLabelEnd();
    }
}