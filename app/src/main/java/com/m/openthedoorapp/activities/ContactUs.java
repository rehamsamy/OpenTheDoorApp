package com.m.openthedoorapp.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ContactUsModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends AppCompatActivity {

    @BindView(R.id.contactus_back_txtV_id)
    ImageView back;
    @BindView(R.id.phone_val_textV_id)
    TextView phone_textV;
    @BindView(R.id.website_text)
    TextView website_textV;
    @BindView(R.id.address_text)
    TextView address_textV;
    @BindView(R.id.contact_progress_id)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.contactData_layout_id)
    ConstraintLayout constraintLayout;

    private NetworkAvailable networkAvailable;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable()) {
            getContactInfo();
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getContactInfo() {
        loadingIndicatorView.show();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ContactUsModel> call = serviceInterface.getContactData();
        call.enqueue(new Callback<ContactUsModel>() {
            @Override
            public void onResponse(Call<ContactUsModel> call, Response<ContactUsModel> response) {
                Log.i(TAG, "onResponse " + response.body().getStatus());
                constraintLayout.setVisibility(View.VISIBLE);
                if (response.body().getStatus()) {
                    ContactUsModel.ContactData contactData = response.body().getAppinfo();
                    phone_textV.setText(contactData.getWebsite_phone());
                    website_textV.setText(contactData.getWebsite_url());
                    address_textV.setText(contactData.getWebsite_address_en());
                }
                loadingIndicatorView.hide();
            }

            @Override
            public void onFailure(Call<ContactUsModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                loadingIndicatorView.hide();
            }
        });
    }

    @OnClick(R.id.contactus_back_txtV_id)
    public void setBack() {
        finish();
    }
}
