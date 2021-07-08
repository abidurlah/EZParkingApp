package com.example.ezparkingapp.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TableLayout;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.activities.auth.adapter.SignInSignUpPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SignInOrSignUpActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_sign_up);

        mTabLayout = findViewById(R.id.sign_in_up_tab_layout);
        mViewPager = findViewById(R.id.sign_in_up_view_pager);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.login));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.sign_up));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        SignInSignUpPagerAdapter signInSignUpPagerAdapter = new SignInSignUpPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                this,
                mTabLayout.getTabCount());
        mViewPager.setAdapter(signInSignUpPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }
}