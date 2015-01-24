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
import com.example.StudyBuddyMobile.connector.DownloadWebsiteTask;
import com.example.StudyBuddyMobile.parser.models.Root;
import com.example.StudyBuddyMobile.parser.ParserTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Settings extends Activity {

    private static final String DEBUG = "SETTINGS";
    private static final String FILENAME = "list.xml";
    private static final String linkFormat = "http://10.0.2.2:9000/%s/%s/xml";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public void saveCredentials(View view) {
        EditText login = (EditText) findViewById(R.id.login);
        EditText password = (EditText) findViewById(R.id.password);

        String loginString = login.getText().toString();
        String passwordString = password.getText().toString();

        if (loginString.isEmpty() || passwordString.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Empty input", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (!checkConnectivity()) {
            Toast toast = Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        DownloadWebsiteTask download = new DownloadWebsiteTask();
        download.execute(String.format(linkFormat, loginString, passwordString));

        String output = "";
        try {
            output = download.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.e(DEBUG, "Interrupt");
        } catch (ExecutionException e) {
            Log.e(DEBUG, "Execution");
        } catch (TimeoutException e) {
            Log.e(DEBUG, "Timeout");
        }

        if( output.isEmpty() ) {
            Toast toast = Toast.makeText(getApplicationContext(), "Could not download file", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(output.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e(DEBUG, "XML File writing error");
        }

        Root xml = null;
        try {
            FileInputStream fis = openFileInput(FILENAME);
            ParserTask parser = new ParserTask();
            parser.execute(fis);
            xml = parser.get(1000, TimeUnit.MILLISECONDS);
            fis.close();
        } catch (IOException e) {
            Log.e(DEBUG, "XML File reading error");
            Log.e(DEBUG, e.toString());
        } catch (InterruptedException e) {
            Log.e(DEBUG, "Interrupt");
        } catch (ExecutionException e) {
            Log.e(DEBUG, "Execution");
        } catch (TimeoutException e) {
            Log.e(DEBUG, "Timeout");
        }


    }

    private boolean checkConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}