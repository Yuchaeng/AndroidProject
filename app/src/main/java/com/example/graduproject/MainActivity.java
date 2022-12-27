package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout home_cs;
    HomeFragment homeFragment;
    FriendFragment friendFragment;
    ChatFragment chatFragment;
    MypageFragment mypageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        friendFragment = new FriendFragment();
        chatFragment = new ChatFragment();
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
                    case R.id.tab_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_cs, chatFragment).commit();
                        bottom_menu.setVisibility(View.GONE);
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