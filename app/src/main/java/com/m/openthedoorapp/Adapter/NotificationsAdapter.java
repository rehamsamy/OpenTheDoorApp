package com.m.openthedoorapp.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m.openthedoorapp.R;
import com.m.openthedoorapp.model.NotficationItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.Holder> {
    Context mContext;
    List<NotficationItem> notficationList;

    public NotificationsAdapter(Context context, List<NotficationItem> mList) {
        mContext = context;
        notficationList = mList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_row_item, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NotficationItem item = notficationList.get(position);
        String time = item.getCreatedAt();
        holder.notify_time_txtV.setText(time);
        holder.notification_content_txtV.setText(item.getUserNotfEn());

    }

    @Override
    public int getItemCount() {
        return notficationList.size();
    }

    public void setTasks(List<NotficationItem> notficationItems) {
        this.notficationList = notficationItems;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        notficationList.remove(position);
        notifyItemRemoved(position);
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.notify_message_info)
        TextView notification_content_txtV;
        @BindView(R.id.notify_time)
        TextView notify_time_txtV;
        @BindView(R.id.view_back_ground)
        public LinearLayout view_back_ground;
        @BindView(R.id.forgroundLayout_item_data)
        public ConstraintLayout view_foreground;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
