package com.m.openthedoorapp.activities;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kcode.bottomlib.BottomDialog;
import com.m.openthedoorapp.Adapter.ReviewAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.AddReviewResponseModel;
import com.m.openthedoorapp.model.ReviewsResponseModel;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.EndlessRecyclerViewScrollListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reviews extends AppCompatActivity {

    @BindView(R.id.review_recycler)
    RecyclerView reviews_recyclerView;
    @BindView(R.id.reviews_noData_txtV_id)
    TextView noData_txtV;
    @BindView(R.id.reviews_number_val_txtV)
    TextView reviewsNum_val_txtV;
    @BindView(R.id.add_review_button_id)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.reviews_progress_id)
    AVLoadingIndicatorView loadingIndicatorView;

    private ReviewAdapter adapter;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private int currentPage = 1;
    private int page_limit = 20;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (networkAvailable.isNetworkAvailable())
            getReviews(currentPage, page_limit, FindServiceActivity.userModel.getToken());
        else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getReviews(int currentPage, int page_limit, String token) {
//        final ProgressDialog dialog = dialogUtil.showProgressDialog(Reviews.this, getString(R.string.loading), false);
        loadingIndicatorView.show();
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ReviewsResponseModel> call = serviceInterface.getAllReviews(currentPage, page_limit, token);
        call.enqueue(new Callback<ReviewsResponseModel>() {
            @Override
            public void onResponse(Call<ReviewsResponseModel> call, Response<ReviewsResponseModel> response) {
                if (response.body().getStatus()) {
                    List<ReviewsResponseModel.ReviewData> reviewsList = response.body().getReviews();
                    reviews_recyclerView.setVisibility(View.VISIBLE);
                    noData_txtV.setVisibility(View.GONE);
                    reviewsNum_val_txtV.setText(reviewsList.size() + " review");

                    reviews_recyclerView.setVisibility(View.VISIBLE);
                    buildReviewsRecycler(reviewsList);
                } else {
                    reviews_recyclerView.setVisibility(View.GONE);
                    noData_txtV.setVisibility(View.VISIBLE);
                }
                loadingIndicatorView.hide();
            }

            @Override
            public void onFailure(Call<ReviewsResponseModel> call, Throwable t) {
                t.printStackTrace();
                loadingIndicatorView.hide();
            }
        });
    }

    private void buildReviewsRecycler(final List<ReviewsResponseModel.ReviewData> reviewsList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reviews_recyclerView.setLayoutManager(layoutManager);
        reviews_recyclerView.setHasFixedSize(true);
        // Set Adapter...
        adapter = new ReviewAdapter(this, reviewsList);
        reviews_recyclerView.setAdapter(adapter);

        reviews_recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        });

        adapter.setItemClickListner(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                if (reviewsList.get(position).getUser_id() == FindServiceActivity.userModel.getId()) {
                    BottomDialog dialog = BottomDialog.newInstance(getResources().getString(R.string.comment), getResources().getString(R.string.cancel), new String[]{getResources().getString(R.string.edit), getResources().getString(R.string.remove)});
                    dialog.show(getSupportFragmentManager(), "dialog");
                    //add item click listener
                    dialog.setListener(new BottomDialog.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void click(int position) {
                            if (position == 0) {
                                Intent intent = new Intent(Reviews.this, AddReview.class);
                                intent.putExtra("review_data", reviewsList.get(position));
                                startActivity(intent);
                            } else if (position == 1) {
                                removeMyComment(reviewsList.get(position).getId(), FindServiceActivity.userModel.getToken());
                            }
                        }
                    });
                }
            }
        });
    }

    private void removeMyComment(int id, String token) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AddReviewResponseModel> call = serviceInterface.deleteReview(id, token);
        call.enqueue(new Callback<AddReviewResponseModel>() {
            @Override
            public void onResponse(Call<AddReviewResponseModel> call, Response<AddReviewResponseModel> response) {
                Log.i(TAG, "onResponse " + response.body());
            }

            @Override
            public void onFailure(Call<AddReviewResponseModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.reviews_back_txtV_id)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.add_review_button_id)
    void addUserReview() {
        Intent intent = new Intent(Reviews.this, AddReview.class);
        startActivity(intent);
    }
}
