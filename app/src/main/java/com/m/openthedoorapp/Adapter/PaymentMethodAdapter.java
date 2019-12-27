package com.m.openthedoorapp.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.m.openthedoorapp.R;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.Holder> {

    Context context;
    OnItemClick mOnItemClick;

    View view;

    public interface OnItemClick {
        void onClick(int position);
    }

    public PaymentMethodAdapter(Context mcontext) {
        context = mcontext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.payment_list_item, viewGroup, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.edit_payment_button);

        }
    }


}
