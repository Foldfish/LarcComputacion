package com.example.alvmaria.larccomputacion.POJO;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Service {

    public String date;
    public String type;
    public String hour;

    public Service(String date, String hour, String type) {
        this.date = date;
        this.type = type;
        this.hour = hour;
    }

    public Service() {
    }

    String getDate() {
        return this.date;
    }

    String getType() {
        return this.type;
    }

    String getHour() { return this.hour; }

    void setDate(String date) {
        this.date = date;
    }

    void setType(String type) {
        this.type = type;
    }
    void setHour(String hour) {
        this.hour = hour;
    }
}
