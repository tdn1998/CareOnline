package com.example.dell_doc.chatFragments;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dell_doc.Chatting;
import com.example.dell_doc.R;
import com.example.dell_doc.chatModel.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChatsAdapter extends FirebaseRecyclerAdapter<Users, ChatsAdapter.ChatsViewHolder> {

    private Context context;
    private DatabaseReference mDoctorsDatabase;

    public ChatsAdapter(@NonNull FirebaseRecyclerOptions<Users> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatsAdapter.ChatsViewHolder holder, int position, @NonNull final Users model) {

        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getId());

        final String[] data = {"Display Name", "None", "None"};

        mDoctorsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data[0] = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();
                data[1] = Objects.requireNonNull(dataSnapshot.child("phno").getValue()).toString();
                data[2] = Objects.requireNonNull(dataSnapshot.child("imgurl").getValue()).toString();

                holder.user_name.setText(data[0]);

                holder.user_phone.setText(data[1]);

                if (data[2].equals("None")) {
                    holder.user_image.setImageResource(R.drawable.blankprofile_round);
                } else {
                    Glide.with(context).load(data[2]).into(holder.user_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Doc", "onclicked");

                Intent intent = new Intent(context, Chatting.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("name", data[0]);
                context.startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public ChatsAdapter.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users, parent, false);

        return new ChatsAdapter.ChatsViewHolder(view);
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, user_phone;
        ImageView user_image;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_phone = itemView.findViewById(R.id.user_phone);
            user_image = itemView.findViewById(R.id.user);
        }
    }

}
