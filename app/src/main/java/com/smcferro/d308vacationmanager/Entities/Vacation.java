package com.smcferro.d308vacationmanager.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Entity(tableName = "vacations")
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    public int vacationId;
    private String vacationTitle;
    private String vacationHotel;
    private String startDate;
    private String endDate;

    public Vacation(int vacationId, String vacationTitle, String vacationHotel, String startDate, String endDate) {
        this.vacationId = vacationId;
        this.vacationTitle = vacationTitle;
        this.vacationHotel = vacationHotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String toString() {
        return vacationTitle;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getVacationHotel() {
        return vacationHotel;
    }

    public void setVacationHotel(String vacationHotel) {
        this.vacationHotel = vacationHotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isValid() {
        return isNotEmpty(vacationTitle) && isNotEmpty(vacationHotel) &&
                isValidDate(startDate) && isValidDate(endDate) &&
                isStartDateBeforeEndDate();
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false); // Strict date validation
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isStartDateBeforeEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            if (startDate == null || endDate == null) {
                return false;
            }
            return sdf.parse(startDate).before(sdf.parse(endDate));
        } catch (ParseException e) {
            return false;
        }
    }

}