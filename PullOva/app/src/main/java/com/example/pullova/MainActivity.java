package com.example.pullova;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    double lat, lon; TextView lattv, lontv; Intent i; Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.getWindow().setFlags(Window.Manager.LayoutParams.FLAG_FULL_SCREEN, Win)
        setContentView(R.layout.activity_main);
        lat = 40.7128;
        lon = 74.0060;
        lattv = (TextView)findViewById(R.id.lat);
        lontv = (TextView)findViewById(R.id.lon);
        String getlatTxt = lattv.getText().toString();
        String getlonTxt = lontv.getText().toString();
        String conCatLat = getlatTxt + " " + lat;
        String conCatLon = getlonTxt + " " + lon;
        lattv.setText(conCatLat);
        lontv.setText(conCatLon);
        i = new Intent(getApplicationContext(), MapsActivity.class);
        i.putExtra("longitude", lon);
        i.putExtra("latitude", lat);
        bt1 = (Button)findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });



    }
}
