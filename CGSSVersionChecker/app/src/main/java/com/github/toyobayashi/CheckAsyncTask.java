package com.github.toyobayashi;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class CheckAsyncTask extends AsyncTask<Void, Void, Object[]> {

    private CheckAsyncTaskCallback callback;
    CheckAsyncTask (CheckAsyncTaskCallback callback) {
        this.callback = callback;
    }

    @FunctionalInterface
    public interface CheckAsyncTaskCallback {
        void call(Exception err, String resver);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object[] doInBackground(Void... voids) {
        // CGSSClient client = new CGSSClient("940464243:174481488:cf608be5-6d38-421a-8eb1-11a501132c0a", "10045510");

        String account = new String(Base64.decode("Nzc1ODkxMjUwOjkxMDg0MTY3NTo2MDBhNWVmZC1jYWU1LTQxZmYtYTBjNy03ZGVkYTc1MWM1ZWQ=", Base64.DEFAULT), StandardCharsets.US_ASCII);
        CGSSClient client = new CGSSClient(account);
        try {
            JSONObject res = client.check();
            JSONObject dataHeaders = res.getJSONObject("data_headers");
            int resultCode = dataHeaders.getInt("result_code");
            if (resultCode == 214) {
                String resver =  dataHeaders.getString("required_res_ver");
                return new Object[] { null, resver };
            } else if (resultCode == 1) {
                return new Object[] { null, client.getResVer() };
            } else {
                return new Object[] { new Exception(String.valueOf(resultCode)), null };
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new Object[] { e, null };
        }
    }

    @Override
    protected void onPostExecute(Object[] res) {
        super.onPostExecute(res);
        if (res[0] != null) {
            if (res[0] instanceof IOException) {
                callback.call((IOException) res[0], null);
            } else if (res[0] instanceof JSONException) {
                callback.call((JSONException) res[0], null);
            } else {
                callback.call((Exception) res[0], null);
            }
        } else {
            callback.call(null, (String) res[1]);
        }
    }
}
