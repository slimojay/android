package com.example.joachim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import java.net.URLConnection;
import java.net.URLEncoder;

public class AsyncActivity extends AsyncTask<String,Void,String>  {
    Context ct; ProgressBar pb; AlertDialog alertDialog; String result;
    public AsyncActivity(Context context) {
     this.ct = context;
    }
    @Override
    protected String doInBackground(String... Params) {

        try {
            String type = (String) Params[0];
            String email = (String) Params[1];
            String password = (String) Params[2];
            String link = "http://www.android.remote-court.com/v1/api/model.php";
            String data = URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("req", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
            URL url = new URL(link);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("post");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            bw.write(data);
            bw.flush();
            bw.close();
            out.close();
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
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
        return result;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {


       try {
            JSONObject jo = new JSONObject(result);
            int status = jo.getInt("status");
            if (status == 0){
                alertDialog.setMessage("Sequence Failed, Wrong Login Credentials");
                alertDialog.show();
            }else {
                String email = jo.getString("email");
                String division = jo.getString("division");
                String role = jo.getString("role");
                String state = jo.getString("state");
                alertDialog.setMessage("1");
                alertDialog.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



    }
