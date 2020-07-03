package com.example.dell_doc.chatFragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dell_doc.chatModel.Chat;
import com.example.dell_doc.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChattingViewHolder> {

    private List<Chat> chat;

    public ChattingAdapter(List<Chat> c) {
        this.chat = c;
    }


    @NonNull
    @Override
    public ChattingAdapter.ChattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }

        return new ChattingViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String self_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Chat c = chat.get(position);
        if (c.getSender().equals(self_id)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingAdapter.ChattingViewHolder holder, int position) {
        Chat c = chat.get(position);
        holder.view.setText(c.getMessage());
        Log.d("Adapter", c.getMessage());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public static class ChattingViewHolder extends RecyclerView.ViewHolder {

        TextView view;

        public ChattingViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.message);
        }
    }
}
