package com.example.StudyBuddyMobile.connector;

import android.os.AsyncTask;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWebsiteTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            return downloadUrl(params[0]);
        } catch (IOException e) {
            return "<list><error id=\"3\"><message>IO Error</message></error><items></items></list>";
        }
    }

    protected String downloadUrl(String urlString) throws IOException {
        InputStream input = null;
        int len = 50000;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            input = conn.getInputStream();
            return readIt(input, len);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    protected String readIt(InputStream stream, int len) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer).trim();
    }
}
