package com.m.openthedoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.Adapter.NotificationsAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.interfaces.RecyclerTouchHelperListner;
import com.m.openthedoorapp.model.DeleteNotificatiomModel;
import com.m.openthedoorapp.model.NotficationItem;
import com.m.openthedoorapp.model.UserNotificationResponse;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.DialogUtil;
import com.m.openthedoorapp.utils.EndlessRecyclerViewScrollListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class NotificationsActivity extends AppCompatActivity implements RecyclerTouchHelperListner {

    @BindView(R.id.notifications_recyclerV_id)
    RecyclerView recyclerView;
    @BindView(R.id.notifications_no_data_txtV_id)
    TextView no_available_data_txtV;
    @BindView(R.id.notifications_progressId)
    ProgressBar progressBar;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private int currentPage = 1;
    private List<NotficationItem> notficationItemList;
    private NotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        dialogUtil = new DialogUtil();
        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable())
            getMyNotifications(FindServiceActivity.userModel.getId(), currentPage);
        else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getMyNotifications(int user_id, int currentPage) {
        // Show Progress Dialog
        Map<String, Object> map = new HashMap<>();
        String token = FindServiceActivity.userModel.getToken();
        map.put("user_id", user_id);
        map.put("api_token", token);
        map.put("page", currentPage);
        map.put("limit", 20);
//        final ProgressDialog dialog = dialogUtil.showProgressDialog(NotificationsActivity.this, getString(R.string.loading), false);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UserNotificationResponse> call = serviceInterface.getUserNotification(map);
        call.enqueue(new Callback<UserNotificationResponse>() {
            @Override
            public void onResponse(Call<UserNotificationResponse> call, Response<UserNotificationResponse> response) {
                if (response.body().isStatus()) {
                    notficationItemList = response.body().getNotfication();
                    recyclerView.setVisibility(View.VISIBLE);
                    no_available_data_txtV.setVisibility(View.GONE);
                    buildRecyclerV(notficationItemList);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    no_available_data_txtV.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserNotificationResponse> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                no_available_data_txtV.setVisibility(View.VISIBLE);
            }
        });
    }

    private void buildRecyclerV(List<NotficationItem> notficationItemList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NotificationsAdapter(NotificationsActivity.this, notficationItemList);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage++;
                //getNotification(userPage);
                getMyNotifications(FindServiceActivity.userModel.getId(), currentPage);
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationsAdapter.Holder) {
            // get the removed item name to display it in snack bar
            // backup of removed item for undo purpose
            final NotficationItem deletedItem = notficationItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            deleteNotification(deletedItem.getId(), FindServiceActivity.userModel.getToken());

            // showing snack bar with Undo option
            Toast.makeText(this, getString(R.string.item_removed), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteNotification(int notifiable_id, String token) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DeleteNotificatiomModel> call = serviceInterface.deleteNotification(notifiable_id, token);
        call.enqueue(new Callback<DeleteNotificatiomModel>() {
            @Override
            public void onResponse(Call<DeleteNotificatiomModel> call, Response<DeleteNotificatiomModel> response) {

                if (response.body().getStatus()) {
                    Toast.makeText(NotificationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteNotificatiomModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.notifications_back_txtV_id)
    void goback() {
        finish();
    }
}
