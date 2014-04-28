package com.comtrade.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.comtrade.android.R;

import java.io.File;
import java.util.logging.Logger;

/**
 * <p></p>
 */
public class BrowseImagesActivity extends Activity {
    private static final Logger log = Logger.getLogger(BrowseImagesActivity.class.getSimpleName());

    private ListFilesFragment listFilesFragment;
    private ImagePreviewFragment imagePreviewFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_images);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        listFilesFragment = (ListFilesFragment) getFragmentManager().findFragmentById(R.id.frg_list_files);
        imagePreviewFragment = (ImagePreviewFragment) getFragmentManager().findFragmentById(R.id.frg_preview_image);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        boolean success = listFilesFragment.goBack();
        if (!success) {
            super.onBackPressed();
        }
    }

    public void openFile(File file) {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        log.info("isTablet = " + isTablet);
        if (isTablet) {
            if (imagePreviewFragment == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.frg_preview_image, new ImagePreviewFragment(file))
                        .commit();
            } else {
                imagePreviewFragment.openFile(file);
            }
        } else {
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            intent.putExtra("file", file);
            startActivity(intent);
        }
    }
}