package com.smcferro.d308vacationmanager.UI;

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

import androidx.appcompat.app.AppCompatActivity;

import com.smcferro.d308vacationmanager.Entities.Excursion;
import com.smcferro.d308vacationmanager.Entities.Vacation;
import com.smcferro.d308vacationmanager.R;
import com.smcferro.d308vacationmanager.Repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ExcursionDetails extends AppCompatActivity {
    String title;
    int excursionID;
    int vacationID;
    EditText editTitle;
    public Repository repository;
    Excursion currentExcursion;
    TextView editExcursionDate;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar myCalendarDate = Calendar.getInstance();

    String setDate;

    Random rand = new Random();
    int numAlert = rand.nextInt(99999);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());
        title = getIntent().getStringExtra("title");
        editTitle = findViewById(R.id.excursionTitle);
        editTitle.setText(title);
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        setDate = getIntent().getStringExtra("excursionDate");
        numAlert = rand.nextInt(99999);

        //sets up date info
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (setDate != null) { /// NULL VALIDATION
            try {
                Date excursionDate = sdf.parse(setDate);
                myCalendarDate.setTime(excursionDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editExcursionDate = findViewById(R.id.excursionDate);

        editExcursionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = editExcursionDate.getText().toString();
                if (info.equals("")) info = setDate;
                try {
                    myCalendarDate.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, excursionDate, myCalendarDate
                        .get(Calendar.YEAR), myCalendarDate.get(Calendar.MONTH),
                        myCalendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        excursionDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarDate.set(Calendar.YEAR, year);
                myCalendarDate.set(Calendar.MONTH, month);
                myCalendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editExcursionDate.setText(sdf.format(myCalendarDate.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            List<Vacation> vacations = repository.getVacations();
            String excursionDateString = sdf.format(myCalendarDate.getTime());
            Vacation vacation = null;

            for (Vacation vac : vacations) {
                if (vac.getVacationId() == vacationID) {
                    vacation = vac;
                }
            }

            try {

                Date excursionDate = sdf.parse(excursionDateString);
                Date endDate = sdf.parse(vacation.getEndDate());
                Date startDate = sdf.parse(vacation.getStartDate());

                if (excursionDate.before(startDate) || excursionDate.after(endDate)) { // makes sure chosen date falls into vacation date range
                    Toast.makeText(this, "Excursion Date must be within the Vacation's Start and End dates", Toast.LENGTH_LONG).show(); /// DATE RANGE VALIDATION
                    return true;
                } else {
                    Excursion excursion;
                    if (excursionID == -1) {
                        if (repository.getExcursions().size() == 0) excursionID = 1;
                        else excursionID = repository.getExcursions().get(repository.getExcursions().size() - 1).getExcursionID() + 1;
                        excursion = new Excursion(excursionID, editTitle.getText().toString(), vacationID, excursionDateString);
                        repository.insert(excursion);

                        this.finish();
                    } else {
                        excursion = new Excursion(excursionID, editTitle.getText().toString(), vacationID, excursionDateString);
                        repository.update(excursion);

                        this.finish();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }

        if (item.getItemId() == R.id.excursiondelete) {
            for (Excursion excursion : repository.getExcursions()) {
                if (excursion.getExcursionID() == excursionID) currentExcursion = excursion;
            }
            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionTitle() + " was deleted", Toast.LENGTH_LONG).show();
            ExcursionDetails.this.finish();
        }

        if (item.getItemId() == R.id.excursionalert) {
            String dateFromScreen = editExcursionDate.getText().toString();
            String alert = "Excursion " + title + " is today";

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", alert);
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            numAlert = rand.nextInt(99999);
            System.out.println("numAlert Excursion = " + numAlert);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLabel();
    }

    public boolean isExcursionDateValid(String excursionDateString, Vacation vacation) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date excursionDate = sdf.parse(excursionDateString);
            Date startDate = sdf.parse(vacation.getStartDate());
            Date endDate = sdf.parse(vacation.getEndDate());

            return !excursionDate.before(startDate) && !excursionDate.after(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}