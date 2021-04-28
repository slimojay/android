package com.example.joachim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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


public class LoginfileActivity extends AppCompatActivity {
    Button b; EditText ed1, ed2; String email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();*/
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loginfile);
        b = findViewById(R.id.sub);

       b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed1 = findViewById(R.id.ed1);
                //.getText().toString();
                ed2 = findViewById(R.id.ed2);
                email = ed1.getText().toString().trim();
                password = ed2.getText().toString();
                if(TextUtils.isEmpty(email)){
                  //  Toast.makeText(this, "Type In Your Email", Toast.LENGTH_SHORT).show();
                    ed1.setError("Email is Required");
                    ed1.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    //  Toast.makeText(this, "Type In Your Email", Toast.LENGTH_SHORT).show();
                    ed2.setError("Email is Required");
                    ed2.requestFocus();
                    return;
                }
                login(email, password);
               // Toast.makeText(getApplicationContext(), ed1.getText().toString() + " " + ed2.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });//*/
    }
    public void login(String email, String password){
       // Toast.makeText(getApplicationContext(), ed1.getText().toString() + " " + ed2.getText().toString(), Toast.LENGTH_LONG).show();
        AsyncAct cls = new AsyncAct(this);
                cls.execute("login_request", email, password);

    }
    public void gotoProfile(){
        Intent i = new Intent(getApplicationContext(), Register.class);
        startActivity(i);
    }

    public class AsyncAct extends AsyncTask<String,Void,String>{
        String result; SharedPreferences sp;
        public static final int CONNECTION_TIMEOUT=10000;
        public static final int READ_TIMEOUT=15000;
        public static final String DETAILS_PREF = "mypref";
        Context ct; ProgressBar pb; AlertDialog alertDialog;
    public AsyncAct(Context context) {
            this.ct = context;
        }
        @Override
        protected String doInBackground(String... Params) {

            try {
                String type = (String) Params[0];
                String email = (String) Params[1];
                String password = (String) Params[2];
                //String link = "http://181.214.83.147/api/model.php";
                String link = "https://android.remote-court.com/api/model.php";
                String data = URLEncoder.encode("email", "UTF-8") + "=" +
                        URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
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
           pb = (ProgressBar)findViewById(R.id.progressBar);
           pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            pb.setVisibility(View.GONE);

            try {
                JSONObject jo = new JSONObject(result);
                int status = jo.getInt("success");
                if (status == 0){
                    alertDialog.setMessage("Sequence Failed, Wrong Login Credentials");
                    alertDialog.show();
                }else {
                    String email = jo.getString("email");
                    String division = jo.getString("division");
                    String role = jo.getString("role");
                    String state = jo.getString("state");
                    sp = getSharedPreferences(DETAILS_PREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putString("email", email);
                    ed.putString("role", role);
                    ed.putString("state", state);
                    ed.putString("division", division);
                    ed.commit();
                    TextView tvv = (TextView)findViewById(R.id.stat);
                    tvv.setText("successful login");
                  // LoginfileActivity lfa = new LoginfileActivity();
                   // lfa.gotoProfile();
                    Intent i = new Intent(LoginfileActivity.this, CaseList.class);
                    startActivity(i);
                }
            }catch (Exception e){
               Toast.makeText(getApplicationContext(), "something went wrong " + result + " " + e, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
//*/
    }

}
