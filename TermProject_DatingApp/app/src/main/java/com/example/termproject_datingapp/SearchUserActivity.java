package com.example.termproject_datingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.adapter.SearchUserRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.auth.User;

public class SearchUserActivity extends AppCompatActivity {
    EditText searchUser;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchUser = findViewById(R.id.search_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.search_user_recyler_view);

        searchUser.requestFocus();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchItem = searchUser.getText().toString();
                if(searchItem.isEmpty() || searchItem.length() < 3){
                    searchUser.setError("Invalid Input!");
                    return;
                }

                setupSearchRecylerView(searchItem);
            }
        });

    }

    private void setupSearchRecylerView(String item ){
        //this code right now dosn't do much
        //needs firebase data to create query to get user information
        //The Query
        /*
            Query query = FirebaseUtil.AllUserCollectionReference()
                .whereGreaterThanEqualTO("username", searchTerm);
        */
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().build();
        //this code chains the above code, due to no firebase information i wont be using it.
        //.setQuery(query, UserModel.class.build();

        recyclerAdapter = new SearchUserRecyclerAdapter(options, getApplicationContext() );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(recyclerAdapter!= null){
            recyclerAdapter.startListening();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerAdapter!= null){
            recyclerAdapter.startListening();
        }
    }
}