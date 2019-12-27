package com.m.openthedoorapp.activities;

import android.content.Intent;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.m.openthedoorapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_back_txtV_id)
    ImageView back;
    @BindView(R.id.profile_userName_txtV)
    TextView userName_txtV;
    @BindView(R.id.profile_imageV_id)
    ImageView userImageV;
    @BindView(R.id.profile_userPhone_txtV)
    TextView userPhone_txtV;
    @BindView(R.id.profile_userEmail_txtV)
    TextView userEmail_txtV;
    @BindView(R.id.edit_profile_date)
    Button editProfile_btn;
    @BindView(R.id.change_password)
    Button changePass_btn;
    @BindView(R.id.profile_swipeRefreshLayout)
    SwipeRefreshLayout swipe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setViewsData();

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ProfileActivity.this != null) {
                            setViewsData();
                            swipe_layout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
        swipe_layout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private void setViewsData() {
        userName_txtV.setText(FindServiceActivity.userModel.getName());
        userEmail_txtV.setText(FindServiceActivity.userModel.getEmail());
        userPhone_txtV.setText(FindServiceActivity.userModel.getPhone());

        Glide.with(ProfileActivity.this).load(FindServiceActivity.userModel.getImage()).placeholder(R.drawable.review_user_pic_withframe).fitCenter().into(userImageV);
    }

    @OnClick(R.id.edit_profile_date)
    public void editProfile() {
        startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
    }

    @OnClick(R.id.change_password)
    public void changePassword() {
        startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
    }

    @OnClick(R.id.myWallet_btn_id)
    void myWallet(){
        startActivity(new Intent(ProfileActivity.this, MyWalletActivity.class));
    }

    @OnClick(R.id.profile_back_txtV_id)
    void goBack() {
        finish();
    }
}