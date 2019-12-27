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
import com.m.openthedoorapp.Adapter.InProcessTabAdapter;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.activities.CancelService;
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.activities.ReportAproblemActivity;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.InProcessHistoryModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.networking.NetworkAvailable;
import com.m.openthedoorapp.utils.ActionsWithServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InProcessFragmentTab extends Fragment {

    private Dialog myDialog;
    private NetworkAvailable networkAvailable;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noData_txtV;

    // Vars
    private final String TAG = this.getClass().getSimpleName();
    private int current_page = 1;
    private static final int limit = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_inprocess_tab, container, false);

        myDialog = new Dialog(getActivity());
        networkAvailable = new NetworkAvailable(getActivity());
        recyclerView = view.findViewById(R.id.inprocess_recycler);
        progressBar = view.findViewById(R.id.inProcess_progressBar_id);
        noData_txtV = view.findViewById(R.id.inProcess_noData_txtV);

        if (networkAvailable.isNetworkAvailable()) {
            getInProcessData();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getInProcessData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<InProcessHistoryModel> call = serviceInterface.inProcessHistory(current_page, limit, FindServiceActivity.userModel.getId(), FindServiceActivity.userModel.getToken());
        call.enqueue(new Callback<InProcessHistoryModel>() {
            @Override
            public void onResponse(Call<InProcessHistoryModel> call, Response<InProcessHistoryModel> response) {
                Log.i(TAG, "onResponse " + response.body().getStatus());
                if (response.body().getStatus()) {
                    List<ServiceItemData> inProcessList = response.body().getUserserviceinfo().getInprocess();
                    if (inProcessList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        noData_txtV.setVisibility(View.GONE);
                        buildInProgressRecycler(inProcessList);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noData_txtV.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<InProcessHistoryModel> call, Throwable t) {
                Log.i(TAG, "onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildInProgressRecycler(final List<ServiceItemData> inprocessList) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        // Set Adapter...
        InProcessTabAdapter adapter = new InProcessTabAdapter(getActivity(), inprocessList, getString(R.string.sending));
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListner(new RecyclerOnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                show_popup(position, inprocessList.get(position));
            }
        });
    }

    private void show_popup(final int position, final ServiceItemData itemData) {
        ImageView makeCall, cancelService_imageV, userImage_imageV;
        Button endService_btn;
        TextView givebonus, addNote, reportProblem, userName_txtV, serviceType_txtV, finishedTime_txtV, completedServices_txtV;
        RatingBar ratingBar;

        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.inprogress_popup_layout);
        makeCall = myDialog.findViewById(R.id.inProcessDetails_makeCall_icon_id);
        endService_btn = myDialog.findViewById(R.id.inprogressDetailse_endService_btn);
        cancelService_imageV = myDialog.findViewById(R.id.inProgress_cancelService_imageV);
        userName_txtV = myDialog.findViewById(R.id.inProcessDetails_userName_txtV_id);
        userImage_imageV = myDialog.findViewById(R.id.inProcessDetails_userImage_imageV_id);
        serviceType_txtV = myDialog.findViewById(R.id.inProcessDetails_serviceType_txtV_val);
        finishedTime_txtV = myDialog.findViewById(R.id.inProcessDetails_finishedTime_txtV_id);
        completedServices_txtV = myDialog.findViewById(R.id.completedServices_txtV_id);
        ratingBar = myDialog.findViewById(R.id.inProcessDetails_item_rating);
        givebonus = myDialog.findViewById(R.id.inProgress_give_bonus);
        addNote = myDialog.findViewById(R.id.inProgress_add_notes);
        reportProblem = myDialog.findViewById(R.id.inProgress_report_proplem);

        endService_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                ActionsWithServices.confirmServicee(getActivity(), itemData.getProvider_minutes_to_arrive(), itemData.getProvider_hour_to_finish(), itemData.getService_id(), itemData.getProvider_id(), FindServiceActivity.userModel.getId(), "notes", FindServiceActivity.userModel.getToken(), getString(R.string.sending));
            }
        });
        cancelService_imageV.setOnClickListener(new View.OnClickListener() {
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

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Intent intent = new Intent(getActivity(), ReportAproblemActivity.class);
                intent.putExtra("itemData", itemData);
                getActivity().startActivity(intent);
            }
        });

        reportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Intent intent = new Intent(getActivity(), ReportAproblemActivity.class);
                intent.putExtra("itemData", itemData);
                getActivity().startActivity(intent);
            }
        });

        // Set Data to Views...
        Glide.with(getActivity())
                .load(itemData.getProvider_image())
                .centerCrop()
                .placeholder(R.drawable.review_user_pic_withframe)
                .into(userImage_imageV);
        userName_txtV.setText(itemData.getProvider_name());
        serviceType_txtV.setText(itemData.getService_name_ar());
        ratingBar.setRating(itemData.getRat_count());
        finishedTime_txtV.setText(itemData.getProvider_hour_to_finish());
        completedServices_txtV.setText(String.valueOf(itemData.getTotal()));
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
