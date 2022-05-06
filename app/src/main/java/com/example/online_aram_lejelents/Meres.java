package com.example.online_aram_lejelents;

import androidx.annotation.NonNull;

public class Meres {
    private String serial;
    private String allas;
    private String date;

    public Meres(String serial, String allas, String date) {
        this.serial = serial;
        this.allas = allas;
        this.date = date;
    }

    public String getSerial() {
        return serial;
    }

    public String getAllas() {
        return allas;
    }

    @NonNull
    @Override
    public String toString() {
        return "serial: " + serial + ", allas: " + allas + ", Date: "+date+"\n";
    }

    public String getDate() {
        return date;
    }
}
