package com.example.galbaat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.galbaat.adpters.chatadapter;
import com.example.galbaat.databinding.ActivityGroupchatBinding;
import com.example.galbaat.models.messagemodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class groupchatActivity extends AppCompatActivity {
ActivityGroupchatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityGroupchatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // firstly we have to implement the back button in our group chat activity
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(groupchatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<messagemodel> messagemodels = new ArrayList<>();
        // creating variable for getting sender id
        final String senderId = FirebaseAuth.getInstance().getUid();
        // for setting the name of group
        binding.username.setText("group chat");
        final chatadapter adapter = new chatadapter(messagemodels,this);
        binding.chatrecyclerview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatrecyclerview.setLayoutManager(layoutManager);
        // know to show that messages in group chat activity
        database.getReference().child("group chat")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagemodels.clear();
                        // we use advance for loop for fetch and show the data
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            messagemodel messagemodel = dataSnapshot.getValue(messagemodel.class);
                            messagemodels.add(messagemodel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // know the step is when ever the user click send button the message is send and store that message in firebase
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.enterMessage.getText().toString();
                final messagemodel messagemodel = new messagemodel(senderId,message);
                messagemodel.setTimestamp(new Date().getTime());
                // to cleare the text field
                binding.enterMessage.setText("");
                database.getReference().child("group chat")
                        .push()
                        .setValue(messagemodel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(groupchatActivity.this, "Message send", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


    }
}