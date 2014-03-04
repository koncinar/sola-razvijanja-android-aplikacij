package com.comtrade.android;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p></p>
 * <p>Created by <a href="mailto:rkoneina@good.com">Rok Koncina</a> on 2.2.2014.</p>
 */
public class OpenImageActivity extends Activity {
    private Uri currentUri;
    private Bitmap bitmap;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitmap = null;
        setContentView(R.layout.display_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() == null || getIntent().getData() == null) {
            ((TextView) findViewById(R.id.image_title)).setText("No image...");
            return;
        }
        Uri uri = getIntent().getData();
        if (uri != null) {
            ((TextView) findViewById(R.id.image_title)).setText(String.format("%s (%s)", getRealNameFromURI(this, uri), uri));
            if (currentUri == null || !currentUri.equals(uri)) {
                new GetBitmapTask().execute(uri);
            } else {
                applyBitmap();
            }
        }
    }

    private void applyBitmap() {
        ((ImageView) findViewById(R.id.image_view)).setImageBitmap(bitmap);
    }

    public String getRealNameFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DISPLAY_NAME};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return null;
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void doNothing(View view) {
    }

    private class GetBitmapTask extends AsyncTask<Uri, Void, Bitmap> {
        private Uri currentUri;

        @Override
        protected Bitmap doInBackground(Uri... params) {
            if (params != null && params.length > 0) {
                currentUri = params[0];
                InputStream is = null;
                try {
                    is = getContentResolver().openInputStream(currentUri);
                    return BitmapFactory.decodeStream(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            OpenImageActivity.this.bitmap = bitmap;
            OpenImageActivity.this.currentUri = this.currentUri;
            OpenImageActivity.this.applyBitmap();
        }
    }

    public void close(View view) {
        finish();
    }
}