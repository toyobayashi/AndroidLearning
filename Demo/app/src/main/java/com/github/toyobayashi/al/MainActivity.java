package com.github.toyobayashi.al;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, RVActivity.class);
            startActivity(intent);
        });
    }
}
