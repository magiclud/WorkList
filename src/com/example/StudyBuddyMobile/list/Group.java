package com.example.StudyBuddyMobile.list;

import com.example.StudyBuddyMobile.parser.models.Item;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String string;
    public final List<Item> children = new ArrayList<Item>();

    public Group(String string) {
        this.string = string;
    }

}