package com.example.kalender.model;

public class EventModel {
    private long id;
    private String date;
    private String description;

    public EventModel(long id, String date, String description) {
        this.id = id;
        this.date = date;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
