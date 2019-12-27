package com.m.openthedoorapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.m.openthedoorapp.model.CancelServiceModel;
import com.m.openthedoorapp.model.ConfirmationResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionsWithServices {

    public static DialogUtil dialogUtil = new DialogUtil();

    public static void cancelServicee(final Activity activity, int service_id, int user_id, String notes, String user_token, String send) {
        final ProgressDialog dialog = dialogUtil.showProgressDialog(activity, send, false);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CancelServiceModel> call = serviceInterface.cancel_Service(service_id, user_id, notes, user_token);
        call.enqueue(new Callback<CancelServiceModel>() {
            @Override
            public void onResponse(Call<CancelServiceModel> call, Response<CancelServiceModel> response) {
                dialog.dismiss();
                if (response.body().getStatus()) {
                    Toast.makeText(activity, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, response.body().getMessages(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CancelServiceModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    public static void confirmServicee(final Activity activity, int minutes_to_arrive, int hour_to_finish, int service_id, int provider_id, int user_id, String notes, String user_token, String send) {
        final ProgressDialog dialog = dialogUtil.showProgressDialog(activity, send, false);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ConfirmationResponseModel> call = serviceInterface.confirmService(minutes_to_arrive, hour_to_finish,
                service_id, user_id, provider_id, "current", "", notes, "10", user_token);
        call.enqueue(new Callback<ConfirmationResponseModel>() {
            @Override
            public void onResponse(Call<ConfirmationResponseModel> call, Response<ConfirmationResponseModel> response) {
                dialog.dismiss();
                if (response.body().getStatus()) {
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConfirmationResponseModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }
}
