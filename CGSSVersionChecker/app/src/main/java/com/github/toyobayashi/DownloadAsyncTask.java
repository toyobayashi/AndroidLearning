package com.github.toyobayashi;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadAsyncTask extends AsyncTask<String, Long, Object[]> {

    private ProgressCallback progressCallback;
    private ResultCallback resultCallback;
    DownloadAsyncTask (ProgressCallback progressCallback, ResultCallback resultCallback) {
        this.progressCallback = progressCallback;
        this.resultCallback = resultCallback;
    }

    @FunctionalInterface
    public interface ProgressCallback {
        void call(long current, long total);
    }

    @FunctionalInterface
    public interface ResultCallback {
        void call(Exception err, String path, long size);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object[] doInBackground(String... args) {
        String path = args[1];
        String dir = path.substring(0, path.lastIndexOf(File.separator));
        // String filename = path.substring(path.lastIndexOf(File.separator) + 1);

        boolean isDecompress = args[2].equals("true");

        File saveDir = new File(dir);
        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                return new Object[] { new IOException("创建目录失败"), "", -1L };
            }
        }
        File file = new File(path);
        long fileLength = file.length();
        long contentLength;

        try {
            URL url = new URL(args[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.0; Nexus 42 Build/XYZZ1Y)");
            connection.setRequestProperty("X-Unity-Version", "5.4.5p1");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Connection", "Keep-Alive");
            if (fileLength > 0) {
                connection.setRequestProperty("Range", fileLength + "-");
            }
            connection.setRequestMethod("GET");

            contentLength = connection.getContentLength();
            byte[] buffer = new byte[8192];
            int len;
            if (fileLength != contentLength) {

                InputStream inputStream = connection.getInputStream();

                int current = 0;
                // ByteArrayOutputStream bos = new ByteArrayOutputStream();

                FileOutputStream fos = new FileOutputStream(file);
                // fos.flush();
                while ((len = inputStream.read(buffer)) != -1) {

                    fos.write(buffer, 0, len);
                    current += len;
                    publishProgress(current + fileLength, contentLength + fileLength);
                }
                inputStream.close();
                fos.close();

            }
            connection.disconnect();

            if (isDecompress) {
                FileInputStream lz4ins = new FileInputStream(path);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = lz4ins.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                lz4ins.close();

                LZ4Decompressor lz4Decompressor = new LZ4Decompressor();
                byte[] decBuffer = lz4Decompressor.decompress(baos.toByteArray());
                baos.close();

                String decompressedFilePath = path.substring(0, path.lastIndexOf("."));
                FileOutputStream decfos = new FileOutputStream(decompressedFilePath);
                decfos.write(decBuffer);
                decfos.close();
                if (!file.delete()) {
                    throw new IOException("删除文件失败");
                }
                return new Object[] { null, decompressedFilePath, (long) decBuffer.length };
            }
            return new Object[] { null, path, contentLength + fileLength };

        } catch (IOException e) {
            e.printStackTrace();
            return new Object[] { e, "", -1L };
        }
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        progressCallback.call(values[0], values[1]);
    }

    @Override
    protected void onPostExecute(Object[] res) {
        super.onPostExecute(res);

        if (res[0] != null) {
            resultCallback.call((Exception) res[0], (String) res[1], (long) res[2]);
        } else {
            resultCallback.call(null, (String) res[1], (long) res[2]);
        }
    }

}
