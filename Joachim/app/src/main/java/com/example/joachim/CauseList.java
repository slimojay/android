package com.example.joachim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class CauseList extends AppCompatActivity {
    Button upcoming, previous, gotodate; LinearLayout ll; ScrollView sc;
    DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cause_list);
        // dp = (DatePicker) findViewById(R.id.dp);
        // dp.setEnabled(true);
        // dp.setBackgroundColor(getResources().getColor(R.color.WHITE));
        //dp.setBackground(getResources().getColor(R.color.WHITE));
        //ll = (LinearLayout) findViewById(R.id.llsv); //LinearLayout(this);

        ll = findViewById(R.id.llsv);
        ll.setOrientation(LinearLayout.VERTICAL);
        sc = (ScrollView) findViewById(R.id.sv);
        upcoming = (Button) findViewById(R.id.upcoming);
        previous = (Button) findViewById(R.id.previous);
        gotodate = (Button) findViewById(R.id.gotodate);

        Intent bundle = getIntent();

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.removeAllViews();
                Toast.makeText(CauseList.this, "Fetching Upcoming Cases", Toast.LENGTH_LONG).show();

                loadCauseList("upcoming", "", "fetch_cause_list");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.removeAllViews();
                Toast.makeText(CauseList.this, "Fetching Historical Cases", Toast.LENGTH_LONG).show();
                loadCauseList("historical", "", "fetch_cause_list");
            }
        });
        if (bundle.hasExtra("date")) {
            Bundle bd = getIntent().getExtras();
            String dts = bd.getString("date");
            ll.removeAllViews();
            Toast.makeText(getApplicationContext(), "fetching hearings for :" + dts, Toast.LENGTH_LONG).show();
            loadCauseList("", dts, "fetch_cause_list");
        }else {
            ll.removeAllViews();
            loadCauseList("upcoming", "", "fetch_cause_list");
        }


    }
    public void gotoDate(View v){
        Intent i = new Intent(getApplicationContext(),PickerClass.class);
        startActivity(i);
    }

    public void loadCauseList(String type, String date_range, String request_type){
        SyncWithDb swd = new SyncWithDb(this);
        swd.execute(type, date_range, request_type);
    }

    public void createDynamicTv(String strn, String s_id, String s_pw){
        //final String s_idd = s_id;
        //final String s_pww = s_pw;
        //Toast.makeText(getApplicationContext(), "returned : " + strn, Toast.LENGTH_LONG).show();

        TextView tv = new TextView(this);
        tv.setText(strn);
        Button btt = new Button(this);
        btt.setText("Join Session");
        /*btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder strbui = new StringBuilder();
                strbui.append("session_id is" + s_idd);
                strbui.append("session_pw is " + s_pww);
                Toast.makeText(getApplicationContext(), strbui, Toast.LENGTH_LONG).show();// strbui, Toast.LENGTH_LONG).show();
            }
        });*/
        ll.addView(tv);
       ll.addView(btt);
        sc.addView(ll);
    }

    public class SyncWithDb extends AsyncTask<String, Integer, String>{
          //SyncWithDb(Context ct, )
        ProgressBar pb; Context ctt; String result;
        public SyncWithDb(Context ct){
            this.ctt = ct;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
           // return ",,";
        }

        @Override
        protected void onPreExecute(){
          pb = (ProgressBar)findViewById(R.id.progressBar);
          pb.setVisibility(View.VISIBLE);
         }

        @Override
        protected String doInBackground(String... params){
            try{
                String type = (String)params[0];
                String date_format = (String)params[1];
                String req_type = (String)params[2];
               // String link = "https://android.remote-court.com/v1/api/model.php";
                String link = "https://android.remote-court.com/api/model.php";
                String data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(req_type, "UTF-8") + "&" + URLEncoder.encode("session_type", "UTF-8")
                        + "=" + URLEncoder.encode(type, "UTF-8") + "&" + URLEncoder.encode("date_range", "UTF-8") +  "=" + URLEncoder.encode(date_format, "UTF-8");
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
                while((ln = br.readLine()) != null ){
                  result += ln;
                }
                br.close();
                in.close();
                conn.disconnect();

                return result;
            }catch (MalformedURLException e){
                e.printStackTrace();
              //  Toast.makeText(getApplicationContext(), "e" + e, Toast.LENGTH_LONG).show();
            }catch (IOException e){
                e.printStackTrace();
             //   Toast.makeText(getApplicationContext(), "e" + e, Toast.LENGTH_LONG).show();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
          //  Toast.makeText(getApplicationContext(), "returned : " + result, Toast.LENGTH_LONG).show();
            int count, num; String str;
            SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy");
            try {
                //.getJSONArray(0);
                JSONArray ja = new JSONArray(result);
                JSONObject jot = ja.getJSONObject(0);
                String response = jot.getString("response");
                if (response.equals("available")){
                    TextView tvvv = (TextView) findViewById(R.id.display);
                    tvvv.setVisibility(View.GONE);
                    sc.setVisibility(View.VISIBLE);//setVisible();
                for(count = 0; count < ja.length(); count++) {
                    num = count + 1;
                    JSONObject jo = ja.getJSONObject(count);
                    String court = jo.getString("court");
                    String charge_suit_no = jo.getString("charge_suit_no");
                    final String title = jo.getString("title");
                    String date = jo.getString("date");
                    String time = jo.getString("time");
                    String session_type = jo.getString("session_type");
                    final String ses_id = jo.getString("session_id");
                    final String ses_pw = jo.getString("session_pw");
                    final String timer = jo.getString("timing");
                    str = num + ") " + title + " on " + date + " " + time + " WAT @ " + court + " \n (" + charge_suit_no + ")" + " session_type : " + session_type + " ... " + ses_id + " " + " " + ses_pw;
                    final Integer HEIGHT_MATCH = 75;
                    final Integer WIDTH_MATCH = 250;

                    //CauseList cl = new CauseList();
                    //cl.createDynamicTv(str, ses_id, ses_pw);
                    //LinearLayout.LayoutParams lparams = new LinearLayout().LayoutParams(
                    //      WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(WIDTH_MATCH, HEIGHT_MATCH);
                    lparams.setMargins(5, 25, 5, 0);
                    lparams2.setMargins(0, 5, 0, 0);
                    //  LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(WindowManager.LayoutParams.LAYOUT_MARGIN_START)
                    TextView tv = new TextView(CauseList.this);
                    tv.setLayoutParams(lparams);
                    tv.setText(str.toString());
                    //tv.setLeftTopRightBottom(7, 25, 7, 0);
                    Typeface tf = Typeface.createFromAsset(getAssets(), "Georgia.ttf");
                    tv.setTypeface(tf);
                    Button btt = new Button(CauseList.this);
                    btt.setLayoutParams(lparams2);
                    btt.setText("Join Session");
                    btt.setWidth(36);
                    btt.setHeight(26);//eight
                    btt.setTypeface(tf);
                    View v = new View(CauseList.this);
                    v.setLayoutParams(lparams);
                    v.setMinimumHeight(1);
                    //v.setBackground(getResources().getColor(R.color.BLACK));
                    v.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                    // btt.setLayoutParams();
                    //btt.setTypeface("font-family", R.string.times);
                    btt.setTextColor(getResources().getColor(R.color.WHITE));
                    btt.setBackgroundColor(getResources().getColor(R.color.BLACK));
                    //Toast.makeText(getApplicationContext(), timer, Toast.LENGTH_LONG).show();
                    btt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(CauseList.this, JoinSession.class);
                            i.putExtra("title", title);
                            i.putExtra("session_id", ses_id);
                            i.putExtra("session_pw", ses_pw);
                            // i.putExtra("court", court);
                            startActivity(i);

                        }
                    });
                    ll.addView(tv);
                    if(!timer.equals("upcoming case")) {
                        ll.addView(btt);
                    }
                    ll.addView(v);
                    ll.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                }
                    //Toast.makeText(getApplicationContext(), "returned : success __ "  + str, Toast.LENGTH_LONG).show();
                }else{
                    TextView tvvv = (TextView)findViewById(R.id.display);
                    //LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    //LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(WIDTH_MATCH, HEIGHT_MATCH);
                   // lparams.setMargins(5, 105, 5, 0);
                    sc.setVisibility(View.GONE);
                    tvvv.setVisibility(View.VISIBLE);
                    //ll.addView(tvvv);
                    pb.setVisibility(View.GONE);
                }

               // sc.addView(ll);
            }catch(Exception e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "returned : " + e, Toast.LENGTH_LONG).show();

            }

        }

    }

}
