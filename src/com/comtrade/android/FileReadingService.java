package com.comtrade.android;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.logging.Logger;

/**
 * <p></p>
 */
public class FileReadingService extends IntentService {
    public static final Logger log = Logger.getLogger(FileReadingService.class.getSimpleName());

    public FileReadingService() {
        super("FileReadingService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        log.info("onStart");
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        log.info("onHandleIntent");
        synchronized (this) {
            try {
                wait(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FileReadingService.this, "File Downloaded", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
