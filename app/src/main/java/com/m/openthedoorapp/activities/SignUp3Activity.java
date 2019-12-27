package com.m.openthedoorapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.RegsiterPhoneModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.HideKeyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp3Activity extends AppCompatActivity {

    @BindView(R.id.signUp_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.phoneCode_ed_id)
    EditText phoneCode_ed;

    private String name, email;
    private String image_uri;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(SignUp3Activity.this);
        phoneCode_ed.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard.setupUI(findViewById(R.id.phoneSignUp_layout_id));
    }

    @OnClick(R.id.login_txtV_id)
    void goLogin() {
        startActivity(new Intent(SignUp3Activity.this, LoginActivity.class));
        finish();
    }


    @OnClick(R.id.signUp3_btn_id)
    void continSignUp() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))) {
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(SignUp3Activity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegsiterPhoneModel> call = serviceInterface.registerPhone(phoneCode_ed.getText().toString() + phone_ed.getText().toString());
                call.enqueue(new Callback<RegsiterPhoneModel>() {
                    @Override
                    public void onResponse(Call<RegsiterPhoneModel> call, Response<RegsiterPhoneModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()) {
//                            showDialog(SignUp3Activity.this, response.body().getCode());
                            Intent intent = new Intent(SignUp3Activity.this, RgisterCheckCode.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp3Activity.this, response.body().getMessages().getPhone().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegsiterPhoneModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @OnClick(R.id.signUp3_back_txtV_id)
    void goBack() {
        finish();
    }

    private void showDialog(Context context, String code_val) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_diaog_code_layout, null);
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        final AlertDialog ad = builder.show();
        TextView code_txtV = dialogLayout.findViewById(R.id.code_val_txtV_id);
        Button ok_btn = dialogLayout.findViewById(R.id.alertOk_btn_id);
        code_txtV.setText(code_val);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                // -------------------------------------------------------
                Intent intent = new Intent(SignUp3Activity.this, RgisterCheckCode.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
