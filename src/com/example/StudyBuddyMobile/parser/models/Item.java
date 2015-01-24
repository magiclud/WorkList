package com.example.StudyBuddyMobile.parser.models;

import org.simpleframework.xml.*;

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
        return url;
    }

    public String getDeadline() {
        return deadline;
    }

    public Item() { }

    public Item(String name, int number, String url, String deadline) {
        this.name = name;
        this.number = number;
        this.url = url;
        this.deadline = deadline;
    }
}
