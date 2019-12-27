package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.UserModel;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import java.util.Locale;

public class ChangeLanguage extends AppCompatActivity {

    private String selected_lang;
    private SharedPreferenceManager preferenceManager;
    private  Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        ButterKnife.bind(this);

        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(ChangeLanguage.this, SharedPreferenceManager.PREFERENCE_NAME);
        if (new SharedPreferenceManager(this, SharedPreferenceManager.PREFERENCE_NAME).check_Language()) {
            loadHome();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @OnClick(R.id.arabic_btn_id)
    void arabicSelected() {
        selected_lang = "ar";
//        PreferencesHelper.setSomeStringValue(ChangeLanguage.this, selected_lang);
        preferenceManager.setValue(SharedPreferenceManager.LANGUAGE, selected_lang);
        setConfig(selected_lang, ChangeLanguage.this);
        loadHome();
    }

    @OnClick(R.id.english_btn_id)
    void englishSelected() {
        selected_lang = "en";
        preferenceManager.setValue(SharedPreferenceManager.LANGUAGE, selected_lang);
        setConfig(selected_lang, ChangeLanguage.this);

        loadHome();
    }

    public void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private void loadHome() {
        String user_data = preferenceManager.getValue(SharedPreferenceManager.USER_DATA, "");
//        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
//        String user_data = prefs.getString("user_data", "");
        if (user_data != null && !user_data.equals("")) {
            // Retrive Gson Object from Shared Prefernces ....
            Gson gson = new Gson();
            UserModel userModel = gson.fromJson(user_data, UserModel.class);
            intent = new Intent(ChangeLanguage.this, FindServiceActivity.class);
            intent.putExtra("user_data", userModel);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        } else {
            intent = new Intent(ChangeLanguage.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
        }
    }
}
