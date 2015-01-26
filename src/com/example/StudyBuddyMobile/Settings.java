package com.example.StudyBuddyMobile;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.StudyBuddyMobile.parser.models.Root;
import com.example.StudyBuddyMobile.util.Downloader;

import java.io.*;

public class Settings extends Activity {

    private static final String DEBUG = "SETTINGS";
    private static final String XMLFILE = "list.xml";
    private static final String CREDENTIALS = "credentials.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        EditText login = (EditText) findViewById(R.id.login);
        EditText password = (EditText) findViewById(R.id.password);

        try {
            FileInputStream fis = openFileInput(CREDENTIALS);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            login.setText(reader.readLine());
            password.setText(reader.readLine());

            fis.close();
        } catch (IOException e) { }
    }

    public void saveCredentials(View view) {
        EditText login = (EditText) findViewById(R.id.login);
        EditText password = (EditText) findViewById(R.id.password);

        String loginString = login.getText().toString();
        String passwordString = password.getText().toString();

        if (!checkBasicRules(loginString, passwordString)) {
            return;
        }

        String output = Downloader.INSTANCE.getXmlStringFromURL(loginString, passwordString);
        saveStringToFile(output, XMLFILE);

        Root xml = readXmlFromFile();
        if (xml == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Could not parse file", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if( xml.getError().getId() > 0 ) {
            Toast toast = Toast.makeText(getApplicationContext(), xml.getError().getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        saveStringToFile(loginString + "\n" + passwordString + "\n", CREDENTIALS);
        Toast toast = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean checkConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean checkBasicRules(String loginString, String passwordString) {
        if (loginString.isEmpty() || passwordString.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Empty input", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (!checkConnectivity()) {
            Toast toast = Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
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