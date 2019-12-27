package com.m.openthedoorapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
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
import com.m.openthedoorapp.activities.FindServiceActivity;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.ServiceItemData;
import com.m.openthedoorapp.utils.ActionsWithServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InProcessTabAdapter extends RecyclerView.Adapter<InProcessTabAdapter.Holder> {

    Context context;
    private List<ServiceItemData> list;
    private RecyclerOnItemClickListner itemClickListner;
    private String sending ;

    public InProcessTabAdapter(Context context, List<ServiceItemData> list, String sending) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.inprocess_item_list, viewGroup, false);
        return new Holder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // SETImage
        holder.userName_txtV.setText(list.get(position).getProvider_name());
        holder.serviceType_txtV.setText(list.get(position).getService_name_ar());
        holder.priceal_txtV.setText(String.valueOf(list.get(position).getProvider_price_per_hour()));
        holder.ratingBar.setRating(list.get(position).getRat_count());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.inProcess_userImageV_id)
        ImageView user_imageV;
        @BindView(R.id.inProcess_userName_txtV_id)
        TextView userName_txtV;
        @BindView(R.id.inprogresItem_ratingBar_id)
        RatingBar ratingBar;
        @BindView(R.id.inProcess_cancelService_btn_id)
        ImageView cancelService_btn;
        @BindView(R.id.inProcess_endService_btn_id)
        Button endService_btn;
        @BindView(R.id.inProcess_price_val_txtV_id)
        TextView priceal_txtV;
        @BindView(R.id.inProcess_serviceType_txtV_id)
        TextView serviceType_txtV;

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

            endService_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionsWithServices.confirmServicee((Activity) context, list.get(getAdapterPosition()).getProvider_minutes_to_arrive(), list.get(getAdapterPosition()).getProvider_hour_to_finish(), list.get(getAdapterPosition()).getService_id(),  list.get(getAdapterPosition()).getProvider_id(), FindServiceActivity.userModel.getId(), "notes", FindServiceActivity.userModel.getToken(), sending);
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
        }
    }
}
