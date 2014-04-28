package com.comtrade.android.fragments;

import android.app.Activity;
import android.os.Bundle;

import java.io.File;

/**
 * <p></p>
 */
public class ImagePreviewActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = (File) getIntent().getSerializableExtra("file");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ImagePreviewFragment(file)).commit();
    }
}