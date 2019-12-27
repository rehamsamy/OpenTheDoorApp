package com.m.openthedoorapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.m.openthedoorapp.Adapter.ServiceProviderAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.interfaces.ProviderRecyclerItemClick;
import com.m.openthedoorapp.model.ServiceProviderModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectServiceProvider extends AppCompatActivity {

    @BindView(R.id.serviceProvider_toolbar)
    Toolbar toolbar;
    @BindView(R.id.serviceProvider_recyclerV)
    RecyclerView providerRecycler;
    @BindView(R.id.serviceProvider_state_progress)
    StateProgressBar stateProgressBar;
    @BindView(R.id.no_providers_txtV_id)
    TextView no_providers_txtV;
    @BindView(R.id.serviceProvider_progressBar_id)
    ProgressBar loading_progressBar;
    @BindView(R.id.serviceProvider_continue_btn_id)
    Button continue_btn;

    private NetworkAvailable networkAvailable;
    ServiceProviderAdapter adapter;
    private String selected_service;
    private int selected_service_id;
    private int currentPage = 1;
    private int limit = 20;
    private double lat, lng;
    private String address;
    private final String TAG = this.getClass().getSimpleName();
    private ServiceProviderModel.ProviderItem selected_Provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_provider);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_filter);
        toolbar.setOverflowIcon(drawable);

        if (getIntent().hasExtra("selected_service")) {
            selected_service = getIntent().getStringExtra("selected_service");
            selected_service_id = getIntent().getExtras().getInt("selected_service_id");
            lat = getIntent().getExtras().getDouble("lat");
            lng = getIntent().getExtras().getDouble("lng");
            address = getIntent().getStringExtra("address");
            Log.i(TAG, selected_service);
            Log.i(TAG, selected_service_id + "");
            Log.i(TAG, lat + "");
            Log.i(TAG, lng + "");
            Log.i(TAG, address + "");
        }
        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable()) {
            getServiceProviders(selected_service_id, "lp");
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getServiceProviders(int selected_service_id, String sort_type) {
        loading_progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ServiceProviderModel> call = serviceInterface.getServiceProviders(currentPage, limit, selected_service_id, lng, lat, FindServiceActivity.userModel.getToken(), sort_type);
        call.enqueue(new Callback<ServiceProviderModel>() {
            @Override
            public void onResponse(Call<ServiceProviderModel> call, Response<ServiceProviderModel> response) {
                if (response.body().getStatus()) {
                    if (response.body().getProviderservice().size() > 0) {
                        no_providers_txtV.setVisibility(View.GONE);
                        providerRecycler.setVisibility(View.VISIBLE);
                        buildProvidersRecycler(response.body().getProviderservice());
                    } else {
                        providerRecycler.setVisibility(View.GONE);
                        no_providers_txtV.setVisibility(View.VISIBLE);
                    }
                    loading_progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ServiceProviderModel> call, Throwable t) {
                t.printStackTrace();
                loading_progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildProvidersRecycler(final List<ServiceProviderModel.ProviderItem> providersList) {
        providerRecycler.setLayoutManager(new LinearLayoutManager(this));
        providerRecycler.setHasFixedSize(true);

        adapter = new ServiceProviderAdapter(this, providersList);
        providerRecycler.setAdapter(adapter);

        // Handle Item Click Listner ...
        adapter.setItemClickListner(new ProviderRecyclerItemClick() {
            @Override
            public void OnItemClick(int position) {
                ServiceProviderAdapter.row_index = position;
                adapter.notifyDataSetChanged();
                selected_Provider = providersList.get(position);
                continue_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void makeCallClick(int position) {
                callPhoneNumber(providersList.get(position).getPhone());
            }
        });
    }

    public void callPhoneNumber(String phoneNum) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.service_provider_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nearest:
                ServiceProviderAdapter.row_index = -1;
                getServiceProviders(selected_service_id, "lp");
            case R.id.low_price:
                ServiceProviderAdapter.row_index = -1;
                getServiceProviders(selected_service_id, "lp");
                break;
            case R.id.high_rate:
                ServiceProviderAdapter.row_index = -1;
                getServiceProviders(selected_service_id, "hr");
                break;
            default:
        }
        return true;
    }

    @OnClick(R.id.serviceProvider_continue_btn_id)
    void confirmationContinue() {
        Intent intent = new Intent(SelectServiceProvider.this, ConfirmationActivity.class);
        intent.putExtra("selected_service", selected_service);
        intent.putExtra("selected_service_id", selected_service_id);
        intent.putExtra("user_lat", lat);
        intent.putExtra("user_lng", lng);
        intent.putExtra("address", address);
        intent.putExtra("provider_data", selected_Provider);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.serviceProvider_back_txtV_id)
    void goBack() {
        finish();
    }
}