package com.example.StudyBuddyMobile.parser.models;

import org.simpleframework.xml.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@org.simpleframework.xml.Root
public class Item {

    @Attribute
    private String name;

    @Attribute
    private int number;

    @Element
    private String url;

    @Element
    private String deadline;

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getUrl() {
        return url.trim();
    }

    public String getDeadline() {
        return deadline;
    }

    public Date getDeadlineDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-hh:mm");
        try {
            return dateFormat.parse(deadline);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public Item() {
    }

    public Item(String name, int number, String url, String deadline) {
        this.name = name;
        this.number = number;
        this.url = url;
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }
}
