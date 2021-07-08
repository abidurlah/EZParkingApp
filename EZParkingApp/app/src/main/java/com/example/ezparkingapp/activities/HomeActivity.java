package com.example.ezparkingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.ezparkingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupNavigationView();
    }

    private void setupNavigationView() {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_container_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(mBottomNavigationView, navHostFragment.getNavController());
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.getNavController());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = navHostFragment.getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}