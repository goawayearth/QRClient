package com.example.qrclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class HomeActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        loadImage();
    }

    private void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.164.235:8080/server_war_exploded/nihao.png");
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    showQrCode(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showQrCode(Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);

                    String result = QrCodeScanner.scanQrCode(bitmap);
                    if (result != null) {
                        textView.setText("Result: " + result);
                    } else {
                        textView.setText("QR code not detected");
                    }
                }
            }
        });
    }
}