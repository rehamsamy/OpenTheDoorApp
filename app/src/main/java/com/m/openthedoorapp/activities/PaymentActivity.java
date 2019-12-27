package com.m.openthedoorapp.activities;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m.openthedoorapp.Adapter.PaymentMethodAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.networking.NetworkAvailable;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends AppCompatActivity {


    @BindView(R.id.done_btn_id)
    Button done_btn;

    private final String TAG = this.getClass().getSimpleName();
    private int paymentMethod_state = 1;
    private NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
    }

    public void onRadioPaymentClicked(View view) {
        done_btn.setVisibility(View.VISIBLE);
        int id = view.getId();
        switch (id) {
            case R.id.wallet_radio_btn:
                paymentMethod_state = 1;
                break;
            case R.id.cash_on_delivery_radio_btn:
                paymentMethod_state = 2;
                break;
            case R.id.online_credit_radio_btn:
                paymentMethod_state = 3;
                break;
        }
    }

    @OnClick(R.id.done_btn_id)
    public void setDone_btn() {
        // Put the String to pass back into an Intent and close this activity
        Intent intent = new Intent();
        intent.putExtra("payment_state", String.valueOf(paymentMethod_state));
        setResult(RESULT_OK, intent);
        finish();
    }
}
