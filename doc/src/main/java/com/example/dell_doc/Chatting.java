package com.example.dell_doc;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dell_doc.chatFragments.ChattingAdapter;
import com.example.dell_doc.chatModel.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Chatting extends AppCompatActivity {

    private EditText edit_msg;
    private ImageButton button_send, button_image;
    private FirebaseUser user;
    private DatabaseReference msg_ref;
    private String id;

    private RecyclerView view;
    private List<Chat> list;
    private LinearLayoutManager lin_lay;
    private ChattingAdapter adapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference dataImage;
    private StorageTask task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        id = getIntent().getStringExtra("id");
        String doc_name = getIntent().getStringExtra("name");

        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(doc_name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        msg_ref = FirebaseDatabase.getInstance().getReference().child("Messages");
        dataImage = FirebaseStorage.getInstance().getReference("imagemessage/").child("users/").child(user.getUid());

        edit_msg = findViewById(R.id.edit_message);
        button_send = findViewById(R.id.btn_send);
        button_image = (ImageButton) findViewById(R.id.btn_image);

        view = findViewById(R.id.chat_recycler_view);

        list = new ArrayList<>();
        adapter = new ChattingAdapter(list,this);
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

        button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        loadMessage(user.getUid(), id);

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent
                .createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Toast.makeText(this, "Sending Image...", Toast.LENGTH_SHORT).show();
            sendImage(user.getUid(), id);
        }
    }

    private void sendImage(final String self_id, final String receiver_id) {

        final StorageReference messageData = dataImage.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        task = messageData.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(NewProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        getUrl(messageData, self_id, receiver_id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
    }

    private void getUrl(StorageReference ref, final String self_id, final String receiver_id) {
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (user != null && mImageUri != null) {

                    String message = uri.toString();

                    final DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("Messages");
                    final DatabaseReference chatlist_self = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(self_id).child(receiver_id);
                    final DatabaseReference chatlist_other = FirebaseDatabase.getInstance().getReference().child("Chatlist").child(receiver_id).child(self_id);

                    final HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", self_id);
                    hashMap.put("receiver", receiver_id);
                    hashMap.put("message", message);
                    hashMap.put("type", "image");

                    refer.child(self_id).child(receiver_id)
                            .push()
                            .setValue(hashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    refer.child(receiver_id).child(self_id)
                                            .push()
                                            .setValue(hashMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(Chatting.this, "Sent", Toast.LENGTH_SHORT).show();
                                                    edit_msg.setText("");

                                                    chatlist_self.child("id").setValue(receiver_id);
                                                    chatlist_other.child("id").setValue(self_id);
                                                }
                                            });
                                }
                            });

                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
                                            edit_msg.setText("");

                                            chatlist_self.child("id").setValue(receiver_id);
                                            chatlist_other.child("id").setValue(self_id);
                                        }
                                    });
                        }
                    });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.report:
                Intent intent = new Intent(Chatting.this, ReportActivity.class);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
