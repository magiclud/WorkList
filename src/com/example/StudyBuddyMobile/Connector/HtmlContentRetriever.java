package com.example.StudyBuddyMobile.Connector;


import com.example.StudyBuddyMobile.Connector.Answer.AnswerBuilder;
import com.example.StudyBuddyMobile.Connector.Answer.ServerAnswer;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;

public enum HtmlContentRetriever {

    INSTANCE;

    private static final String linkFormat = "https://10.0.2.2:9443/%s/%s/xml";

    public ServerAnswer checkCredentials(String login, String password) throws IOException {

        URL url = new URL(String.format(linkFormat, login, password));
        CertificateValidator.trustAllHosts();
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setHostnameVerifier(CertificateValidator.DO_NOT_VERIFY);

        AnswerBuilder answerBuilder = new AnswerBuilder();
        answerBuilder.setServerAnswer(httpsURLConnection);
        return answerBuilder.getServerAnswer();
    }
}
