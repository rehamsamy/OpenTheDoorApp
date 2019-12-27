package com.m.openthedoorapp.Adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.interfaces.ProviderRecyclerItemClick;
import com.m.openthedoorapp.model.ServiceProviderModel;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ServiceProviderAdapter extends RecyclerView.Adapter<ServiceProviderAdapter.Holder> {

    private Context context;
    private ProviderRecyclerItemClick itemClickListner;
    private List<ServiceProviderModel.ProviderItem> list;
    public static int row_index = -1;

    public ServiceProviderAdapter(Context context, List<ServiceProviderModel.ProviderItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemClickListner(ProviderRecyclerItemClick itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_provider_list_item, viewGroup, false);
        return new Holder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.userName_txtV.setText(list.get(i).getName());
        holder.price_val_txtV.setText(String.valueOf(list.get(i).getService_price()));
        holder.arriveTime_txtV.setText(list.get(i).getDistance());
//        double i2=i/60000;
        holder.arriveTime_txtV.setText(new DecimalFormat("##.##").format(Double.parseDouble(list.get(i).getDistance())));
        holder.provider_ratingBar.setRating(list.get(i).getRat_count());
        if (row_index == i) {
            holder.constraintLayout.setBackgroundResource(R.drawable.selected_proider_boerder);
            holder.userName_txtV.setTextColor(Color.parseColor("#baa805"));
        } else {
            holder.constraintLayout.setBackgroundResource(R.drawable.border);
            holder.userName_txtV.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.provider_userName_txtV_id)
        TextView userName_txtV;
        @BindView(R.id.provider_ratingBar_id)
        RatingBar provider_ratingBar;
        @BindView(R.id.price_val_txtV)
        TextView price_val_txtV;
        @BindView(R.id.provider_makeCall_icon)
        ImageView provider_makeCall;
        @BindView(R.id.provider_arriveTime_val_txtV)
        TextView arriveTime_txtV;
        @BindView(R.id.provider_item_layout_id)
        ConstraintLayout constraintLayout;

        public Holder(@NonNull View itemView, final ProviderRecyclerItemClick listner) {
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

            provider_makeCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listner.makeCallClick(position);
                        }
                    }
                }
            });
        }
    }
}
