package com.example.StudyBuddyMobile;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import com.example.StudyBuddyMobile.list.Group;
import com.example.StudyBuddyMobile.list.MyExpandableListAdapter;
import com.example.StudyBuddyMobile.parser.models.Item;
import com.example.StudyBuddyMobile.parser.models.Root;
import com.example.StudyBuddyMobile.util.Downloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MyActivity extends Activity {

    private static final String DEBUG = "MYACTIVITY";
    private static final String XMLFILE = "list.xml";
    private static final String CREDENTIALS = "credentials.txt";

    SparseArray<Group> groups = new SparseArray<Group>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        if (xmlString == null) {
            return;
        }
        saveStringToFile(xmlString, XMLFILE);
        Root xml = readXmlFromFile();
        if (xml == null) {
            return;
        }

        createData(xml);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, groups);
        listView.setAdapter(adapter);
    }

    private String tryXmlDownload() {
        try {
            return Downloader.INSTANCE.getXmlStringFromURLWithExistingCredentials(openFileInput(CREDENTIALS));
        } catch (IOException e) {
            return null;
        }
    }

    private boolean saveStringToFile(String content, String filename) {
        if (content == null || filename == null) {
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

    public void createData(Root xml) {
        Set<String> names = new HashSet<String>();

        int cnt = 0;
        for (Item item : xml.getItems()) {
            String name = item.getName();

            if (names.contains(name)) {
                continue;
            }
            names.add(name);

            Group group = new Group(name);
            for (Item curItem : xml.getItems()) {
                if (curItem.getName().equals(name)) {
                    group.children.add(curItem);
                }
            }
            groups.append(cnt++, group);
        }
    }

    public void notify(View vobj) {
        String title = "TITLE";
        String subject = "SUBJECT";
        String body = "BODY";
        int seconds = 10;

        // Give the intent the values so that we can populate the notification
        // in the receiver
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra(MyReceiver.TITLE_KEY, title);
        intent.putExtra(MyReceiver.SUBJECT_KEY, subject);
        intent.putExtra(MyReceiver.BODY_KEY, body);

        // Schedule the alarm
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmMgr1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent1 = PendingIntent.getBroadcast(this, 1, intent, 0);

        AlarmManager alarmMgr2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent2 = PendingIntent.getBroadcast(this, 2, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 3000, alarmIntent);

        alarmMgr1.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 3000, alarmIntent1);

        alarmMgr2.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000, alarmIntent2);

        Log.d(DEBUG, "BEJTON");
    }
}
