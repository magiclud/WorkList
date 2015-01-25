package com.example.StudyBuddyMobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.StudyBuddyMobile.parser.models.Item;
import com.example.StudyBuddyMobile.parser.models.Root;
import com.example.StudyBuddyMobile.util.Downloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {

    private static final String DEBUG = "MYACTIVITY";
    private static final String XMLFILE = "list.xml";
    private static final String CREDENTIALS = "credentials.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateList() {
        String xmlString = tryXmlDownload();
        if( xmlString == null ) {
            return;
        }
        saveStringToFile(xmlString, XMLFILE);
        Root xml = readXmlFromFile();
        if (xml == null) {
            return;
        }

        ListView list = (ListView) findViewById(R.id.listView);

        List<String> positions = new ArrayList<String>();
        for( Item item : xml.getItems() ) {
            positions.add(item.getName() + " " + item.getNumber() + "\n" + item.getDeadline());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, positions);
        list.setAdapter(adapter);
    }

    private String tryXmlDownload() {
        try {
            return Downloader.INSTANCE.getXmlStringFromURLWithExistingCredentials(openFileInput(CREDENTIALS));
        } catch (IOException e) {
            return null;
        }
    }

    private boolean saveStringToFile(String content, String filename) {
        if( content == null || filename == null ) {
            return false;
        }

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e(DEBUG, "File writing error");
            return false;
        }
        return true;
    }

    private Root readXmlFromFile() {
        try {
            FileInputStream fis = openFileInput(XMLFILE);
            Root xml = Downloader.INSTANCE.getRootFromXmlInputStream(fis);
            fis.close();
            return xml;
        } catch (IOException e) {
            Log.e(DEBUG, "XML File reading error");
            return null;
        }
    }
}
