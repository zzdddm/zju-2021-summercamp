package com.example.chpt1demo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn1 = findViewById(R.id.btn1);
        TextView tv1 = findViewById(R.id.tv1);
        Switch swt1 = findViewById(R.id.swt1);
        TextView tv3 = findViewById(R.id.tv3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = (flag + 1 ) % 2 ;
                tv1.setText(flag > 0?"by:zzdddm":"Hello World!");
                Log.d("mainactivity","change");

            }
        });

        swt1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv3.setText("Swithc:ON");
                    Log.d("switch1", "onCheckedChanged:ON ");


                }else{
                    tv3.setText("Swithc:OFF");
                    Log.d("switch1", "onCheckedChanged:OFF ");


                }
        };



    });
}

}