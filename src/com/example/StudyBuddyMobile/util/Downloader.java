package com.example.StudyBuddyMobile.util;

import com.example.StudyBuddyMobile.connector.XmlDownloaderTask;
import com.example.StudyBuddyMobile.parser.ParserTask;
import com.example.StudyBuddyMobile.parser.models.Root;

import java.io.*;
import java.util.concurrent.TimeUnit;

public enum Downloader {

    INSTANCE;

    private static final String linkFormat = "http://10.0.2.2:9000/%s/%s/xml";

    public String getXmlStringFromURL(String login, String password) {

        if( login == null || login.isEmpty() || password == null || password.isEmpty() ) {
            return null;
        }

        XmlDownloaderTask xmlDownloader = new XmlDownloaderTask();
        xmlDownloader.execute(String.format(linkFormat, login, password));

        try {
            return xmlDownloader.get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }

    public String getXmlStringFromURLWithExistingCredentials(FileInputStream fis) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String xml = this.getXmlStringFromURL(reader.readLine(), reader.readLine());
            fis.close();
            return xml;
        } catch (IOException e) {
            return null;
        }
    }

    public Root getRootFromXmlInputStream(InputStream fis) {
        try {
            ParserTask parser = new ParserTask();
            parser.execute(fis);
            return parser.get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }
}
