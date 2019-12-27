package com.m.openthedoorapp.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.m.openthedoorapp.R;
import com.m.openthedoorapp.interfaces.RecyclerOnItemClickListner;
import com.m.openthedoorapp.model.ReviewsResponseModel;
import com.m.openthedoorapp.utils.Urls;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Holder> {

    private Context context;
    private List<ReviewsResponseModel.ReviewData> list;
    private RecyclerOnItemClickListner itemClickListner;

    public ReviewAdapter(Context context, List<ReviewsResponseModel.ReviewData> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemClickListner(RecyclerOnItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, viewGroup, false);
        return new Holder(view, itemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.userName_txtV.setText(list.get(i).getUser_name());
        holder.review_txtV.setText(list.get(i).getNotes());
        holder.ratingBar.setRating(list.get(i).getRate());
        Glide.with(context)
                .load(Urls.BASE_URL + list.get(i).getUser_image())
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
////                        viewHolder.progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        // image ready, hide progress now
////                        viewHolder.progressBar.setVisibility(View.GONE);
//                        return false;   // return false if you want Glide to handle everything else.
//                    }
//                })
//                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_userImage)
        ImageView imageView;
        @BindView(R.id.review_userName)
        TextView userName_txtV;
        @BindView(R.id.review_content_txtV)
        TextView review_txtV;
        @BindView(R.id.reviewItem_ratingBar_id)
        RatingBar ratingBar;

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
        }
    }
}
