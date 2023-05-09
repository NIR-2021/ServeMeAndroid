package com.example.serveme_m1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomProgressButton extends AppCompatActivity {

    Button but ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress_button);
        but = findViewById(R.id.button2);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new Dialog(CustomProgressButton.this);
                if(d.getWindow()!=null)
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                d.setContentView(R.layout.custom_progress);
                d.show();
            }
        });

    }
}