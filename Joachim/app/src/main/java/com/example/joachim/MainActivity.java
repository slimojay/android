package com.example.joachim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
/*
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

 */

public class MainActivity extends AppCompatActivity {
    Intent i, j; String result; Intent b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        b = getIntent();
        if (b.hasExtra("date")) {
            Bundle bd = getIntent().getExtras();
            String error = bd.getString("error");
            Toast.makeText(getApplicationContext(), "error found is : " + error, Toast.LENGTH_LONG).show();
        }
        checkVersion();
    }
    public void login(View View){
        //i = new Intent(getApplicationContext(), LoginfileActivity.class);
        //startActivity(i);
        //checkVersion();
    }
    public void causeList(View View){
        j = new Intent(getApplicationContext(), CauseList.class);
        startActivity(j);
    }
    public void appLook(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void checkVersion(){
        VersionControl vc = new VersionControl();
        String versionNumber = vc.getVersion();
        AsyncAct aa = new AsyncAct(this);
        aa.execute("check_version", versionNumber);

    }


    public class AsyncAct extends AsyncTask<String,Void, String> {
        //String result; SharedPreferences sp;
        public static final int CONNECTION_TIMEOUT=10000;
        public static final int READ_TIMEOUT=15000;
        //public static final String DETAILS_PREF = "mypref";
        Context ct; ProgressBar pb; AlertDialog alertDialog;
        public AsyncAct(Context context) {
            this.ct = context;
        }
        @Override
        protected String doInBackground(String... Params) {

            try {
                String type = (String) Params[0];
                String version = (String) Params[1];
                //String password = (String) Params[2];
                //String link = "http://181.214.83.147/api/model.php";
                String link = "https://android.remote-court.com/api/model.php";
                String data = URLEncoder.encode("version", "UTF-8") + "=" +
                        URLEncoder.encode(version, "UTF-8");

                data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                URL url = new URL(link);

                // Append parameters to URL
               /* Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", email)
                        .appendQueryParameter("password", password)
                        .appendQueryParameter("type", type)
                        ;
                String data = builder.build().getEncodedQuery();*/


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                OutputStream out = conn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                bw.write(data);
                bw.flush();
                bw.close();
                out.close();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                result = "";
                String ln = "";
                while((ln = br.readLine()) != null){
                    result += ln;
                }
                br.close();
                in.close();
                conn.disconnect();
                return result;

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            TextView tv = (TextView)findViewById(R.id.version);
            tv.setText("please wait ...");
            tv.setTextColor(getColor(R.color.lightgrey));
            //pb = (ProgressBar)findViewById(R.id.progressBar);
            //pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = ja.getJSONObject(0);
                TextView tv = (TextView)findViewById(R.id.version);
                //JSONObject ja = obj.getJSONObject(0);
                //JSONArray jo = new JSONArray(result);
                String report = jo.getString("message");
                String link = "http://" + jo.getString("link");
                if (!report.toLowerCase().equals("app is up to date")){
                    tv.setText(report);// +  jo.getString("link") + "to get latest version");
                    tv.setTextColor(getColor(R.color.WHITE));
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    Button clbtn = (Button)findViewById(R.id.causelistbtn);
                    clbtn.setVisibility(View.GONE);
                    Button btdwn = (Button)findViewById(R.id.dwn);
                    btdwn.setVisibility(View.VISIBLE);
                    btdwn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //String url = "http://www.example.com";
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(link));
                            startActivity(i);
                        }
                    });
                }else{
                    tv.setText("");
                    Toast.makeText(getApplicationContext(), report, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), CauseList.class);
                    startActivity(i);
                }



                //Toast.makeText(getApplicationContext(), "response is : " + jo.getString("message"), Toast.LENGTH_LONG).show();
            }catch(Exception e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "error is : " + e, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
//*/
    }

}