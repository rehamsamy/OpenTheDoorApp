package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.CancelServiceModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.ActionsWithServices;
import com.m.openthedoorapp.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelService extends AppCompatActivity {

    @BindView(R.id.cancelService_back_txtV_id)
    ImageView back;
    @BindView(R.id.cancel_service_reasons_ed)
    EditText reasons_ed;
    @BindView(R.id.cancel_service_button)
    Button cancelService_btn;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    ServiceItemData itemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_service);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("itemData")) {
            itemData = getIntent().getParcelableExtra("itemData");
        }
    }

    @OnClick(R.id.cancelService_back_txtV_id)
    public void setBack() {
        finish();
    }

    @OnClick(R.id.cancel_service_button)
    public void cancelUserService() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(reasons_ed, getString(R.string.required))) {

//                ActionsWithServices.cancelServicee(CancelService.this, itemData.getService_id(), FindServiceActivity.userModel.getId(), reasons_ed.getText().toString(), FindServiceActivity.userModel.getToken(), getString(R.string.sending));
                final ProgressDialog dialog = dialogUtil.showProgressDialog(CancelService.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<CancelServiceModel> call = serviceInterface.cancel_Service(itemData.getService_id(), itemData.getUser_id(), reasons_ed.getText().toString(), FindServiceActivity.userModel.getToken());
                call.enqueue(new Callback<CancelServiceModel>() {
                    @Override
                    public void onResponse(Call<CancelServiceModel> call, Response<CancelServiceModel> response) {

                        dialog.dismiss();
                        if (response.body().getStatus()) {
                            Toast.makeText(CancelService.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CancelService.this, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CancelServiceModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

    }
}
