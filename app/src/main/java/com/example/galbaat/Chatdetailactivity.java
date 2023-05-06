package com.example.galbaat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.galbaat.adpters.chatadapter;
import com.example.galbaat.databinding.ActivityChatdetailactivityBinding;
import com.example.galbaat.models.messagemodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class Chatdetailactivity extends AppCompatActivity {
FirebaseDatabase database;
FirebaseAuth auth;


    ActivityChatdetailactivityBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityChatdetailactivityBinding.inflate(getLayoutInflater());
       // In Android Studio, "inflate" refers to the process of taking an XML layout
       // file and converting it into a View object that can be displayed in the user interface
        //of an Android app.
       setContentView(binding.getRoot());

       getSupportActionBar().hide();

       // initialize above declared variables
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // know we have to fetch the user name,id  from intent
         final String sender_ID  =  auth.getUid();
         String receiveId=  getIntent().getStringExtra("userId");
        String  username=  getIntent().getStringExtra("username");
        String profile_pic=  getIntent().getStringExtra("profilepic");

        binding.username.setText(username);
        Picasso.get().load(profile_pic).placeholder(R.drawable.avatar).into(binding.profilePic);

           // this code is for what happen if user click on back arrow
         binding.backarrow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent  = new Intent(Chatdetailactivity.this,MainActivity.class);
                 startActivity(intent);
             }
         });
         final ArrayList<messagemodel> messagemodels = new ArrayList<>();
         final chatadapter chatadapter = new chatadapter(messagemodels,this,receiveId);

         binding.chatrecyclerview.setAdapter(chatadapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // this is context
        binding.chatrecyclerview.setLayoutManager(layoutManager);
        // know create 2 variables one for sender room and one for reciever room
        final String sender_room = sender_ID+receiveId;
        final String reciever_room = receiveId+sender_ID;

        // know we have message in firebase and know our work is to show on view
        database.getReference().child("chats")
                .child(sender_room)
                //In Android Studio, ValueEventListener is used to listen for changes
                // in the data in a Firebase Realtime Database.
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                              messagemodels.clear();
                              for(DataSnapshot snapshot1:snapshot.getChildren()){
                                  messagemodel models = snapshot1.getValue(messagemodel.class);
                                  models.setMessageID(snapshot1.getKey());
                                  messagemodels.add(models);
                              }
                              // know we have to tell our chat adapter that our data is changed
                                chatadapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        // know we code for what happen on click on sending button
           binding.send.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   // firstly we have to extract the message for text view so we need a
                  // variable to store that extracted message

                   String message = binding.enterMessage.getText().toString();
                   final messagemodel messagemodel = new messagemodel(sender_ID,message);
                   // down code is written for the purpose of to see the time and date when the message is send
                   messagemodel.setTimestamp(new Date().getTime());
                   // down code is for clearning the textfield after sending text
                   binding.enterMessage.setText(" ");

                   // after this we have to store the texts in database what ever the sender is send in seprate node
                   database.getReference().child("chats")
                           .child(sender_room)
                           .push()
                           .setValue(messagemodel).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override

                               public void onSuccess(Void unused) {
                                   // that same message is stored in reciever node
                                   database.getReference().child("chats")
                                           .child(reciever_room)
                                           .push()
                                           .setValue(messagemodel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {

                                               }
                                           });
                               }
                           });
                           // we are already create a user block in fire base know we create
                           // a new block for chats the above code is for that purpose
               }
           });

    }
}