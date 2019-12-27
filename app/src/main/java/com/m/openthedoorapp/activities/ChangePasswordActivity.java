package com.m.openthedoorapp.activities;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ChangePasswordModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.changePass_back_txtV_id)
    ImageView back;
    @BindView(R.id.old_password_ed_id)
    EditText oldPassword_ed;
    @BindView(R.id.new_password_ed_id)
    EditText newPassword_ed;
    @BindView(R.id.confirm_newPass_ed_id)
    EditText confirm_newPass_ed;
    @BindView(R.id.changePass_btn_id)
    Button changePass_btn;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.changePass_back_txtV_id)
    public void setBack() {
        finish();
    }

    @OnClick(R.id.changePass_btn_id)
    public void changrPassword() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(oldPassword_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(newPassword_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(confirm_newPass_ed, getString(R.string.required))
                    && FUtilsValidation.isPasswordEqual(newPassword_ed, confirm_newPass_ed, getString(R.string.pass_not_equal))
            ) {

                if (oldPassword_ed.getText().toString().length() < 6) {
                    oldPassword_ed.setError(getResources().getString(R.string.pass_is_weak));
                    oldPassword_ed.requestFocus();
                    return;
                }
                if (newPassword_ed.getText().toString().length() < 6) {
                    newPassword_ed.setError(getResources().getString(R.string.pass_is_weak));
                    newPassword_ed.requestFocus();
                    return;
                }
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(ChangePasswordActivity.this, getString(R.string.updatting), false);

                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ChangePasswordModel> call = serviceInterface.changePassword(oldPassword_ed.getText().toString(),
                        newPassword_ed.getText().toString(), confirm_newPass_ed.getText().toString(), FindServiceActivity.userModel.getToken());
                call.enqueue(new Callback<ChangePasswordModel>() {
                    @Override
                    public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                        ChangePasswordModel changePasswordModel = response.body();
                        dialog.dismiss();
                        if (changePasswordModel.getStatus()) {
                            Log.i(TAG, "onResponse " + changePasswordModel.getStatus());
                            Toast.makeText(ChangePasswordActivity.this, changePasswordModel.getMessages(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, changePasswordModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
