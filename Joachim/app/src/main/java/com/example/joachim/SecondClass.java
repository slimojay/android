package com.example.joachim;

import android.content.Intent;
import android.os.Bundle;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SecondClass extends AppCompatActivity {
    String name, Occupation, Join; int age; Intent i;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_class);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        Occupation = extras.getString("occupation");
        age = extras.getInt("age");
        Join = " my name is " + name +  " and I am a " + age +  " old " + Occupation;
        Toast.makeText(getApplicationContext(), Join, Toast.LENGTH_LONG).show();
    }
    public void showAlert(View v){
        AlertDialog.Builder b = new AlertDialog.Builder(SecondClass.this);
        b.setTitle("Login Alert");
                b.setMessage("Do you want to close this app ?");
                b.setCancelable(false);
                b.setPositiveButton("yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SecondClass.this.finish();
                       // System.exit(0);
                         i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                    }
                });
                b.setNegativeButton("no", new AlertDialog.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int which){
                       dialog.cancel();
                   }
                });
                AlertDialog d = b.create();
                d.show();
    }
}
