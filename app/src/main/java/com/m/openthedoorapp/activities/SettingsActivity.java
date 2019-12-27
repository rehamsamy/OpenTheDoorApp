package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    String lang_selected;

    @BindView(R.id.settings_back_txtV_id)
    ImageView back;
    @BindView(R.id.settings_language_layout_id)
    RelativeLayout langusge_layout;
    @BindView(R.id.language_val_txtV_id)
    TextView language_val_txtV;

    @BindView(R.id.currency_eng_sideIcon_txtV)
    TextView currency_eng_sideIcon_txtV;
    @BindView(R.id.currency_arb_sideIcon_txtV)
    TextView currency_arb_sideIcon_txtV;
    @BindView(R.id.payment_eng_sideIcon_txtV)
    TextView payment_eng_sideIcon_txtV;
    @BindView(R.id.payment_arb_sideIcon_txtV)
    TextView payment_arb_sideIcon_txtV;
    @BindView(R.id.lang_eng_sideIcon_txtV)
    TextView lang_eng_sideIcon_txtV;
    @BindView(R.id.lang_arb_sideIcon_txtV)
    TextView lang_arb_sideIcon_txtV;
    @BindView(R.id.notifications_eng_sideIcon_txtV)
    TextView notifications_eng_sideIcon_txtV;
    @BindView(R.id.notifications_arb_sideIcon_txtV)
    TextView notifications_arb_sideIcon_txtV;

    private SharedPreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        // get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(SettingsActivity.this, SharedPreferenceManager.PREFERENCE_NAME);

        language_val_txtV.setText(getResources().getString(R.string.language_val));
        if (preferenceManager.getValue(SharedPreferenceManager.LANGUAGE, "").equals("ar")) {
            Log.i("lang: ", preferenceManager.getValue(SharedPreferenceManager.LANGUAGE, ""));
            language_val_txtV.setText(getString(R.string.arabic));
            currency_arb_sideIcon_txtV.setVisibility(View.VISIBLE);
            payment_arb_sideIcon_txtV.setVisibility(View.VISIBLE);
            lang_arb_sideIcon_txtV.setVisibility(View.VISIBLE);
            notifications_arb_sideIcon_txtV.setVisibility(View.VISIBLE);
            currency_eng_sideIcon_txtV.setVisibility(View.GONE);
            payment_eng_sideIcon_txtV.setVisibility(View.GONE);
            lang_eng_sideIcon_txtV.setVisibility(View.GONE);
            notifications_eng_sideIcon_txtV.setVisibility(View.GONE);
        } else {
            language_val_txtV.setText(getString(R.string.english));
            currency_arb_sideIcon_txtV.setVisibility(View.GONE);
            payment_arb_sideIcon_txtV.setVisibility(View.GONE);
            lang_arb_sideIcon_txtV.setVisibility(View.GONE);
            notifications_arb_sideIcon_txtV.setVisibility(View.GONE);
            currency_eng_sideIcon_txtV.setVisibility(View.VISIBLE);
            payment_eng_sideIcon_txtV.setVisibility(View.VISIBLE);
            lang_eng_sideIcon_txtV.setVisibility(View.VISIBLE);
            notifications_eng_sideIcon_txtV.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.settings_back_txtV_id)
    public void go_back() {
        finish();
    }

    @OnClick(R.id.settings_language_layout_id)
    public void setLang_selected() {
        showLanguagesDialog();
    }

    public void setConfig(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private void showLanguagesDialog() {
        // Items to display in Dialog
        String[] items = {getString(R.string.english), getString(R.string.arabic)};

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.select_language));
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    // English Item is Slected
                    lang_selected = "en";
                    language_val_txtV.setText(lang_selected);
                    preferenceManager.setValue(SharedPreferenceManager.LANGUAGE, lang_selected);
                    setConfig(lang_selected, SettingsActivity.this);

                } else if (which == 1) {
                    // Arabic Item is Selected
                    lang_selected = "ar";
                    language_val_txtV.setText(lang_selected);
                    preferenceManager.setValue(SharedPreferenceManager.LANGUAGE, lang_selected);
                    setConfig(lang_selected, SettingsActivity.this);
                }

                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        dialog.create().show(); // show Dialog...
    }
}
