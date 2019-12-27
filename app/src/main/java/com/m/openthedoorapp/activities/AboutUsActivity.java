package com.m.openthedoorapp.activities;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.AboutResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.about_back_txtV_id)
    ImageView back_imageV;
    @BindView(R.id.about_txtV_id)
    TextView about_txtV;
    @BindView(R.id.aboutUs_progress_id)
    AVLoadingIndicatorView loadingIndicatorView;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

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
        Call<AboutResponseModel> call = serviceInterface.aboutUs();
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

    @OnClick(R.id.about_back_txtV_id)
    void geBack() {
        finish();
    }
}
