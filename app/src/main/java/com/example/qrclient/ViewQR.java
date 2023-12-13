package com.example.qrclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

public class ViewQR extends AppCompatActivity {

    public static Intent newIntent(FragmentActivity activity,String num){
        Intent intent = new Intent(activity, HomeActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

    }
}