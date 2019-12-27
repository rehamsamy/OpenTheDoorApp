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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.m.openthedoorapp.Adapter.CanceledAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.CanceledHistoryModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.ActionsWithServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancledFragmebtTab extends Fragment {

    Dialog myDialog;
    private NetworkAvailable networkAvailable;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noData_txtV;

    // Vars
    private final String TAG = this.getClass().getSimpleName();
    private int current_page = 1;
    private static final int limit = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cancled_tab, container, false);

        myDialog = new Dialog(getActivity());
        networkAvailable = new NetworkAvailable(getActivity());
        recyclerView = view.findViewById(R.id.canceled_recycler);
        progressBar = view.findViewById(R.id.canceled_progressBar_id);
        noData_txtV = view.findViewById(R.id.canceled_noData_txtV);

        if (networkAvailable.isNetworkAvailable()) {
            getCanceledData();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getCanceledData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CanceledHistoryModel> call = serviceInterface.canceledHistory(current_page, limit, FindServiceActivity.userModel.getId(), FindServiceActivity.userModel.getToken());
        call.enqueue(new Callback<CanceledHistoryModel>() {
            @Override
            public void onResponse(Call<CanceledHistoryModel> call, Response<CanceledHistoryModel> response) {
                Log.i(TAG, "onResponse " + response.body().getStatus());
                if (response.body().getStatus()) {
                    List<ServiceItemData> canceledList = response.body().getUserserviceinfo().getCanceled();
                    if (canceledList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noData_txtV.setVisibility(View.GONE);
                        buildCanceledRecycler(canceledList);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noData_txtV.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CanceledHistoryModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildCanceledRecycler(final List<ServiceItemData> canceledList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        //set Adapter ...
        CanceledAdapter adapter = new CanceledAdapter(getActivity(), canceledList);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListner(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                show_popup(position, canceledList.get(position));
            }
        });
    }

    private void show_popup(final int position, final ServiceItemData serviceItemData) {
        ImageView makeCall, provider_imageV;
        TextView  serviceType_txtV, userName_txtV, costOfService_txtV, timeToArrive_txtV;
        Button reorderService_btn;
        MapView mapView;
        GoogleMap googleMap;

        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.canceled_popup_layout);
        provider_imageV = myDialog.findViewById(R.id.canceledDetails_providerImageV);
        serviceType_txtV = myDialog.findViewById(R.id.canceledDetails_serviceType_txtV_val);
        userName_txtV = myDialog.findViewById(R.id.cancelled_userName_txtV_id);
        costOfService_txtV = myDialog.findViewById(R.id.canceledDetails_costOfService_txtV_id);
        timeToArrive_txtV = myDialog.findViewById(R.id.canceledDetails_timeToArrive_txtV_id);
        makeCall = myDialog.findViewById(R.id.canceledDetails_makeCall_icon_id);
        reorderService_btn = myDialog.findViewById(R.id.canceledDetails_reorderService_btn);
        mapView = myDialog.findViewById(R.id.canceledDetails_mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap = googleMap;
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    LatLng latLng = new LatLng(Double.parseDouble(serviceItemData.getProvider_lat()), Double.parseDouble(serviceItemData.getProvider_long()));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(serviceItemData.getProvider_name()));
                    CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(15).bearing(0).tilt(45).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }
        reorderService_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ActionsWithServices.confirmServicee(getActivity(), serviceItemData.getProvider_minutes_to_arrive(), serviceItemData.getProvider_hour_to_finish(), serviceItemData.getService_id()
                        , serviceItemData.getProvider_id(), FindServiceActivity.userModel.getId(), "notes", FindServiceActivity.userModel.getToken(), getString(R.string.sending));

            }
        });
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callPhoneNumber(serviceItemData.getProvider_phone());
            }
        });
        // SetData to Views
        Glide.with(getActivity())
                .load(serviceItemData.getProvider_image())
                .centerCrop()
                .placeholder(R.drawable.review_user_pic_withframe)
                .into(provider_imageV);
        userName_txtV.setText(serviceItemData.getProvider_name());
        serviceType_txtV.setText(serviceItemData.getService_name_ar());
        costOfService_txtV.setText(String.valueOf(serviceItemData.getProvider_price_per_hour()));
        timeToArrive_txtV.setText(String.valueOf(serviceItemData.getProvider_minutes_to_arrive()));
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
