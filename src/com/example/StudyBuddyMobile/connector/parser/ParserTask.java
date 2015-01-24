package com.example.StudyBuddyMobile.connector.parser;

import android.os.AsyncTask;
import android.util.Log;
import com.example.StudyBuddyMobile.connector.parser.models.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;

public class ParserTask extends AsyncTask<InputStream, Void, Root> {

    private static final String DEBUG = "Parser";

    @Override
    protected Root doInBackground(InputStream... params) {
        Serializer serializer = new Persister();
        try {
            return serializer.read(Root.class, params[0]);
        } catch (Exception e) {
            Log.e(DEBUG, e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Root result) {

    }
}
