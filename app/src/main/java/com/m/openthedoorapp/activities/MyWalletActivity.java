package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.utils.SharedPreferenceManager;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

public class MyWalletActivity extends AppCompatActivity {

    @BindView(R.id.mahfza_val_txtV_id)
    TextView mahfza_val_txtV;

    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        // get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(MyWalletActivity.this, SharedPreferenceManager.PREFERENCE_NAME);
    }

    @OnClick(R.id.add_money_btn_id)
    void add_balance(){
        showPopUp();
    }

    private void showPopUp() {
        final Dialog pass_Dialog = new Dialog(this, R.style.dialog_style);
        pass_Dialog.setContentView(R.layout.order_state_popup);
        final RatingBar ratingBar = pass_Dialog.findViewById(R.id.ratingBar_item_id);
        final EditText amount_ed = pass_Dialog.findViewById(R.id.review_ed_id);
        final Button add_btn = pass_Dialog.findViewById(R.id.add_rate_btn_id);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        pass_Dialog.getWindow().setLayout(width,  ViewGroup.LayoutParams.WRAP_CONTENT);
        pass_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        pass_Dialog.setCancelable(true);
        pass_Dialog.setCanceledOnTouchOutside(true);
        pass_Dialog.show();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cliclk Here ....
                pass_Dialog.dismiss();

                openPaymentPage(Double.parseDouble(amount_ed.getText().toString()));
            }
        });
    }

    //InFlate Payment Page ...
    private void openPaymentPage(Double cart_total_cost) {
        Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "tremno@gmail.com"); //this a demo account for testing the sdk
        in.putExtra(PaymentParams.SECRET_KEY,"211145sds3aqmkol21314");//Add your Secret Key Here
        in.putExtra(PaymentParams.LANGUAGE, preferenceManager.getValue(SharedPreferenceManager.LANGUAGE , ""));
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Payment");
        in.putExtra(PaymentParams.AMOUNT, cart_total_cost);

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, FindServiceActivity.userModel.getPhone());
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, FindServiceActivity.userModel.getEmail());
        in.putExtra(PaymentParams.ORDER_ID, "12345678");
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

        //Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "SAR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "SAR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

        //Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");

        //Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
            }
            if (data.getStringExtra(PaymentParams.RESPONSE_CODE).equals("100")) {
                int current_balance = Integer.parseInt(mahfza_val_txtV.getText().toString()) + Integer.parseInt(data.getStringExtra(PaymentParams.AMOUNT));
                mahfza_val_txtV.setText(String.valueOf(current_balance));
                Toast.makeText(this, getString(R.string.payment_successfully), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @OnClick(R.id.paymentMahfza_back_txtV_id)
    void goBack(){
        finish();
    }
}
