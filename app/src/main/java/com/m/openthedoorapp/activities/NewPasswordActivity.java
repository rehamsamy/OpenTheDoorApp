package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.SetNewPasswordModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.HideKeyboard;

public class NewPasswordActivity extends AppCompatActivity {

    @BindView(R.id.newPassword_ed_id)
    EditText password_ed;
    @BindView(R.id.confirmNewPass_ed_id)
    EditText conPassword_ed;

    // Vars...
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this);
        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(NewPasswordActivity.this);

        if (getIntent().hasExtra("user_id")) {
            user_id = getIntent().getStringExtra("user_id");
        }
    }

    @OnClick(R.id.newPass_btn_id)
    void setNewPassword() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(password_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(conPassword_ed, getString(R.string.required))) {
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(NewPasswordActivity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<SetNewPasswordModel> call = serviceInterface.setNewPass(password_ed.getText().toString(), conPassword_ed.getText().toString(), user_id);
                call.enqueue(new Callback<SetNewPasswordModel>() {
                    @Override
                    public void onResponse(Call<SetNewPasswordModel> call, Response<SetNewPasswordModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            Toast.makeText(NewPasswordActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(NewPasswordActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SetNewPasswordModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.newPassword_back_txtV_id)
    void goBack() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard.setupUI(findViewById(R.id.newPassword_layout_id));
    }
}
