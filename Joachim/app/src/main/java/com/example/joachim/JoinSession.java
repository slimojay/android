package com.example.joachim;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;



public class JoinSession extends AppCompatActivity {
    Bundle extras;
    String title, full_name, session_id, session_pw, str;
    Button bt, join;
    TextView tv;
    EditText ed1, ed2, ed3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_session);
        extras = getIntent().getExtras();
        tv = (TextView) findViewById(R.id.ct);
        ed1 = (EditText) findViewById(R.id.fullname);
        ed2 = (EditText) findViewById(R.id.session_id);
        ed3 = (EditText) findViewById(R.id.password);
        ed2.setText(extras.getString("session_id"));
        ed3.setText(extras.getString("session_pw"));
        title = extras.getString("title");
        tv.setText(title);
        join = (Button) findViewById(R.id.btnjoin);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                full_name = ed1.getText().toString();
                session_id = ed2.getText().toString().trim();
                session_pw = ed3.getText().toString().trim();
                verifySession(session_id, session_pw, "verify_session");
            }
        });
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void verifySession(String session_id, String session_pw, String type) {
        VerifySession vs = new VerifySession();
        vs.execute(session_id, session_pw, type);
    }


    public class VerifySession extends AsyncTask<String, Integer, String> {
        ProgressBar pb;
        String result;
        Context ct;

        @Override
        public void onPreExecute() {
            pb = (ProgressBar) findViewById(R.id.pb);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        public String doInBackground(String... params) {
            try {
                String type = params[2];
                String session_id = params[0];
                String session_pw = params[1];
                String link = "https://android.remote-court.com/api/model.php";
                String data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" + URLEncoder.encode("session_id", "UTF-8")
                        + "=" + URLEncoder.encode(session_id, "UTF-8") + "&" + URLEncoder.encode("session_pw", "UTF-8") + "=" + URLEncoder.encode(session_pw, "UTF-8");
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bw.write(data);
                bw.flush();
                bw.close();
                outputStream.close();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String ln;
                result = "";
                ln = "";
                while ((ln = br.readLine()) != null) {
                    result += ln;
                }
                br.close();
                in.close();
                conn.disconnect();

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
              //  Toast.makeText(getApplicationContext(), "e" + e, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
              //  Toast.makeText(getApplicationContext(), "e" + e, Toast.LENGTH_LONG).show();

            }
            return null;
        }
        // }


        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jo = new JSONObject(result);
                if (jo.has("success")){
                String response = jo.getString("success");
               // if (response == "Proceed") {
                    pb.setVisibility(View.GONE);
                    Intent i = new Intent(JoinSession.this, MeetingActivity.class);
                    i.putExtra("session_id", session_id);
                    i.putExtra("session_pw", session_pw);
                    i.putExtra("participant_name", full_name);
                    i.putExtra("title", title);
                    startActivity(i);
                } else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "result " + result, Toast.LENGTH_LONG).show();
                    Intent z = new Intent(JoinSession.this, CauseList.class);
                    startActivity(z);
                }
            } catch (Exception e) {
                pb.setVisibility(View.GONE);
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "returned : " + e + " _ " + result, Toast.LENGTH_LONG).show();
                Intent z = new Intent(JoinSession.this, CauseList.class);
                startActivity(z);
            }
        }

    }
}