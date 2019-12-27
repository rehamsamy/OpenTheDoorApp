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

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.m.openthedoorapp.Adapter.CurrentRecyclerAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.activities.CancelService;
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.CurrentHistoryModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentFragmentTab extends Fragment {

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_current_tab, container, false);

        myDialog = new Dialog(getActivity());
        networkAvailable = new NetworkAvailable(getActivity());
        recyclerView = view.findViewById(R.id.current_recycler);
        progressBar = view.findViewById(R.id.current_progressBar_id);
        noData_txtV = view.findViewById(R.id.noData_txtV_id);
        if (networkAvailable.isNetworkAvailable()) {
            getCurrentData();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void getCurrentData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<CurrentHistoryModel> call = serviceInterface.currentHistory(current_page, limit, FindServiceActivity.userModel.getId(), FindServiceActivity.userModel.getToken());
        call.enqueue(new Callback<CurrentHistoryModel>() {
            @Override
            public void onResponse(Call<CurrentHistoryModel> call, Response<CurrentHistoryModel> response) {
                if (response.body().getStatus()) {
                    List<ServiceItemData> currentList = response.body().getUserserviceinfo().getCurrent();
                    if (currentList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noData_txtV.setVisibility(View.GONE);
                        buildCurrentRecycler(currentList);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noData_txtV.setVisibility(View.VISIBLE);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CurrentHistoryModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void buildCurrentRecycler(final List<ServiceItemData> currentList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        // setAdapter...
        CurrentRecyclerAdapter adapter = new CurrentRecyclerAdapter(getActivity(), currentList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClick(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                show_popup(position, currentList.get(position));
            }
        });
    }

    private void show_popup(final int position, final ServiceItemData itemData) {
        ImageView makeCall, provider_imageV;
        Button cancelService_btn;
        TextView providerName_txtV, seviceType_txtV, serviceHourPrice_txtV, minToArrive_txtV;
        RatingBar ratingBar;
        MapView mapView;
        GoogleMap googleMap;

        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.current_popup_layout);
        makeCall = myDialog.findViewById(R.id.currentDetails_makeCall_icon_id);
        cancelService_btn = myDialog.findViewById(R.id.currentDetailse_endService_btn);
        providerName_txtV = myDialog.findViewById(R.id.current_userName_txtV_id);
        seviceType_txtV = myDialog.findViewById(R.id.serviceType_txtV_val);
        serviceHourPrice_txtV = myDialog.findViewById(R.id.currentDetails_costOfService_txtV_id);
        minToArrive_txtV = myDialog.findViewById(R.id.currentDetails_timeToArrive_txtV_id);
        ratingBar = myDialog.findViewById(R.id.currentDetails_item_rating);
        provider_imageV = myDialog.findViewById(R.id.currentDetails_provider_imageV_id);
        mapView = myDialog.findViewById(R.id.currentDetails_mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap = googleMap;
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    LatLng latLng = new LatLng(Double.parseDouble(itemData.getProvider_lat()), Double.parseDouble(itemData.getProvider_long()));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(itemData.getProvider_name()));
                    CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(15).bearing(0).tilt(45).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
        }

        // setData to Views
        Glide.with(getActivity())
                .load(itemData.getProvider_image())
                .centerCrop()
                .placeholder(R.drawable.review_user_pic_withframe)
                .into(provider_imageV);
        providerName_txtV.setText(itemData.getProvider_name());
        seviceType_txtV.setText(itemData.getService_name_ar());
        ratingBar.setRating(itemData.getRat_count());
        serviceHourPrice_txtV.setText(String.valueOf(itemData.getProvider_price_per_hour()));
        minToArrive_txtV.setText(String.valueOf(itemData.getProvider_minutes_to_arrive()));
        cancelService_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Intent intent = new Intent(getActivity(), CancelService.class);
                intent.putExtra("itemData", itemData);
                getActivity().startActivity(intent);
            }
        });
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                callPhoneNumber(itemData.getProvider_phone());
            }
        });

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
