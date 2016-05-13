package com.example.musictube.Download;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.musictube.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;

/**
 * Created by Ekrem on 2016-05-08.
 */

public class FileDownloader extends AsyncTask<Void, Integer, Void> {
    public static final String TAG = "FileDownloader";


    private NotificationManager nm;
    private NotificationCompat.Builder builder;
    private int notifyId = 0x1234;
    private int fileSize = 0xffffffff;

    private final Context context;
    private final String fileURL;
    private final File saveFilePath;
    private final String title;

    private final String debugContext;

    public FileDownloader(Context context, String fileURL, File saveFilePath, String title) {
        this.context = context;
        this.fileURL = fileURL;
        this.saveFilePath = saveFilePath;
        this.title = title;

        this.debugContext = "'" + fileURL +
                "' => '" + saveFilePath + "'";
    }

    /**
     * Hämtar en fil från en URL i bakgrunden med hjälp av en { @link AsyncTask }
     *
     * @param fileURL      HTTP URL of the file to be downloaded
     * @param saveFilePath path of the directory to save the file
     * @param title
     * @throws IOException
     */
    public static void downloadFile(final Context context, final String fileURL, final File saveFilePath, String title) {
        new FileDownloader(context, fileURL, saveFilePath, title).execute();
    }

    /** AsyncTask impl: utförs i gui tråd */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Drawable icon = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setLargeIcon(((BitmapDrawable) icon).getBitmap())
                .setContentTitle(saveFilePath.getName())
                .setContentText(saveFilePath.getAbsolutePath())
                .setProgress(fileSize, 0, false);
        nm.notify(notifyId, builder.build());
    }

    /** AsyncTask impl: utförs i bakgrunden, tråden gör nedladdning */
    @Override
    protected Void doInBackground(Void... voids) {
        HttpsURLConnection con = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            con = NetCipher.getHttpsURLConnection(fileURL);
            int responseCode = con.getResponseCode();

            // alltid kontrollera HTTP svarskod först
            if (responseCode == HttpURLConnection.HTTP_OK) {
                fileSize = con.getContentLength();
                inputStream = new BufferedInputStream(con.getInputStream());
                outputStream = new FileOutputStream(saveFilePath);

                int bufferSize = 8192;
                int downloaded = 0;

                int bytesRead = -1;
                byte[] buffer = new byte[bufferSize];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    downloaded += bytesRead;
                    if (downloaded % 50000 < bufferSize) {
                        publishProgress(downloaded);
                    }
                }

                publishProgress(bufferSize);

            } else {
                Log.i(TAG, "No file to download. Server replied HTTP code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, "No file to download. Server replied HTTP code: ", e);
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        builder.setProgress(fileSize, progress[0], false);
        nm.notify(notifyId, builder.build());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        nm.cancel(notifyId);
    }
}
