package com.example.dell_pro.chatFragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dell_pro.R;
import com.example.dell_pro.chatModel.Chat;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChattingViewHolder> {

    private List<Chat> chat;
    private Context context;

    public ChattingAdapter(List<Chat> c,Context context) {
        this.chat = c;
        this.context = context;
    }


    @NonNull
    @Override
    public ChattingAdapter.ChattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = null;

        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        } else if (viewType == 2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_image_self, parent, false);
        } else if (viewType == 3) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_image_other, parent, false);
        }

        return new ChattingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingAdapter.ChattingViewHolder holder, int position) {

        Chat c = chat.get(position);
        
        if(c.getType().equals("text")){
            holder.view.setText(c.getMessage());
        }else if(c.getType().equals("image")){
            Glide.with(context).load(c.getMessage()).into(holder.imageView);
        }
        
        //Log.d("Adapter", c.getMessage());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public int getItemViewType(int position) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String self_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Chat c = chat.get(position);

        if (c.getType().equals("text")) {
            if (c.getSender().equals(self_id)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (c.getSender().equals(self_id)) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    public static class ChattingViewHolder extends RecyclerView.ViewHolder {

        TextView view;
        ImageView imageView;

        public ChattingViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.message);
            imageView = itemView.findViewById(R.id.messageImage);
        }
    }
}
