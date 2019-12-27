package com.m.openthedoorapp.activities;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.AddReviewResponseModel;
import com.m.openthedoorapp.model.ReviewsResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReview extends AppCompatActivity {

    @BindView(R.id.addReview_back_txtV_id)
    ImageView back;
    @BindView(R.id.user_image)
    ImageView user_imageV;
    @BindView(R.id.user_name)
    TextView userName_txtV;
    @BindView(R.id.addReview_ratingBar_id)
    RatingBar ratingBar;
    @BindView(R.id.review_ed)
    EditText review_ed;
    @BindView(R.id.addReview_button)
    Button add_review_btn;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    ReviewsResponseModel.ReviewData reviewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("review_data")) {
            reviewData = getIntent().getParcelableExtra("review_data");
            Log.i("userNAME: ", reviewData.getUser_name());
            Log.i("notee: ", reviewData.getNotes());
            Log.i("datee: ", reviewData.getCreated_at());
        }
    }

    @OnClick(R.id.addReview_back_txtV_id)
    public void setBack() {
        finish();
    }

    @OnClick(R.id.addReview_button)
    public void addReview() {
        final ProgressDialog dialog = dialogUtil.showProgressDialog(AddReview.this, getString(R.string.loading), false);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddReviewResponseModel> call = serviceInterface.addReview(FindServiceActivity.userModel.getId(), 1, review_ed.getText().toString(), 4, FindServiceActivity.userModel.getToken());
        call.enqueue(new Callback<AddReviewResponseModel>() {
            @Override
            public void onResponse(Call<AddReviewResponseModel> call, Response<AddReviewResponseModel> response) {
                if (response.body().getStatus()) {
                    Toast.makeText(AddReview.this, response.body().getSuccessMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddReview.this, response.body().getSuccessMsg(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddReviewResponseModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }
}