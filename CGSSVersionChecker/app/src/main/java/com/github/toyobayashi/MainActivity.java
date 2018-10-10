package com.github.toyobayashi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnCheck;
    private TextView txtResVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = findViewById(R.id.btnCheck);
        txtResVer = findViewById(R.id.txtResVer);

        btnCheck.setOnClickListener((v) -> {

            txtResVer.setText(R.string.checking);
            btnCheck.setEnabled(false);
            doCheck((err, resver) -> {
                if (err != null) {
                    Toast.makeText(MainActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                    txtResVer.setText("failed");
                } else {
                    txtResVer.setText(resver);
                }
                btnCheck.setEnabled(true);
            });
        });
    }

    private static void doCheck(CheckAsyncTask.CheckAsyncTaskCallback callback) {
        new CheckAsyncTask(callback).execute();
    }

}
