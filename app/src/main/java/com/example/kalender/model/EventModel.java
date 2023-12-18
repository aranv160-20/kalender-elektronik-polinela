package com.example.kalender.model;

public class EventModel {
    private long _id; // Sesuaikan dengan perubahan ID
    private String date;
    private String description;

    public EventModel(long _id, String date, String description) {
        this._id = _id;
        this.date = date;
        this.description = description;
    }

    public long getId() {
        return _id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}