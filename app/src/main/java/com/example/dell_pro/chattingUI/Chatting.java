package com.example.dell_pro.chattingUI;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dell_pro.chatFragments.ChattingAdapter;
import com.example.dell_pro.chatModel.Chat;
import com.example.dell_pro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Chatting extends AppCompatActivity {

    private EditText edit_msg;
    private ImageButton button_send;
    private FirebaseUser user;
    private DatabaseReference msg_ref;

    private RecyclerView view;
    private List<Chat> list;
    private LinearLayoutManager lin_lay;
    private ChattingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        final String id = getIntent().getStringExtra("id");
        String doc_name = getIntent().getStringExtra("name");

        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(doc_name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        msg_ref = FirebaseDatabase.getInstance().getReference().child("Messages");

        edit_msg = findViewById(R.id.edit_message);
        button_send = findViewById(R.id.btn_send);

        view = findViewById(R.id.chat_recycler_view);

        list = new ArrayList<>();
        adapter = new ChattingAdapter(list);
        lin_lay = new LinearLayoutManager(this);
        view.setHasFixedSize(true);
        view.setLayoutManager(lin_lay);
        view.setAdapter(adapter);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(user.getUid(), id);
            }
        });

        loadMessage(user.getUid(), id);

    }

    private void loadMessage(final String self_id, final String receiver_id) {
        msg_ref.child(self_id).child(receiver_id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Chat c = dataSnapshot.getValue(Chat.class);
                        Log.d("Data", c.getMessage());

                        list.add(c);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage(final String self_id, final String receiver_id) {

        final String message = edit_msg.getText().toString();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Messages");
        final DatabaseReference chatlist_self = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(self_id).child(receiver_id);
        final DatabaseReference chatlist_other = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(receiver_id).child(self_id);

        if (!TextUtils.isEmpty(message)) {

            final HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", self_id);
            hashMap.put("receiver", receiver_id);
            hashMap.put("message", message);
            hashMap.put("type", "text");

            ref.child(self_id).child(receiver_id)
                    .push()
                    .setValue(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            ref.child(receiver_id).child(self_id)
                                    .push()
                                    .setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Chatting.this, "Sent", Toast.LENGTH_SHORT).show();

                                            chatlist_self.child("id").setValue(receiver_id);
                                            chatlist_other.child("id").setValue(self_id);
                                        }
                                    });
                        }
                    });

        }
    }
}
