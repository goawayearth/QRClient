package com.example.qrclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import util.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    public static Intent newIntent(FragmentActivity activity ){
        Intent intent = new Intent(activity, MainActivity.class);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}