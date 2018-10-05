package com.toyobayashi.cgssversionchecker;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

class CheckAsyncTask extends AsyncTask<Void, Void, String> {

    private WeakReference<TextView> txtResVer;
    private WeakReference<Button> btnCheck;
    CheckAsyncTask (TextView txtResVer, Button btnCheck) {
        this.txtResVer = new WeakReference<>(txtResVer);
        this.btnCheck = new WeakReference<>(btnCheck);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        txtResVer.get().setText(R.string.checking);
        btnCheck.get().setEnabled(false);
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String res = "";
        try {
            URL url = new URL("https://starlight.kirara.ca/api/v1/info");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            res = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return res;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        String resver = "failed";
        try {
            JSONObject jsonObject = new JSONObject(res);
            resver = jsonObject.getString("truth_version");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        btnCheck.get().setEnabled(true);
        txtResVer.get().setText(resver);
    }
}
