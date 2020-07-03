package com.example.dell_pro.chatFragments;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dell_pro.chatModel.Doctors;
import com.example.dell_pro.chattingUI.Chatting;
import com.example.dell_pro.chattingUI.DoctorProfile;
import com.example.dell_pro.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class DoctorsAdapter extends FirebaseRecyclerAdapter<Doctors, DoctorsAdapter.DoctorsViewHolder> {

    private Context context;

    public DoctorsAdapter(@NonNull FirebaseRecyclerOptions<Doctors> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position, @NonNull final Doctors model) {
        holder.doc_name.setText(model.getDocname());
        holder.status.setText(model.getStatus());

        if (model.getImgurl().equals("default")) {
            holder.doc_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(model.getImgurl()).into(holder.doc_image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Doc","onclicked");

                CharSequence[] options = new CharSequence[]{"Open Profile", "Send Message"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Select Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(context, DoctorProfile.class);
                            intent.putExtra("id", model.getId());
                            intent.putExtra("name", model.getDocname());
                            context.startActivity(intent);
                        }
                        if (i == 1) {
                            Intent intent = new Intent(context, Chatting.class);
                            intent.putExtra("id", model.getId());
                            intent.putExtra("name", model.getDocname());
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
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor, parent, false);

        return new DoctorsViewHolder(view);
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder {

        TextView doc_name, status;
        ImageView doc_image;

        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);

            doc_name = itemView.findViewById(R.id.doc_name);
            status = itemView.findViewById(R.id.doc_status);
            doc_image = itemView.findViewById(R.id.doctor);
        }
    }

}
