package com.smcferro.d308vacationmanager.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logs")
public class Log {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String action;
    private String vacationTitle;
    private String logDate;

    public Log(String action, String vacationTitle, String logDate) {
        this.action = action;
        this.vacationTitle = vacationTitle;
        this.logDate = logDate;
    }

    public int getID() {return ID;}

    public void setID(int excursionID) {
        this.ID = excursionID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String LogDate) {
        this.logDate = LogDate;
    }

}
