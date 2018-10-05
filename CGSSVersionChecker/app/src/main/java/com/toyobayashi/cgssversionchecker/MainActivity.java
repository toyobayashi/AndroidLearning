package com.toyobayashi.cgssversionchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btnCheck;
    private TextView txtResVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = findViewById(R.id.btnCheck);
        txtResVer = findViewById(R.id.txtResVer);

        btnCheck.setOnClickListener((View v) -> {
            CheckAsyncTask task = new CheckAsyncTask(txtResVer, btnCheck);
            task.execute();
        });
    }

}
