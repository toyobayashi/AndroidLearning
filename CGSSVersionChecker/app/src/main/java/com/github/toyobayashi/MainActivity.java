package com.github.toyobayashi;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnCheck;
    private Button btnDl;
    private TextView txtResVer;
    private TextView div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = findViewById(R.id.btnCheck);
        btnDl = findViewById(R.id.btnDl);
        txtResVer = findViewById(R.id.txtResVer);
        div = findViewById(R.id.div);
        div.setText(getExternalFilesDir("").getAbsolutePath());

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

        btnDl.setOnClickListener((v) -> {
            div.setText("0 / 0");
            btnDl.setEnabled(false);
            downloadManifest(
                "http://storage.game.starlight-stage.jp/dl/10045510/manifests/Android_AHigh_SHigh",
                getExternalFilesDir("").getAbsolutePath() + File.separator + "manifest.lz4",
                (err, current, total) -> {
                    if (err != null) {
                        btnDl.setEnabled(true);
                        Toast.makeText(MainActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                    }
                    if (current == total) {
                        btnDl.setEnabled(true);
                    }
                    div.setText(current + " / " + total);
                }
            );
        });
    }

    private static void doCheck(CheckAsyncTask.CheckAsyncTaskCallback callback) {
        new CheckAsyncTask(callback).execute();
    }

    private static void downloadManifest(String url, String path, DownloadAsyncTask.DownloadAsyncTaskCallback callback) {
        new DownloadAsyncTask(callback).execute(url, path);
    }
}
