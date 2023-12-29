package com.example.qrclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

public class ScannerQR extends AppCompatActivity {

    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, ScannerQR.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qr);
    }
}