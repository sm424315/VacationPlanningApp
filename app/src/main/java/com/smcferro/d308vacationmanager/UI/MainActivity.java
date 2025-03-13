package com.smcferro.d308vacationmanager.UI;

import android.content.Intent;
import android.os.Bundle;

import com.smcferro.d308vacationmanager.R;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.MainButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VacationList.class);
                startActivity(intent);
            }
        });

    }
}