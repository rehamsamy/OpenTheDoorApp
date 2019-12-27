package com.m.openthedoorapp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import com.m.openthedoorapp.activities.CancelService;
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.utils.ActionsWithServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduledTabAdapter extends RecyclerView.Adapter<ScheduledTabAdapter.Holder> {

    Context context;
    private List<ServiceItemData> list;
    private RecyclerOnItemClickListner itemClickListner;
    private String ending;

    public ScheduledTabAdapter(Context context, List<ServiceItemData> list, String ending) {
        this.context = context;
        this.list = list;
        this.ending = ending;
    }

    public void setItemClickListner(RecyclerOnItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.scheduled_item_list, viewGroup, false);
        return new Holder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.providerName_txtV.setText(list.get(position).getProvider_name());
        holder.serviceType_txtV.setText(list.get(position).getService_name_ar());
        holder.hourPrice_val_txtV.setText(String.valueOf(list.get(position).getProvider_price_per_hour()));
        holder.timeToArrive_txtV.setText(String.valueOf(list.get(position).getProvider_minutes_to_arrive()));
        holder.ratingBar.setRating(list.get(position).getRat_count());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.scheduled_user_imageV_id)
        ImageView provider_imageV;
        @BindView(R.id.scheduled_userName_txtV)
        TextView providerName_txtV;
        @BindView(R.id.scheduledItem_ratingBar_id)
        RatingBar ratingBar;
        @BindView(R.id.scheduled_serviceType_txtV_id)
        TextView serviceType_txtV;
        @BindView(R.id.scheduled_hourPrice_val_txtV)
        TextView hourPrice_val_txtV;
        @BindView(R.id.scheduled_timeToArrive_val_txtV)
        TextView timeToArrive_txtV;
        @BindView(R.id.scheduled_endService_btn_id)
        Button endService_btn;
        @BindView(R.id.scheduled_cancelService_imageV_id)
        ImageView cancelService_imageV;
        @BindView(R.id.scheduled_makeCall_imageV)
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

            cancelService_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CancelService.class);
                    intent.putExtra("itemData", list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            endService_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionsWithServices.confirmServicee((Activity) context, list.get(getAdapterPosition()).getProvider_minutes_to_arrive(), list.get(getAdapterPosition()).getProvider_hour_to_finish(), list.get(getAdapterPosition()).getService_id(), list.get(getAdapterPosition()).getProvider_id(), FindServiceActivity.userModel.getId(), "notes", FindServiceActivity.userModel.getToken(), ending);
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