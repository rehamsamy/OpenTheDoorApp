package com.m.openthedoorapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    NetworkAvailable networkAvailable;
    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(SplashScreen.this, SharedPreferenceManager.PREFERENCE_NAME);
        setConfig(preferenceManager.getValue(SharedPreferenceManager.LANGUAGE, "en"), SplashScreen.this);

        networkAvailable = new NetworkAvailable(this);
        if (!networkAvailable.isNetworkAvailable())
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, ChangeLanguage.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
            }
        }, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
