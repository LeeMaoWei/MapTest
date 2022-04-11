package com.example.maptest.myhome;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.example.maptest.R;

import com.example.maptest.databinding.ActivityMyHomeBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class HomeActivity extends AppCompatActivity{

    private ActivityMyHomeBinding binding;
    public static GeoCoder mSearch = null;
    public String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mSearch = GeoCoder.newInstance();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_my_home);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        Intent i=getIntent();

        username=i.getStringExtra("username");



    }

    public String getTitles(){
        return username;
        }
    }

