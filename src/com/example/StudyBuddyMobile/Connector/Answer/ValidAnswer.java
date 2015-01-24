package com.example.StudyBuddyMobile.Connector.Answer;

public class ValidAnswer implements ServerAnswer {

    private String content;

    @Override
    public boolean authenticated() {
        return true;
    }

    @Override
    public String getContent() {
        return content;
    }

    public ValidAnswer(String content) {
        this.content = content;
    }
}
