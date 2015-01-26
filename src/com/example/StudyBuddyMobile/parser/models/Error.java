package com.example.StudyBuddyMobile.parser.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Error {

    @Attribute
    private int id;

    @Element
    private String message;

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Error() { }

    public Error(int id, String message) {
        this.id = id;
        this.message = message;
    }

}
