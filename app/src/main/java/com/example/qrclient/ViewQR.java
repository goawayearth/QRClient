package com.example.qrclient;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.net.URL;

public class ViewQR extends AppCompatActivity {

    private static String ip = "192.168.117.235";
    private static final String EXTRA_NUM = "express_num";
    private String express_num = null;
    private ImageView imageView = null;

    public static Intent newIntent(FragmentActivity activity,String num){
        Intent intent = new Intent(activity, ViewQR.class);
        intent.putExtra(EXTRA_NUM,num);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);
        imageView = findViewById(R.id.imageView);
        express_num = getIntent().getStringExtra(EXTRA_NUM);
        Log.i(TAG,express_num);
        loadImage(express_num);

    }


    private void loadImage(String express_num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://"+ip+":8080/server_war_exploded/"+express_num+".png");
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
                    // 如果需要，可以处理结果
                } else {
                    Log.e(TAG, "Bitmap 为空");
                }
            }
        });
    }
}