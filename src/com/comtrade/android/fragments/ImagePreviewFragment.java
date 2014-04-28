package com.comtrade.android.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.comtrade.android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <p></p>
 */
public class ImagePreviewFragment extends Fragment {

    private TextView imageTitle;
    private ImageView imageView;

    private File fileToOpen = null;

    public ImagePreviewFragment() {
    }

    public ImagePreviewFragment(File fileToOpen) {
        this.fileToOpen = fileToOpen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_preview, container, false);
        if (view != null) {
            imageTitle = (TextView) view.findViewById(R.id.image_title);
            imageView = (ImageView) view.findViewById(R.id.image_view);
        }
        if (fileToOpen != null) {
            openFile(fileToOpen);
        }
        return view;
    }

    public void openFile(File file) {
        imageTitle.setText(file.getName());
        try {
            if (getActivity() == null || getActivity().getContentResolver() == null) {
                return;
            }
            InputStream is = getActivity().getContentResolver().openInputStream(Uri.fromFile(file));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}