package com.example.StudyBuddyMobile.parser.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@org.simpleframework.xml.Root(name = "list")
public class Root {

    @Element
    private Error error;

    @ElementList
    private List<Item> items;

    public Error getError() {
        return error;
    }

    public List getItems() {
        return items;
    }

}
