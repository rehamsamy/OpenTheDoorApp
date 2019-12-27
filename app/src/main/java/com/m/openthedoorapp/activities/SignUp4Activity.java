package com.m.openthedoorapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.SetPasswordModel;
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

public class SignUp4Activity extends AppCompatActivity {

    @BindView(R.id.signUp_password_ed_id)
    EditText password_ed;
    @BindView(R.id.sign_confirmPass_ed_id)
    EditText confirmPass_ed;

    private NetworkAvailable networkAvailable;
    private int user_id;
    private String token, phone;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4);
        ButterKnife.bind(this);
        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(SignUp4Activity.this);

        if (getIntent().hasExtra("id")) {
            user_id = getIntent().getExtras().getInt("id");
            token = getIntent().getStringExtra("token");
            phone = getIntent().getStringExtra("phone");
        }
    }

    @OnClick(R.id.signUp4_btn_id)
    void signUp4() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(password_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(confirmPass_ed, getString(R.string.required))
                    && FUtilsValidation.isPasswordEqual(password_ed, confirmPass_ed, getString(R.string.password_not_identical))) {

                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(SignUp4Activity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<SetPasswordModel> call = serviceInterface.registerSetPassword(user_id, password_ed.getText().toString(),confirmPass_ed.getText().toString());
                call.enqueue(new Callback<SetPasswordModel>() {
                    @Override
                    public void onResponse(Call<SetPasswordModel> call, Response<SetPasswordModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()){
                            Toast.makeText(SignUp4Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp4Activity.this, SignUp2Activity.class);
                            intent.putExtra("id" , response.body().getUserinfo().getId());
                            intent.putExtra("token" , response.body().getToken());
                            intent.putExtra("phone" , response.body().getUserinfo().getPhone());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp4Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SetPasswordModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard.setupUI(findViewById(R.id.password_signUp_layout_id));
    }
}
