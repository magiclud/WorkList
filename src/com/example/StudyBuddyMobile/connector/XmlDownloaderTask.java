package com.example.StudyBuddyMobile.connector;

import android.os.AsyncTask;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class XmlDownloaderTask extends AsyncTask<String, Void, String> {

    private static final String errorXML = "<list><error id=\"3\"><message>IO Error</message></error><items></items></list>";

    @Override
    protected String doInBackground(String... params) {
        try {
            return downloadUrl(params[0]);
        } catch (IOException e) {
            return errorXML;
        }
    }

    protected String downloadUrl(String urlString) throws IOException {
        InputStream input = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            input = conn.getInputStream();
            return getStringFromInputStream(input);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private String getStringFromInputStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();

    }
}
