package com.example.dell_doc.chatFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dell_doc.chatModel.Users;
import com.example.dell_doc.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUsersFragment extends Fragment {

    private RecyclerView user_recycler;
    private DatabaseReference mUsersDatabase;

    private UsersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_all_users, container, false);

        user_recycler = inflate.findViewById(R.id.user_recycler_view);
        user_recycler.setHasFixedSize(true);
        user_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseRecyclerOptions<Users> options= new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUsersDatabase,Users.class)
                .build();

        adapter = new UsersAdapter(options,getContext());
        user_recycler.setAdapter(adapter);

        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}