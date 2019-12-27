package com.m.openthedoorapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.UserLoginModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;

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

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.editProfile_back_txtV_id)
    ImageView back;
    @BindView(R.id.profile_add_imageV)
    ImageView add_imageV;
    @BindView(R.id.profile_user_imageV)
    ImageView user_imageV;
    @BindView(R.id.edit_userName_ed)
    EditText userName_ed;
    @BindView(R.id.edit_userEmail_ed)
    EditText userEmail_ed;
    @BindView(R.id.edit_userPhone_ed)
    EditText userPhone_ed;
    @BindView(R.id.save_editing_btn)
    Button save_btn;
    @BindView(R.id.edit_phoneCode_ed_id)
    EditText phoneCode_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    Uri image_uri;
    MultipartBody.Part body = null;
    private final String TAG = this.getClass().getSimpleName();

    String storage_permission[];
    private static final int storage_request_code = 20;
    private static final int image_pick_gallery_code = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        dialogUtil = new DialogUtil();
        networkAvailable = new NetworkAvailable(this);
        phoneCode_ed.setEnabled(false);
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        userName_ed.setText(FindServiceActivity.userModel.getName());
        userEmail_ed.setText(FindServiceActivity.userModel.getEmail());
        userPhone_ed.setText(FindServiceActivity.userModel.getPhone());
        Glide.with(EditProfileActivity.this).load(FindServiceActivity.userModel.getImage()).placeholder(R.drawable.review_user_pic_withframe).fitCenter().into(user_imageV);
    }

    @OnClick(R.id.editProfile_back_txtV_id)
    public void setBack() {
        finish();
    }

    @OnClick(R.id.profile_add_imageV)
    public void addImage() {
        try {
            if ((ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    && ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditProfileActivity.this, storage_permission, image_pick_gallery_code);
            } else {
                pickGallery();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.save_editing_btn)
    public void saveData() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(userName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(userEmail_ed, getString(R.string.required))
                    && FUtilsValidation.isValidEmail(userEmail_ed, getString(R.string.enter_valid_email))
                    && !FUtilsValidation.isEmpty(userPhone_ed, getString(R.string.required))) {

                final ProgressDialog dialog = dialogUtil.showProgressDialog(EditProfileActivity.this, getString(R.string.signing), false);
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, userName_ed.getText().toString().trim());
                ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = apiServiceInterface.updateProfileData( NamePart,
                        userEmail_ed.getText().toString(), phoneCode_ed.getText().toString() + userPhone_ed.getText().toString(), FindServiceActivity.userModel.getToken(), body);
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        Log.i(TAG, "onResponse " + response.body().getStatus());
                        if (response.body().getStatus()) {
                            dialog.dismiss();
                            UserLoginModel.User user = response.body().getUser();
                            FindServiceActivity.userModel.setToken(response.body().getToken());
                            FindServiceActivity.userModel.setName(user.getName());
                            FindServiceActivity.userModel.setEmail(user.getEmail());
                            FindServiceActivity.userModel.setPhone(user.getPhone());
                            FindServiceActivity.userModel.setImage(user.getUser_image());
                            Toast.makeText(EditProfileActivity.this, getString(R.string.profile_updated_success), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                    }
                });

            }
        }
    }

    private void pickGallery() {
        Intent gallery_intent = new Intent(Intent.ACTION_PICK);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent, image_pick_gallery_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == image_pick_gallery_code) {
                // get image from camera now and crop it
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    user_imageV.setImageBitmap(bitmap);
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