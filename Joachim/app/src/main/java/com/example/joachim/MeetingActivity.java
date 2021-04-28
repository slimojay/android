package com.example.joachim;

import android.content.Intent;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MeetingActivity extends AppCompatActivity {
    Bundle b; TextView tv; String stv, ttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        try {
            Intent bundle = getIntent();
            if (!bundle.hasExtra("session_id") || !bundle.hasExtra("session_pw") || !bundle.hasExtra("participant_name")) {
                Intent z = new Intent(getApplicationContext(), CauseList.class);
                startActivity(z);
            } else {
                b = getIntent().getExtras();
                String name = b.getString("participant_name");
                String session = b.getString("session_id");
                //String password = b.getString("session_pw");
                ttitle = b.getString("title");
                URL serverUrl;
                serverUrl = new URL("https://meet.jit.si");
                JitsiMeetUserInfo info = new JitsiMeetUserInfo();
                info.setDisplayName(name);
                //info.email = "alice@atlanta.com"
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setServerURL(serverUrl)
                        .setRoom(session)
                        .setSubject(ttitle)
                        // .setToken(password)
                        //.setName(name)
                        // When using JaaS, set the obtained JWT here
                        //.setToken("MyJWT")
                        .setWelcomePageEnabled(false)
                        .build();
               /* JitsiMeet.setDefaultConferenceOptions(defaultOptions);
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(session)
                        .build();*/
                // Launch the new activity with the given options. The launch() method takes care
                // of creating the required Intent and passing the options.
                JitsiMeetActivity.launch(this, options);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("error", e);
            startActivity(i);
            //throw new RuntimeException("Invalid server URL!");
        }
        tv = (TextView)findViewById(R.id.title);
        tv.setText("Case Title : " + ttitle);
    }


    public void gotoCauseList(View View){
        Intent j = new Intent(getApplicationContext(), CauseList.class);
        startActivity(j);
    }

}
