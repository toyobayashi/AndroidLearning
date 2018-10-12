package com.github.toyobayashi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
            String resVer = "10045700";
            downloadManifest(
                resVer,
                (current, total) -> {
                    div.setText(current + " / " + total);
                },
                (err, path, size) -> {

                    if (err != null) {
                        btnDl.setEnabled(true);
                        Toast.makeText(MainActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    div.setText("success, " + path + " / " + size);
                    SQLiteDatabase db = openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
                    Cursor c = db.query("manifests", new String[] { "name", "hash" }, "name = \"master.mdb\"", null, null, null, null);

                    c.moveToFirst();
                    String hash = c.getString(c.getColumnIndex("hash"));

                    downloadMaster(resVer, hash,
                        (current, total) -> {
                            div.setText(current + " / " + total);
                        },
                        (err2, path2, size2) -> {
                            btnDl.setEnabled(true);
                            if (err2 != null) {
                                Toast.makeText(MainActivity.this, err2.toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            div.setText("success, " + path2 + " / " + size2);
                        }
                    );
                }
            );
        });
    }

    private static void doCheck(CheckAsyncTask.CheckAsyncTaskCallback callback) {
        new CheckAsyncTask(callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void downloadManifest(String resVer, DownloadAsyncTask.ProgressCallback progressCallback, DownloadAsyncTask.ResultCallback resultCallback) {
        new DownloadAsyncTask(progressCallback, resultCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
            "http://storage.game.starlight-stage.jp/dl/" + resVer + "/manifests/Android_AHigh_SHigh",
            getExternalFilesDir("").getAbsolutePath() + File.separator + "manifest_" + resVer + ".db.lz4",
            "true"
        );
    }

    private void downloadMaster(String resVer, String hash, DownloadAsyncTask.ProgressCallback progressCallback, DownloadAsyncTask.ResultCallback resultCallback) {
        new DownloadAsyncTask(progressCallback, resultCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
            "http://storage.game.starlight-stage.jp/dl/resources/Generic/" + hash,
            getExternalFilesDir("").getAbsolutePath() + File.separator + "master_" + resVer + ".db.lz4",
            "true"
        );
    }
}
