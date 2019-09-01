package com.github.toyobayashi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IconAsyncTask extends AsyncTask<String, Void, Object[]> {
    private IconAsyncTask.IconAsyncTaskCallback callback;
    IconAsyncTask(IconAsyncTask.IconAsyncTaskCallback callback) {
        this.callback = callback;
    }

    @FunctionalInterface
    public interface IconAsyncTaskCallback {
        void call(Exception err, Bitmap bitmap);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object[] doInBackground(String... str) {

        try {
            URL url = new URL("https://truecolor.kirara.ca/icon_card/" + str[0] + ".png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.setConnectTimeout(10000);
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                InputStream inputStream = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return new Object[] { null, bitmap };
            }
            return new Object[] { new IOException(String.valueOf(statusCode)), null };
        } catch (IOException e) {
            e.printStackTrace();
            return new Object[] { e, null };
        }
    }

    @Override
    protected void onPostExecute(Object[] res) {
        super.onPostExecute(res);
        if (res[0] != null) {
            callback.call((IOException)res[0], null);
        } else {
            callback.call(null, (Bitmap) res[1]);
        }
    }
}
