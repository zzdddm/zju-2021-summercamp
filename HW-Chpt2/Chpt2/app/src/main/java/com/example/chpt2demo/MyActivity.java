package com.example.chpt2demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Log.i("MyActivity","onCreate_My");

        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,"点击",Toast.LENGTH_SHORT).show();

            }
        });

        
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MyActivity", "onRestart_My ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("MyActivity","onResume_My");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MyActivity", "onStart_My ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MyActivity", "onPause_My ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MyActivity", "onStop:_My");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MyActivity", "onDestroy_My ");
    }
}