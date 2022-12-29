package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout home_cs;
    HomeFragment homeFragment;
    FriendFragment friendFragment;
    TipFragment tipFragment;
    MypageFragment mypageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        friendFragment = new FriendFragment();
        tipFragment = new TipFragment();
        mypageFragment = new MypageFragment();
        home_cs = findViewById(R.id.home_cs);


        getSupportFragmentManager().beginTransaction().replace(R.id.home_cs, homeFragment).commit();
        BottomNavigationView bottom_menu = findViewById(R.id.bottom_menu);
        bottom_menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_cs, homeFragment).commit();
                        return true;
                    case R.id.tab_friend:
                        Intent intent = new Intent(MainActivity.this, FriendTabActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.tab_tip:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_cs, tipFragment).commit();
                        return true;
                    case R.id.tab_mpage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_cs, mypageFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }



}