package com.example.StudyBuddyMobile;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.example.StudyBuddyMobile.list.Group;
import com.example.StudyBuddyMobile.list.MyExpandableListAdapter;
import com.example.StudyBuddyMobile.parser.models.Item;
import com.example.StudyBuddyMobile.parser.models.Root;
import com.example.StudyBuddyMobile.util.Downloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyActivity extends Activity {


    private static final String DEBUG = "MYACTIVITY";
    private static final String XMLFILE = "list.xml";
    private static final String CREDENTIALS = "credentials.txt";
    private int notifyId=0;
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
        //petla po wszystkich item
        readDataDeadline();//->time

        String title = "TITLE";
       long time =3000;
        createNotification(time, title);
        notifyId++;
        String title2 = "TITLE2";
        long time2 =5000;
        createNotification(time2, title2);

    }

    private void createNotification(long time, String title) {
      //  String title = "TITLE";
        String subject = "SUBJECT";
        String body = "BODY";

        // Give the intent the values so that we can populate the notification
        // in the receiver
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra(MyReceiver.TITLE_KEY, title);
        intent.putExtra(MyReceiver.SUBJECT_KEY, subject);
        intent.putExtra(MyReceiver.BODY_KEY, body);
        intent.putExtra("NotificationId", notifyId); //to get id and receive unique notify
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        // Schedule the alarm
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, notifyId, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time, alarmIntent);

    }

    private void readDataDeadline() {

         List<Item> xml = readXmlFromFile().getItems();

       // for (Item item : xml) {
            Toast.makeText(this, "DEADLINE before: " , Toast.LENGTH_SHORT).show();
         //  Date deadline = xml.get(0).getDeadlineDate();
         //   Log.d("CZAS", "!!!!!!!!!!!!!:   deadline.toString();");
         //   Toast.makeText(this, "DEADLINE: " + deadline, Toast.LENGTH_SHORT).show();
       // }
    }
}
