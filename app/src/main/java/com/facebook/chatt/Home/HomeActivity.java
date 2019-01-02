package com.facebook.chatt.Home;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.facebook.chatt.LogInActivity;
import com.facebook.chatt.R;
import com.facebook.chatt.Settings.SettingsActivity;
import com.facebook.chatt.utils.CustomPageAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Context context = HomeActivity.this;

    private Toolbar mToolbar;
    private FirebaseAuth.AuthStateListener mListener;
    private ViewPager viewPager;
    private CustomPageAdapter mCustomPageAdapter;
    private TabLayout mTabLayout;

    //Firebase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.mainAppBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.drawable.logo_small);

        viewPager = findViewById(R.id.mainTabPager);
        mCustomPageAdapter = new CustomPageAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mCustomPageAdapter);

        mTabLayout = findViewById(R.id.mainTabs);
        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.getTabAt(0).setText("REQUESTS");
        mTabLayout.getTabAt(1).setText("CHATS");
        mTabLayout.getTabAt(2).setText("FRIENDs");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        Log.d(TAG, "onCreateOptionsMenu: inflatingMenuLayout");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        Log.d(TAG, "onOptionsItemSelected: settingUpitemSelectedListener");
        switch (item.getItemId()) {

            case R.id.menuLogOut:
                mAuth.signOut();
                startActivity(new Intent(context, LogInActivity.class));
                finish();
                break;
            case R.id.menuSettings:
                startActivity(new Intent(context, SettingsActivity.class));
                break;
        }
        return true;
    }

}
