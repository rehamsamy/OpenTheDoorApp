package com.m.openthedoorapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.RegisterationModel;
import com.m.openthedoorapp.model.UserLoginModel;
import com.m.openthedoorapp.model.UserModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.HideKeyboard;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp2Activity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.signUp_name_ed_id)
    EditText name_ed;
    @BindView(R.id.signUp_email_ed_id)
    EditText email_ed;
    @BindView(R.id.sign_user_image)
    ImageView user_image;

    private NetworkAvailable networkAvailable;
    String storage_permission[];
    public static MultipartBody.Part body = null;
    private static final int storage_request_code = 100;
    private static final int image_pick_gallery_code = 200;

    private int user_id;
    private String userPhone, token;
    private UserModel userModel;
    private DialogUtil dialogUtil;
    private HideKeyboard hideKeyboard;
    private SharedPreferenceManager preferenceManager;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        hideKeyboard = new HideKeyboard(SignUp2Activity.this);
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(SignUp2Activity.this, SharedPreferenceManager.PREFERENCE_NAME);
        if (getIntent().hasExtra("id")) {
            user_id = getIntent().getExtras().getInt("id");
            userPhone = getIntent().getExtras().getString("phone");
            token = getIntent().getExtras().getString("token");
        }
    }

    @OnClick(R.id.signUp1_btn_id)
    void signUp() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                    && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))) {
                final ProgressDialog dialog = dialogUtil.showProgressDialog(SignUp2Activity.this, getString(R.string.signing), false);
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, name_ed.getText().toString());
                userModel = new UserModel();
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = serviceInterface.updateProfileData(NamePart, email_ed.getText().toString(), userPhone, token, SignUp2Activity.body);
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        Log.i(TAG, "onResponse Started " + response.body().getStatus());
                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            userModel = new UserModel();
                            UserLoginModel registerationModel = response.body();
                            UserLoginModel.User userData = registerationModel.getUser();
                            userModel.setId(userData.getId());
                            userModel.setName(userData.getName());
                            userModel.setEmail(userData.getEmail());
                            userModel.setPhone(userData.getPhone());
                            userModel.setImage(userData.getUser_image());
                            userModel.setToken(registerationModel.getToken());

                            // Convert User Data to Gon OBJECT ...
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            preferenceManager.setValue(SharedPreferenceManager.USER_DATA, user_data);

                            Toast.makeText(SignUp2Activity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp2Activity.this, FindServiceActivity.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUp2Activity.this, getString(R.string.email_already_found), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.sign_add_user_image)
    void addImage() {
        if (!checkStoragePermission()) {
            // Camera permission isnot allowed
            requestStoragePermission();
        } else {
            // Camera Permission is allowed
            pickGallery();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard.setupUI(findViewById(R.id.login_layout_id));
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storage_permission, storage_request_code);
    }

    private void pickGallery() {
        Intent gallery_intent = new Intent(Intent.ACTION_PICK);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent, image_pick_gallery_code);
    }

    // Handle permission result...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case image_pick_gallery_code:
                if (grantResults.length > 0) {
                    boolean write_storage_accepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    boolean camera_accepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    if (camera_accepted && write_storage_accepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == image_pick_gallery_code) {
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    user_image.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }
}