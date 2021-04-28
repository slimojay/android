package com.example.joachim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class PickerClass extends AppCompatActivity {
Button cl, sbd; DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_picker_class);
        dp = (DatePicker)findViewById(R.id.dp);
        sbd = (Button)findViewById(R.id.btsort);
        sbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daywithzero, monthwithzero;
               if (dp.getDayOfMonth() < 10){
             Integer day = dp.getDayOfMonth();
            daywithzero = "0" + day.toString();
                }
                else{
                    Integer day = dp.getDayOfMonth();
                     daywithzero  = day.toString();
                }

             Integer month = dp.getMonth() + 1;
                if (month < 10){
                    monthwithzero = "0" + month.toString();
                }else{
                    monthwithzero = month.toString();
                }
             Integer year = dp.getYear();
             String join = monthwithzero + "/" + daywithzero + "/" + year;

               /* StringBuilder builder=new StringBuilder();;
                builder.append((dp.getMonth() + 1)+"/");//month is 0 based
                builder.append(dp.getDayOfMonth()+"/");
                builder.append(dp.getYear());*/
                Toast.makeText(getApplicationContext(), "fetching hearings for : " + join.toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), CauseList.class);
                i.putExtra("date", join.toString());
                startActivity(i);
            }
        });
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void gotoCauseList(View v){
        Intent go = new Intent(getApplicationContext(), CauseList.class);
        startActivity(go);
    }
}
