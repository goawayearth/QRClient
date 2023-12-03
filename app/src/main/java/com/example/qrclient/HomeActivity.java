package com.example.qrclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment mfragment;

    public static Intent newIntent(FragmentActivity activity){
        Intent intent = new Intent(activity, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFragmentManager = getSupportFragmentManager();
        mfragment = mFragmentManager.findFragmentById(R.id.fragment_homepage);

        if (mfragment == null) {
            mfragment = new HomePageFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_homepage, mfragment)
                    .commit();
        }

        //更改fragment处的页面(主页、我的)
        mBottomNavigationView = findViewById(R.id.bottom_menu);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_homepage:
                        mfragment = new HomePageFragment();
                        break;
                    case R.id.item_me:
                        mfragment = new MeFragment();
                        break;
                }

                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_homepage, mfragment)
                        .commit();
                return true;
            }
        });

    }
}

