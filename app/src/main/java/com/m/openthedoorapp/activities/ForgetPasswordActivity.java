package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ForgetPassModel;
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


public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.forgetPass_phone_ed_id)
    EditText forgetPass_phone_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(ForgetPasswordActivity.this);
    }

    @OnClick(R.id.forgetPass_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.forgetPass_btn_id)
    void sendPhone() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(forgetPass_phone_ed, getString(R.string.required))) {

                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(ForgetPasswordActivity.this, getString(R.string.sending), false);

                ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ForgetPassModel> call = apiServiceInterface.sendPhone(forgetPass_phone_ed.getText().toString());
                call.enqueue(new Callback<ForgetPassModel>() {
                    @Override
                    public void onResponse(Call<ForgetPassModel> call, Response<ForgetPassModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgetPasswordActivity.this, VerificationCodeActivity.class);
                            intent.putExtra("code", response.body().getMobileConfCode());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgetPassModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard.setupUI(findViewById(R.id.forgetPass_layout_id));
    }
}
