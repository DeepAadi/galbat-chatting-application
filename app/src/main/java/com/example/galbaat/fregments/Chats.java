package com.example.galbaat.fregments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.galbaat.R;
import com.example.galbaat.adpters.userAdapter;
import com.example.galbaat.databinding.FragmentChatsBinding;
import com.example.galbaat.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Chats extends Fragment {


    public Chats() {  // chats fragment constructor
        // Required empty public constructor

    }

    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database; // for purpose of fetching people from database





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater,container,false);
        // know initialize data base
        database = FirebaseDatabase.getInstance();

        // know we have to create user adapters
         userAdapter adapter = new userAdapter(list,getContext());
         binding.chatrecyclerview.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatrecyclerview.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              // clear the list
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){ // children is ID
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserid(dataSnapshot.getKey());

                    if(!users.getUserid().equals(FirebaseAuth.getInstance().getUid())){
                        // know we have to add user to list
                        // use if so it not added me in chat list
                        list.add(users);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    // first root data in firebase

        return binding.getRoot(); // return view
    }
}