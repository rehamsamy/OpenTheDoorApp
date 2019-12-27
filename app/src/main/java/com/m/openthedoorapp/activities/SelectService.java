package com.m.openthedoorapp.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ServicesResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectService extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    @BindView(R.id.my_spinner)
    AwesomeSpinner spinner;
    @BindView(R.id.select_service_btn_id)
    Button select_service_btn;
    @BindView(R.id.currentLocation_txtV_id)
    TextView currentLocation_txtV;
    @BindView(R.id.mapView_selectService)
    MapView mapView;

    //Vars...
    private GoogleMap mMap;
    private LocationManager locationManager;
    private final String TAG = this.getClass().getSimpleName();
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int location_permission_request_code = 1222;
    private static final int Dialog_Error_Request = 110;
    private static final float default_Zoom = 15f;
    private static final float MIN_DISTANCE = 1000;
    private static final long MIN_TIME = 400;
    private boolean mLocationPermissionGrated = false;

    private int currentPage = 1;
    private int limit = 20;
    private List<String> stringList;
    private String selected_service;
    private int selected_service_pos;
    private double lat, lng;
    private String address;
    Geocoder geocoder;
    List<ServicesResponseModel.ServiceItem> serviceItemList;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_activty);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        // get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(SelectService.this, SharedPreferenceManager.PREFERENCE_NAME);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        if (isSericeOk()) {
            getLocationPermission();
        }
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        geocoder = new Geocoder(this, Locale.getDefault());
        selected_service = getString(R.string.select_service);
        stringList = new ArrayList<>();

        if (!networkAvailable.isNetworkAvailable())
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        else {
            spinner.setSpinnerHint(getString(R.string.select_service));
            spinner.setHintTextColor(Color.GRAY);
            spinner.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);

            final ProgressDialog dialog = dialogUtil.showProgressDialog(SelectService.this, getString(R.string.loading), false);
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<ServicesResponseModel> call = serviceInterface.getUserServices(currentPage, limit, FindServiceActivity.userModel.getToken());
            call.enqueue(new Callback<ServicesResponseModel>() {
                @Override
                public void onResponse(Call<ServicesResponseModel> call, Response<ServicesResponseModel> response) {
                    if (response.body().getStatus()) {
                        serviceItemList = response.body().getServices();
                        for (int i = 0; i < serviceItemList.size(); i++) {
                            if (preferenceManager.getValue(SharedPreferenceManager.LANGUAGE, "").equals("ar"))
                                stringList.add(serviceItemList.get(i).getService_name_ar());
                            else
                                stringList.add(serviceItemList.get(i).getService_name_en());
                        }
                        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(SelectService.this, android.R.layout.simple_spinner_item, stringList);
                        spinner.setAdapter(categoriesAdapter);
                        spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
                            @Override
                            public void onItemSelected(int position, String itemAtPosition) {
                                //TODO YOUR ACTIONS
                                selected_service = itemAtPosition;
                                selected_service_pos = position;
                            }
                        });
                        dialog.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<ServicesResponseModel> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mLocationPermissionGrated) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private boolean isSericeOk() {
        Log.i(TAG, "is Service Ok Started ...");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SelectService.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Service Ok is OK");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.i(TAG, "Service Error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SelectService.this, available, Dialog_Error_Request);
            dialog.show();
            return false;
        } else {
            Toast.makeText(this, "You canot make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void init() {
        MapView mapView = findViewById(R.id.mapView_selectService);
        mapView.getMapAsync(this);
    }

    private void getLocationPermission() {
        Log.i(TAG, "onLocation Permissions Started ...");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGrated = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }
    }

    @OnClick(R.id.select_service_btn_id)
    public void selectService() {
        if (selected_service.equals(getString(R.string.select_service))) {
            Toast.makeText(this, getString(R.string.plz_select_service_first), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SelectService.this, SelectServiceProvider.class);
        intent.putExtra("selected_service", selected_service);
        intent.putExtra("selected_service_id", serviceItemList.get(selected_service_pos).getId());
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("address", address);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.selectService_back_txtV_id)
    void goBack() {
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), default_Zoom, "My Location");
                getLocationAddress(location);
            }
            locationManager.removeUpdates(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getLocationAddress(Location currentLocation) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(SelectService.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.d("max", " " + addresses.get(0).getMaxAddressLineIndex());
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            currentLocation_txtV.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.i(TAG, "moveCamera called");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (!title.equals("My Location")) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng)
                    .title(title);
            mMap.addMarker(markerOptions);
        }
    }
}
