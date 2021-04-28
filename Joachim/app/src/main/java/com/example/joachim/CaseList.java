package com.example.joachim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class CaseList extends AppCompatActivity {
    SharedPreferences sp; TextView tv; String email, div, role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_case_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
       sp = getSharedPreferences("mypref", Context.MODE_PRIVATE);
       if(!sp.contains("email")){
           SharedPreferences.Editor ed = sp.edit();
           ed.clear();
           Intent i = new Intent(getApplicationContext(), LoginfileActivity.class);
           startActivity(i);
       }else{
           tv = (TextView)findViewById(R.id.who);
            div = sp.getString("division", "user");
            role = sp.getString("role", "");
            int rol = Integer.parseInt(role);
            int divv = Integer.parseInt(div);
           if(divv == 1 && rol == 0) {
               tv.setText("Signed-In As : Lagos Registrar");
           }else if(divv == 2 && rol == 0){
               tv.setText("Signed-In As : Ikeja Registrar");
           }else if(rol == 1){
               tv.setText("Signed-In As : Admin User");
           }else{
               tv.setText("Signed-In As : Unknown");
           }
       }

    }

}
