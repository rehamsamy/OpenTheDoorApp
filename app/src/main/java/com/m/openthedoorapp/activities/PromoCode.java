package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.SendCopounResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCode extends AppCompatActivity {

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @BindView(R.id.promoCode_ed_id)
    EditText promoCode_ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.promoCode_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.get_gift_button)
    void sendPromoCode() {
        if (networkAvailable.isNetworkAvailable()) {
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<SendCopounResponseModel> call = serviceInterface.sendCoupon(FindServiceActivity.userModel.getId(), promoCode_ed.getText().toString(), FindServiceActivity.userModel.getToken());
            call.enqueue(new Callback<SendCopounResponseModel>() {
                @Override
                public void onResponse(Call<SendCopounResponseModel> call, Response<SendCopounResponseModel> response) {
                    if (response.body().getStatus()) {
                        Toast.makeText(PromoCode.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PromoCode.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SendCopounResponseModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
    }
}
