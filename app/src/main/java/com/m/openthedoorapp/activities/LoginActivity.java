package com.m.openthedoorapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.UserLoginModel;
import com.m.openthedoorapp.model.UserModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.HideKeyboard;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.login_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.login_password_ed_id)
    EditText password_ed;

    private HideKeyboard hideKeyboard;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private UserModel userModel;
    private SharedPreferenceManager preferenceManager;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(LoginActivity.this);
        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(LoginActivity.this, SharedPreferenceManager.PREFERENCE_NAME);
        phone_ed.setText(preferenceManager.getValue(SharedPreferenceManager.USER_PHONE, ""));//"No name defined" is the default value.
        password_ed.setText(preferenceManager.getValue(SharedPreferenceManager.PASSWORD, "")); //0 is the default value.
    }

    @OnClick(R.id.signIn_btn_id)
    void login() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(password_ed, getString(R.string.required))) {

                if (password_ed.getText().toString().length() < 6) {
                    password_ed.setError(getResources().getString(R.string.pass_is_weak));
                    password_ed.requestFocus();
                    return;
                }
//               // String token = FirebaseInstanceId.getInstance().getToken();
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(LoginActivity.this, getString(R.string.logining), false);

                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = serviceInterface.userLogin(phone_ed.getText().toString(), password_ed.getText().toString());
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        userModel = new UserModel();
                        UserLoginModel userLoginModel = response.body();
                        if (userLoginModel.getStatus()) {
                            UserLoginModel.User user = userLoginModel.getUser();
                            userModel.setId(user.getId());
                            userModel.setName(user.getName());
                            userModel.setEmail(user.getEmail());
                            userModel.setPhone(user.getPhone());
                            userModel.setImage(user.getUser_image());
                            userModel.setToken(userLoginModel.getToken());

                            // Convert User Data to Gon OBJECT ...
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            preferenceManager.setValue(SharedPreferenceManager.USER_DATA, user_data);

                            Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, FindServiceActivity.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, userLoginModel.getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.sign_up)
    void signUp() {
        startActivity(new Intent(LoginActivity.this, SignUp3Activity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    @OnClick(R.id.forget_password_txtV_id)
    void goToForgetPass() {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard.setupUI(findViewById(R.id.login_layout_id));
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferenceManager.setValue(SharedPreferenceManager.USER_PHONE, phone_ed.getText().toString());
        preferenceManager.setValue(SharedPreferenceManager.PASSWORD, password_ed.getText().toString());
    }

}