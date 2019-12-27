package com.m.openthedoorapp.fragmentTabs;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.Adapter.CompletedRecyclerAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.CompletedHistoryModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.ActionsWithServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedFragmentTab extends Fragment {

    Dialog myDialog;
    private NetworkAvailable networkAvailable;
    private RecyclerView recyclerView;
    public static ProgressBar progressBar;
    private TextView noData_txtV;

    // Vars
    private final String TAG = this.getClass().getSimpleName();
    private int current_page = 1;
    private static final int limit = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_tab, container, false);

        myDialog = new Dialog(getActivity());
        networkAvailable = new NetworkAvailable(getActivity());
        recyclerView = view.findViewById(R.id.completed_recycler);
        progressBar = view.findViewById(R.id.completed_progressBar_id);
        noData_txtV = view.findViewById(R.id.completed_noData_txtV);

        if (networkAvailable.isNetworkAvailable()) {
            getCompletedData();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getCompletedData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CompletedHistoryModel> call = serviceInterface.completedHistory(current_page, limit, FindServiceActivity.userModel.getId(), FindServiceActivity.userModel.getToken());
        call.enqueue(new Callback<CompletedHistoryModel>() {
            @Override
            public void onResponse(Call<CompletedHistoryModel> call, Response<CompletedHistoryModel> response) {
                Log.i(TAG, "onResponse " + response.body().getStatus());
                if (response.body().getStatus()) {
                    List<ServiceItemData> completedList = response.body().getUserserviceinfo().getCompleted();
                    if (completedList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noData_txtV.setVisibility(View.GONE);
                        buildCompletedRecycler(completedList);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noData_txtV.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CompletedHistoryModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildCompletedRecycler(final List<ServiceItemData> currentList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        // setAdapter...
        CompletedRecyclerAdapter adapter = new CompletedRecyclerAdapter(getActivity(), currentList, getString(R.string.sending));
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListner(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                show_popup(position, currentList.get(position));
            }
        });
    }

    private void show_popup(final int position, final ServiceItemData completedItemData) {
        ImageView makeCall;
        final Button reorderService_btn;
        final TextView userName_txtV, serviceType_txtV, completedServices_txtV, timeToArrive_txtV, costOfService_txtV,
                serviceAllTime_txtV, givenBonuos_txtV, costofProvider_txtV, discount_txtV, totalCost_txtV;
        RatingBar ratingBar;

        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.completed_popup_layout);
        makeCall = myDialog.findViewById(R.id.completeDetails_makeCall_icon_id);
        reorderService_btn = myDialog.findViewById(R.id.completeDetailse_reorderService_btn);
        userName_txtV = myDialog.findViewById(R.id.completedDetails_userName_txtV_id);
        serviceType_txtV = myDialog.findViewById(R.id.completedDetails_serviceType_txtV_val);
        ratingBar = myDialog.findViewById(R.id.completedDetails_popUp_item_rating);
        completedServices_txtV = myDialog.findViewById(R.id.completedDetails_completedServices_txtV_id);
        timeToArrive_txtV = myDialog.findViewById(R.id.currentDetails_timeToArrive_txtV_id);
        costOfService_txtV = myDialog.findViewById(R.id.currentDetails_costOfService_txtV_id);
        serviceAllTime_txtV = myDialog.findViewById(R.id.completedDetails_serviceAllTime_txtV_id);
        givenBonuos_txtV = myDialog.findViewById(R.id.completeDetails_givenBonuos_txtV);
        costofProvider_txtV = myDialog.findViewById(R.id.completeDetails_costofProvider_txtV_id);
        discount_txtV = myDialog.findViewById(R.id.completeDetails_discount_txtV_id);
        totalCost_txtV = myDialog.findViewById(R.id.completeDetails_totalCost_txtV_id);

        reorderService_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                if (networkAvailable.isNetworkAvailable()) {
                    ActionsWithServices.confirmServicee(getActivity(), completedItemData.getProvider_minutes_to_arrive(), completedItemData.getProvider_hour_to_finish(), completedItemData.getService_id(),
                            completedItemData.getProvider_id(), FindServiceActivity.userModel.getId(), "notes", FindServiceActivity.userModel.getToken(), getString(R.string.sending));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callPhoneNumber(completedItemData.getProvider_phone());
            }
        });

        // Set Data to Views...
        userName_txtV.setText(completedItemData.getProvider_name());
        serviceType_txtV.setText(completedItemData.getService_name_ar());
        ratingBar.setRating(completedItemData.getRat_count());
        timeToArrive_txtV.setText(String.valueOf(completedItemData.getProvider_minutes_to_arrive()));
        costOfService_txtV.setText(String.valueOf(completedItemData.getProvider_price_per_hour()));
        costofProvider_txtV.setText(String.valueOf(completedItemData.getCost_of_service_provider()));
        totalCost_txtV.setText(String.valueOf(completedItemData.getTotal()));
        completedServices_txtV.setText(String.valueOf(completedItemData.getWatch()));

        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void callPhoneNumber(String phoneNum) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNum));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNum));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
