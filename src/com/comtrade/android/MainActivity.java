package com.comtrade.android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQUEST_SELECT_FILE = 1;
    private static final int REQUEST_SELECT_CONTACT = 2;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.open_image_activity_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OpenImageActivity.class);
                intent.setData(Uri.parse("content://media/external/images/media/5374"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileReadingService.class);
                startService(intent);
            }
        });

        findViewById(R.id.btn_open_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("content://media/external/images/media/5374"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_open_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.google.com"));
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_select_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            }
        });

        findViewById(R.id.btn_select_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(Uri.parse("content://contacts"),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_SELECT_CONTACT);
            }
        });

        findViewById(R.id.btn_send_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://media/external/images/media/5374"));
                intent.setType("image/jpeg");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_FILE) {
                String file = data.getData().toString();
                Toast.makeText(this, "Datoteka: " + file, Toast.LENGTH_LONG).show();
            } else if (requestCode == REQUEST_SELECT_CONTACT) {
                String phone = getPhoneNumber(data.getData());
                Toast.makeText(this, "Datoteka: " + phone, Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getPhoneNumber(Uri data) {
        Cursor cursor = getContentResolver().query(data, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        return cursor.getString(columnIndex);
    }
}
