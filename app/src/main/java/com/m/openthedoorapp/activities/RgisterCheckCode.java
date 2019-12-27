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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.RegisterCodeModel;
import com.m.openthedoorapp.model.RegsiterPhoneModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.HideKeyboard;

public class RgisterCheckCode extends AppCompatActivity {

    @BindView(R.id.signVerifyCode_ed_id)
    EditText code_ed;
    @BindView(R.id.sendCode_btn_id)
    Button send_btn;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister_check_code);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(RgisterCheckCode.this);
    }

    @OnClick(R.id.sendCode_btn_id)
    void sendCodee() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(code_ed, getString(R.string.required))) {
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(RgisterCheckCode.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegisterCodeModel> call = serviceInterface.registerCode(code_ed.getText().toString());
                call.enqueue(new Callback<RegisterCodeModel>() {
                    @Override
                    public void onResponse(Call<RegisterCodeModel> call, Response<RegisterCodeModel> response) {
                        dialog.dismiss();
                        if (response.body().getStatus()){
                            Toast.makeText(RgisterCheckCode.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RgisterCheckCode.this, SignUp4Activity.class);
                            intent.putExtra("id" , response.body().getUserinfo().getId());
                            intent.putExtra("token" , response.body().getToken());
                            intent.putExtra("phone" , response.body().getUserinfo().getPhone());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RgisterCheckCode.this, getString(R.string.invalid_code), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterCodeModel> call, Throwable t) {
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
        hideKeyboard.setupUI(findViewById(R.id.signUp_check_code_layout_id));
    }
}
