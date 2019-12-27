package com.m.openthedoorapp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.activities.ConfirmationActivity;
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.fragmentTabs.CompletedFragmentTab;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.ConfirmationResponseModel;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.networking.ApiClient;
import com.m.openthedoorapp.networking.ApiServiceInterface;
import com.m.openthedoorapp.utils.ActionsWithServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedRecyclerAdapter extends RecyclerView.Adapter<CompletedRecyclerAdapter.Holder> {

    Context context;
    private List<ServiceItemData> list;
    private RecyclerOnItemClickListner itemClickListner;
    private String sending;

    public CompletedRecyclerAdapter(Context context, List<ServiceItemData> list, String sending) {
        this.context = context;
        this.list = list;
        this.sending = sending;
    }

    public void setItemClickListner(RecyclerOnItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_item_list, viewGroup, false);
        return new Holder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.userName_txtV.setText(list.get(position).getProvider_name());
        holder.serviceType_txtV.setText(list.get(position).getService_name_ar());
        holder.hourPrice_txtV.setText(String.valueOf(list.get(position).getProvider_price_per_hour()));
        holder.timeToArrive_txtV.setText(String.valueOf(list.get(position).getProvider_minutes_to_arrive()));
        holder.ratingBar.setRating(list.get(position).getRat_count());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.completedService_user_imageV_id)
        ImageView user_imageV;
        @BindView(R.id.completed_userName_txtV_id)
        TextView userName_txtV;
        @BindView(R.id.completedItem_ratingBar_id)
        RatingBar ratingBar;
        @BindView(R.id.completed_serviceType_txtV_id)
        TextView serviceType_txtV;
        @BindView(R.id.completedService_hourPrice_val_txtV)
        TextView hourPrice_txtV;
        @BindView(R.id.completed_timeToArrive_val_txtV)
        TextView timeToArrive_txtV;
        @BindView(R.id.completed_cancelService_btn_id)
        Button reOrderService_btn;
        @BindView(R.id.completed_makeCall_imageV_id)
        ImageView makeCall_imageV;

        public Holder(@NonNull View itemView, final RecyclerOnItemClickListner listner) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.OnItemClick(position);
                        }
                    }
                }
            });

            makeCall_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhoneNumber(list.get(getAdapterPosition()).getProvider_phone());
                }
            });

            reOrderService_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionsWithServices.confirmServicee((Activity) context, list.get(getAdapterPosition()).getProvider_minutes_to_arrive(), list.get(getAdapterPosition()).getProvider_hour_to_finish(), list.get(getAdapterPosition()).getService_id(),
                            list.get(getAdapterPosition()).getProvider_id(), FindServiceActivity.userModel.getId(), "notes", FindServiceActivity.userModel.getToken(), sending);
                }
            });
        }
    }

    public void callPhoneNumber(String phoneNum) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNum));
                context.startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNum));
                context.startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
