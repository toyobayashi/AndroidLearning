package com.github.toyobayashi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button btnCheck;
    private TextView txtResVer;
    private TextView div;
    private ComponentProgress progress;
    private ImageView testImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = findViewById(R.id.btnCheck);
        txtResVer = findViewById(R.id.txtResVer);
        div = findViewById(R.id.div);
        progress = findViewById(R.id.progressBar);
        testImg = findViewById(R.id.testImg);
        div.setText(getExternalFilesDir("").getAbsolutePath());

        btnCheck.setOnClickListener((v) -> {

            txtResVer.setText(R.string.checking);
            btnCheck.setEnabled(false);
            doCheck((err, resVer) -> {

                if (err != null) {
                    Toast.makeText(MainActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                    txtResVer.setText("failed");
                    btnCheck.setEnabled(true);
                    return;
                }

                txtResVer.setText(resVer);
                progress.setProgress(0);

                downloadManifest(
                    resVer,
                    (current, total) -> {
                        if (progress.getMax() != total) progress.setMax((int) total);
                        progress.setProgress((int)current);
                        div.setText(current + " / " + total);
                    },
                    (e, path, size) -> {
                        progress.setProgress(0);
                        if (e != null) {
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            btnCheck.setEnabled(true);
                            return;
                        }

                        SQLiteDatabase db = openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
                        Cursor c = db.query("manifests", new String[] { "name", "hash" }, "name = \"master.mdb\"", null, null, null, null);

                        c.moveToFirst();
                        String hash = c.getString(c.getColumnIndex("hash"));
                        db.close();

                        downloadMaster(resVer, hash,
                            (current1, total1) -> {
                                if (progress.getMax() != total1) progress.setMax((int) total1);
                                progress.setProgress((int)current1);
                                div.setText(current1 + " / " + total1);
                            },
                            (err2, path2, size2) -> {
                                btnCheck.setEnabled(true);
                                progress.setProgress(0);
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
        });

        new IconAsyncTask((err, bitmap) -> {
            if (err != null) {
                Toast.makeText(MainActivity.this, err.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            testImg.setImageBitmap(bitmap);
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "200251");
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
