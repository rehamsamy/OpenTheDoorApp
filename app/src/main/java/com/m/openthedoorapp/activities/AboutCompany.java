package com.m.openthedoorapp.activities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.AboutResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutCompany extends AppCompatActivity {

    @BindView(R.id.aboutCompany_txtV_id)
    TextView about_txtV;
    @BindView(R.id.aboutCompany_progress_id)
    AVLoadingIndicatorView loadingIndicatorView;

    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_company);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);

        if (networkAvailable.isNetworkAvailable()) {
            getAboutData();
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAboutData() {
        // Show Progress Dialog
        loadingIndicatorView.show();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AboutResponseModel> call = serviceInterface.aboutCompany();
        call.enqueue(new Callback<AboutResponseModel>() {
            @Override
            public void onResponse(Call<AboutResponseModel> call, Response<AboutResponseModel> response) {
                if (response.body().getStatus()) {
                    about_txtV.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        about_txtV.setText(Html.fromHtml(response.body().getAboutus().getContent_en(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        about_txtV.setText(response.body().getAboutus().getContent_en());
                    }
                }
                loadingIndicatorView.hide();
            }

            @Override
            public void onFailure(Call<AboutResponseModel> call, Throwable t) {
                t.printStackTrace();
                loadingIndicatorView.hide();
            }
        });
    }
}
