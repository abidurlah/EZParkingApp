package com.example.ezparkingapp.activities.auth.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.activities.auth.fragments.LoginFragment;
import com.example.ezparkingapp.activities.auth.fragments.SignUpFragment;

public class SignInSignUpPagerAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;
    public SignInSignUpPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context, int totalTabs) {
        super(fm, behavior);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new LoginFragment();
        }
        return new SignUpFragment();
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getResources().getString(R.string.login);
        }
        return context.getResources().getString(R.string.sign_up);
    }
}
