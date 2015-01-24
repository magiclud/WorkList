package com.example.StudyBuddyMobile.Connector.Answer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ProtocolException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnswerBuilder {

    private ServerAnswer serverAnswer;

    public void setServerAnswer(HttpsURLConnection connection) {
        String content = getResponse(connection);
        if (!isXMLLike(content)) {
            this.serverAnswer = new WrongAnswer(content);
        } else {
            this.serverAnswer = new ValidAnswer(content);
        }
    }

    public ServerAnswer getServerAnswer() {
        return serverAnswer;
    }

    private String getResponse(HttpsURLConnection connection) {

        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            return "Protocol Exception";
        }
        connection.setDoInput(true);
        try {
            connection.connect();
        } catch (IOException e) {
            return "Could not connect. Maybe blocked socket?";
        }

        int response;
        try {
            response = connection.getResponseCode();
        } catch (IOException e) {
            return "Could not retrieve response code";
        }
        if (response != 200) {
            return "Error response code: " + response;
        }

        try {
            return readIt(connection.getInputStream(), 11000);
        } catch (IOException e) {
            return "Could not read from inputStream";
        }
    }

    // Reads an InputStream and converts it to a String.
    private String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public static boolean isXMLLike(String inXMLStr) {
        boolean retBool = false;

        final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";

        if (inXMLStr != null && inXMLStr.trim().length() > 0) {
            if (inXMLStr.trim().startsWith("<")) {

                Pattern pattern = Pattern.compile(XML_PATTERN_STR,
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

                Matcher matcher = pattern.matcher(inXMLStr);
                retBool = matcher.matches();
            }
        }

        return retBool;
    }
}
