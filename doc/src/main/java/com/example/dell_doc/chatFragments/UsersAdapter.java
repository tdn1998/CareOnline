package com.example.dell_doc.chatFragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dell_doc.chatModel.Users;
import com.example.dell_doc.Chatting;
import com.example.dell_doc.R;
import com.example.dell_doc.UserProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UsersAdapter extends FirebaseRecyclerAdapter<Users, UsersAdapter.UsersViewHolder> {

    private Context context;

    public UsersAdapter(@NonNull FirebaseRecyclerOptions<Users> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position, @NonNull final Users model) {

        if(model.getUsername()==null){
            holder.user_name.setText("Display Name");
        } else{
            holder.user_name.setText(model.getUsername());
        }

        if(model.getPhno()==null){
            holder.user_phone.setText("No Phone Number");
        }else{
            holder.user_phone.setText(model.getPhno());
        }

        if (model.getImgurl()==null) {
            holder.user_image.setImageResource(R.drawable.blankprofile_round);
        } else {
            Glide.with(context).load(model.getImgurl()).into(holder.user_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Use","onclicked");

                CharSequence[] options = new CharSequence[]{"Open Profile", "Send Message"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Select Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(context, UserProfile.class);
                            intent.putExtra("id", model.getId());
                            intent.putExtra("name",model.getUsername());
                            context.startActivity(intent);
                        }
                        if (i == 1) {
                            Intent intent = new Intent(context, Chatting.class);
                            intent.putExtra("id", model.getId());
                            intent.putExtra("name",model.getUsername());
                            context.startActivity(intent);
                        }
                    }
                });
                builder.show();

            }
        });

    }

    @NonNull
    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users, parent, false);

        return new UsersViewHolder(view);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, user_phone;
        ImageView user_image;


        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_phone = itemView.findViewById(R.id.user_phone);
            user_image = itemView.findViewById(R.id.user);
        }
    }
}