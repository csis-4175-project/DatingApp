package com.example.termproject_datingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.termproject_datingapp.UserModel.ChatRoomModel;
import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.adapter.RecentChatRecyclerAdapter;
import com.example.termproject_datingapp.adapter.SearchUserRecyclerAdapter;
import com.example.termproject_datingapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecentChatRecyclerAdapter recyclerAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_chat, container, false);
       recyclerView = view.findViewById(R.id.recycler_view);
       setUpRecyclerView();

       return view;
    }

    private void setUpRecyclerView(){

        Query query = FirebaseUtil.allChatRoomCollectionReference()
                .whereArrayContains("userId", FirebaseUtil.currentUSerID())
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query, ChatRoomModel.class).build();

        recyclerAdapter = new RecentChatRecyclerAdapter(options, getContext() );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recyclerAdapter!= null){
            recyclerAdapter.startListening();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(recyclerAdapter!=null){
            recyclerAdapter.stopListening();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(recyclerAdapter!= null){
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}