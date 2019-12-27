package com.m.openthedoorapp.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.activities.CancelService;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.ServiceItemData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CanceledAdapter extends RecyclerView.Adapter<CanceledAdapter.Holder> {

    Context context;
    private List<ServiceItemData> list;
    private RecyclerOnItemClickListner itemClickListner;

    public CanceledAdapter(Context context, List<ServiceItemData> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemClickListner(RecyclerOnItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.canceled_item_list, viewGroup, false);
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

        @BindView(R.id.canceledService_user_imageV_id)
        ImageView provider_imageV;
        @BindView(R.id.canceled_userName_txtV_id)
        TextView userName_txtV;
        @BindView(R.id.canceled_serviceType_txtV_id)
        TextView serviceType_txtV;
        @BindView(R.id.canceledService_hourPrice_val_txtV)
        TextView hourPrice_txtV;
        @BindView(R.id.canceled_timeToArrive_val_txtV)
        TextView timeToArrive_txtV;
        @BindView(R.id.canceledItem_ratingBar_id)
        RatingBar ratingBar;
        @BindView(R.id.canceled_makeCall_imageV_id)
        ImageView makeCall_imageV;
        @BindView(R.id.canceled_cancelService_btn_id)
        Button cancelService_btn;

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

            cancelService_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CancelService.class);
                    intent.putExtra("itemData", list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            makeCall_imageV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhoneNumber(list.get(getAdapterPosition()).getProvider_phone());
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
