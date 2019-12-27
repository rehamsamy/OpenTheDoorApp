package com.m.openthedoorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.SendCodeModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.HideKeyboard;

import static com.m.openthedoorapp.utils.ActionsWithServices.dialogUtil;

public class VerificationCodeActivity extends AppCompatActivity {

    @BindView(R.id.verficationCode_btn_id)
    Button send_btn;
    @BindView(R.id.verficationCode_phone_ed_id)
    EditText verifyCode_ed;
    @BindView(R.id.verficationCode_back_txtV_id)
    ImageView back;

    private NetworkAvailable networkAvailable;
    private String code;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(VerificationCodeActivity.this);
        if (getIntent().hasExtra("code")) {
            code = getIntent().getStringExtra("code");
            verifyCode_ed.setText(code);
        }
    }

    @OnClick(R.id.verficationCode_back_txtV_id)
    void go_back() {
        finish();
    }

    @OnClick(R.id.verficationCode_btn_id)
    void send_code() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(verifyCode_ed, getString(R.string.required))) {

                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(VerificationCodeActivity.this, getString(R.string.sending), false);

                ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<SendCodeModel> call = apiServiceInterface.sendCode(verifyCode_ed.getText().toString());
                call.enqueue(new Callback<SendCodeModel>() {
                    @Override
                    public void onResponse(Call<SendCodeModel> call, Response<SendCodeModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerificationCodeActivity.this, NewPasswordActivity.class);
                            intent.putExtra("user_id" , response.body().getUser_id());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(VerificationCodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendCodeModel> call, Throwable t) {
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
        hideKeyboard.setupUI(findViewById(R.id.verifyCode_layout_id));
    }
}
