package com.example.dell_pro.chatFragments;

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
import com.example.dell_pro.R;
import com.example.dell_pro.chatModel.Doctors;
import com.example.dell_pro.chattingUI.Chatting;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ChatsAdapter extends FirebaseRecyclerAdapter<Doctors, ChatsAdapter.ChatsViewHolder> {

    private Context context;
    private DatabaseReference mDoctorsDatabase;

    public ChatsAdapter(@NonNull FirebaseRecyclerOptions<Doctors> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatsAdapter.ChatsViewHolder holder, int position, @NonNull final Doctors model) {

        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor").child(model.getId());

        final String[] data = {"Display Name", "None", "None"};

        mDoctorsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data[0] = Objects.requireNonNull(dataSnapshot.child("docname").getValue()).toString();
                data[1] = Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString();
                data[2] = Objects.requireNonNull(dataSnapshot.child("imgurl").getValue()).toString();

                holder.doc_name.setText(data[0]);

                holder.status.setText(data[1]);

                if (data[2].equals("None")) {
                    holder.doc_image.setImageResource(R.drawable.blankprofile_round);
                } else {
                    Glide.with(context).load(data[2]).into(holder.doc_image);
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
                .inflate(R.layout.doctor, parent, false);

        return new ChatsAdapter.ChatsViewHolder(view);
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {

        TextView doc_name, status;
        ImageView doc_image;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            doc_name = itemView.findViewById(R.id.doc_name);
            status = itemView.findViewById(R.id.doc_status);
            doc_image = itemView.findViewById(R.id.doctor);
        }
    }

}
