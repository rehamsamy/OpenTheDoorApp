package com.m.openthedoorapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.UserModel;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.SharedPreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    @BindView(R.id.nv)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout_id)
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.current_location)
    TextView currentAddress_txtV;
    @BindView(R.id.homeSearch_ed_id)
    SearchView searchView;
    @BindView(R.id.findService_mapView_id)
    MapView mapView;

    private TextView nav_UserName_txtV;
    private ImageView nav_imageV;

    //Vars ....
    GoogleMap mMap;
    Marker marker;
    public static UserModel userModel;
    private boolean mLocationPermissionGrated = false;
    private LocationManager locationManager;
    private NetworkAvailable networkAvailable;
    private SharedPreferenceManager preferenceManager;

    private final String TAG = this.getClass().getSimpleName();
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int location_permission_request_code = 1234;
    private static final int Dialog_Error_Request = 9001;
    private static final float default_Zoom = 15f;
    private static final float MIN_DISTANCE = 1000;
    private static final long MIN_TIME = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_service);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        hideKeyboard(this);
        networkAvailable = new NetworkAvailable(this);
        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(FindServiceActivity.this, SharedPreferenceManager.PREFERENCE_NAME);
        if (!networkAvailable.isNetworkAvailable())
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        userModel = new UserModel();
        if (getIntent().hasExtra("user_data")) {
            userModel = getIntent().getParcelableExtra("user_data");
        }
        // Set UserData to Navigation
        View hView = navigationView.getHeaderView(0);
        nav_UserName_txtV = (TextView) hView.findViewById(R.id.header_txtV_id);
        nav_imageV = (ImageView) hView.findViewById(R.id.header_imageV_id);
        nav_UserName_txtV.setText(userModel.getName());
        Glide.with(FindServiceActivity.this).load(userModel.getImage()).placeholder(R.drawable.review_user_pic_withframe).fitCenter().into(nav_imageV);

        if (isSericeOk()) {
            getLocationPermission();
        }
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    mMap.clear();
                    if (marker != null)
                        marker.remove();
                    Geocoder geocoder = new Geocoder(FindServiceActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isSericeOk() {
        Log.i(TAG, "is Service Ok Started ...");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(FindServiceActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Service Ok is OK");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.i(TAG, "Service Error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(FindServiceActivity.this, available, Dialog_Error_Request);
            dialog.show();
            return false;
        } else {
            Toast.makeText(this, "You canot make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
        Log.i(TAG, "onLocation Permissions Started ...");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "on Request permission Called...");
        mLocationPermissionGrated = false;
        switch (requestCode) {
            case location_permission_request_code:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGrated = false;
                            Log.i(TAG, "on Request permission Failed ...");
                            return;
                        }
                    }
                    mLocationPermissionGrated = true;
                    Log.i(TAG, "on Request permission granted ...");
                    // Initialize Map
//                    init();
                }
                break;
            default:
                Toast.makeText(this, "Permission is Cancelled", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mLocationPermissionGrated) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void getLocationAddress(Location currentLocation) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(FindServiceActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.d("max", " " + addresses.get(0).getMaxAddressLineIndex());
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            currentAddress_txtV.setText(address);
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

    @OnClick(R.id.log_out_sidemenu)
    void logOut() {
        drawerLayout.closeDrawers();
        logoutOfApp();
    }

    @OnClick(R.id.find_service_btn_id)
    void getService() {
        Intent intent = new Intent(FindServiceActivity.this, SelectService.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null) {
                getLocationAddress(location);
                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), default_Zoom, "My Location");
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

    private void logoutOfApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FindServiceActivity.this);
        builder.setMessage(getString(R.string.outofApp))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //super.onBackPressed();
                        dialogInterface.dismiss();
                        //-------------------------------------------------------
                        preferenceManager.removeValue(SharedPreferenceManager.USER_DATA);
                        startActivity(new Intent(FindServiceActivity.this, LoginActivity.class));
                        finish();
                        // -------------------------------------------------------
                    }
                })
                .setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.profile:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.history:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.reviews:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, Reviews.class);
                startActivity(intent);
                break;
            case R.id.nav_balance:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.promocode:
                drawerLayout.closeDrawers();
                startActivity(new Intent(FindServiceActivity.this, PromoCode.class));
                break;
            case R.id.notifications:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, NotificationsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.contact_us:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, ContactUs.class);
                startActivity(intent);
                break;
            case R.id.about_company:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_inviteFriend:
                drawerLayout.closeDrawers();
                intent = new Intent(FindServiceActivity.this, InviteContactsActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}