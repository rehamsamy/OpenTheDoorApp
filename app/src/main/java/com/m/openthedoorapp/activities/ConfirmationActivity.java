package com.m.openthedoorapp.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.ConfirmationResponseModel;
import com.m.openthedoorapp.model.ServiceProviderModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    @BindView(R.id.location_text)
    TextView location_txtV;
    @BindView(R.id.service_type_text)
    TextView service_type_txtV;
    @BindView(R.id.user_name)
    TextView user_name_txtV;
    @BindView(R.id.confirmation_ratingBar_id)
    RatingBar confirmation_ratingBar;
    @BindView(R.id.price_val_txtV)
    TextView price_val_txtV;
    @BindView(R.id.arrival_time_val_txtV_id)
    TextView arrival_time_val_txtV;
    @BindView(R.id.paymentMethod_val_txtV_id)
    TextView paymentMethod_val_txtV;

    private String selected_service;
    private int selected_service_id;
    private double lat, lng;
    private String address;
    private final String TAG = this.getClass().getSimpleName();
    private ServiceProviderModel.ProviderItem selected_Provider;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private int payment_request_code = 20;
    private String payment_response = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("selected_service")) {
            selected_service = getIntent().getStringExtra("selected_service");
            selected_service_id = getIntent().getExtras().getInt("selected_service_id");
            lat = getIntent().getExtras().getDouble("user_lat");
            lng = getIntent().getExtras().getDouble("user_lng");
            address = getIntent().getStringExtra("address");
            selected_Provider = getIntent().getParcelableExtra("provider_data");

            location_txtV.setText(address);
            service_type_txtV.setText(selected_service);
            user_name_txtV.setText(selected_Provider.getName());
            price_val_txtV.setText(String.valueOf(selected_Provider.getService_price()));
            confirmation_ratingBar.setRating(selected_Provider.getRat_count());
            arrival_time_val_txtV.setText(new DecimalFormat("##.##").format(Double.parseDouble(selected_Provider.getDistance())));
        }
        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.serviceConfirmation_back_txtV_id)
    public void setBack() {
        finish();
    }

    @OnClick(R.id.make_call)
    void makeACall() {
        callPhoneNumber(selected_Provider.getPhone());
    }

    @OnClick(R.id.confirm_button)
    void confirmOrder() {
        if (networkAvailable.isNetworkAvailable()) {
            final ProgressDialog dialog = dialogUtil.showProgressDialog(ConfirmationActivity.this, getString(R.string.sending), false);
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<ConfirmationResponseModel> call = serviceInterface.confirmService(25, 20, selected_service_id, FindServiceActivity.userModel.getId(),
                    selected_Provider.getProvider_id(), "current", "", "notes", "10", FindServiceActivity.userModel.getToken());
            call.enqueue(new Callback<ConfirmationResponseModel>() {
                @Override
                public void onResponse(Call<ConfirmationResponseModel> call, Response<ConfirmationResponseModel> response) {
                    dialog.dismiss();
                    if (response.body().getStatus()) {
                        Toast.makeText(ConfirmationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ConfirmationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ConfirmationResponseModel> call, Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.select_payment_type_btn_id)
    void select_payment() {
        Intent intent = new Intent(ConfirmationActivity.this, PaymentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.scheduled_button)
    void cheduleOrder() {
        Date currentTime = Calendar.getInstance().getTime();
        Log.i(TAG, currentTime.toString());
    }

    @OnClick(R.id.cancel_button)
    void cancelOrder() {
        finish();
    }

    public void callPhoneNumber(String phoneNum) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == payment_request_code) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String payment_state = data.getStringExtra("payment_state");
                Log.i("payment: ", payment_state);
                if (payment_state.equals("2")) {
                    paymentMethod_val_txtV.setText(getResources().getString(R.string.payment_cash_on_delivery));
                } else if (payment_state.equals("3")) {

                    openPaymentPage(selected_Provider.getService_price());

//                    ApiServiceInterface apiServiceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
//                    Call<PaymentInfoModel> call = apiServiceInterface.getPaymentInfo();
//                    call.enqueue(new Callback<PaymentInfoModel>() {
//                        @Override
//                        public void onResponse(Call<PaymentInfoModel> call, Response<PaymentInfoModel> response) {
//                            dialog.dismiss();
//                            if (response.body().getStatus()) {
//                                List<PaymentInfoModel.PaymentInfo> paymentInfoList = response.body().getPaynment();
//                                String merchant_email = paymentInfoList.get(0).getValue();
//                                String secret_key = paymentInfoList.get(1).getValue();
//                                String site_url = paymentInfoList.get(2).getValue();
//                                String ip_merchant = paymentInfoList.get(3).getValue();
//                                String secureHash = paymentInfoList.get(4).getValue();
//                                openPaymentPage(cart_total_cost, merchant_email, secret_key, site_url, ip_merchant, secureHash);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<PaymentInfoModel> call, Throwable t) {
//                            t.printStackTrace();
//                            dialog.dismiss();
//                        }
//                    });

//                    payment_val_txtV.setText(getResources().getString(R.string.online_byCreditCard));
                } else if (payment_state.equals("1")) {
//                    paymentMethod_val_txtV.setText(getResources().getString(R.string.payment_wallet));
                    openPaymentPage(selected_Provider.getService_price());
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE && data != null) {
            Log.e("RESPONSE_CODE: ", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("TRANSACTION_ID: ", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            payment_response = data.getStringExtra(PaymentParams.RESPONSE_CODE);
//            Log.e("AMOUNT: ", data.getDoubleExtra(PaymentParams.AMOUNT, 0.0) + "");
//            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
//                Log.e("TOKEN: ", data.getStringExtra(PaymentParams.TOKEN));
//                Log.e("CUSTOMER_EMAIL: ", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
//                Log.e("CUSTOMER_PASSWORD: ", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
//            }
            if (payment_response.equals("100")) {
                paymentMethod_val_txtV.setText(getResources().getString(R.string.payment_online_credit));
                Toast.makeText(this, getString(R.string.payment_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
        }
    }

    //InFlate Payment Page ...
    private void openPaymentPage(int cart_total_cost) {
        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "merchant-email@example.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY,"secret key");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE,PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Test Paytabs android library");
        in.putExtra(PaymentParams.AMOUNT, cart_total_cost);

        in.putExtra(PaymentParams.CURRENCY_CODE, "BHD");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733");
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, "customer-email@example.com");
        in.putExtra(PaymentParams.ORDER_ID, "123456");
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

//Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");

//Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }
}
