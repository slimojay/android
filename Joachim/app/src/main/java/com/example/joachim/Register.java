package com.example.joachim;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView;;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class Register extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String name;
    Button bt1;
    EditText ed1, ed2, ed3, ed4;
    TextView tv1;
    Spinner sp;
    String[] list = {"Full-stack", "Back-end", "Front-end", "Native App", "Desktop App", "Hybrid App"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        sp = (Spinner) findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aa);
        ed1 = (EditText) findViewById(R.id.firstname);
        ed2 = (EditText) findViewById(R.id.lastname);
        ed3 = (EditText) findViewById(R.id.email);
        ed4 = (EditText) findViewById(R.id.phone);
        //sp = (Spinner) findViewById(R.id.spinner);
    }
    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), list[position] , Toast.LENGTH_LONG).show();
    }

    public Register() {
        super();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void  registerUser(){
        StringBuilder str = new StringBuilder("Your Registration Details Are : ");
        str.append(ed1.toString() + ' ' + ed2.toString() + ' '+ ed3.toString() + ' ' + ed4.toString() + ' ' + sp.getSelectedItem().toString() );
    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }


}