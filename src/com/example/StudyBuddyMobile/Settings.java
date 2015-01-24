package com.example.StudyBuddyMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.StudyBuddyMobile.Connector.Answer.ServerAnswer;
import com.example.StudyBuddyMobile.Connector.HtmlContentRetriever;

import java.io.IOException;

public class Settings extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.settings);
    }

    public void saveCredentials(View view) {
        EditText login = (EditText) findViewById(R.id.login);
        EditText password = (EditText) findViewById(R.id.password);

        String loginString = login.getText().toString();
        String passwordString = password.getText().toString();

        Toast toast = Toast.makeText(getApplicationContext(), "App error", Toast.LENGTH_SHORT);
        try {
            ServerAnswer serverAnswer = HtmlContentRetriever.INSTANCE.checkCredentials(loginString, passwordString);
            if ( serverAnswer.authenticated() ) {
                toast = Toast.makeText(getApplicationContext(), "OK!", Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(getApplicationContext(), serverAnswer.getContent(), Toast.LENGTH_SHORT);
            }
        } catch (IOException e) {
            Log.e("saveCredentials", "ERROR");
        }
        toast.show();
    }

}