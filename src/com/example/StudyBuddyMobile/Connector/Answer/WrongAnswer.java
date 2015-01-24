package com.example.StudyBuddyMobile.Connector.Answer;

public class WrongAnswer implements ServerAnswer {

    private String content;

    @Override
    public boolean authenticated() {
        return false;
    }

    @Override
    public String getContent() {
        return content;
    }

    public WrongAnswer(String content) {
        this.content = content;
    }
}
