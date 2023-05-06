package com.example.galbaat.adpters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galbaat.Chatdetailactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galbaat.R;
import com.example.galbaat.models.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewholder> {

    ArrayList<Users> list;
    Context context;
 // In Java, ArrayList is a class that provides a dynamic array implementation, meaning that the
 // size of the array can be changed during runtime. It is a part of the java.util package and is
 // commonly used in Java programming for storing and manipulating collections of data.
 //An ArrayList can hold objects of any type, including primitives such as integers and doubles.
    public userAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // onCreateViewHolder() is a method that is called by the
        // RecyclerView when a new ViewHolder object needs to be created.
        // This method is typically implemented in the adapter class
        // that is used to populate the RecyclerView.

        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

          Users users = list.get(position);
          Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar).into(holder.image);
          holder.user_name.setText(users.getUsername());

          // This code is write to set the last message
        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(FirebaseAuth.getInstance().getUid() + users.getUserid())
                                .orderByChild("timestamp")
                //The ORDER BY command is used to sort the result set in ascending or descending order.
                        .limitToLast(1) // no. of recordes you want that is only one
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                                holder.Last_message.setText(snapshot1.child("message").getValue().toString());
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



          holder.itemView.setOnClickListener(new View.OnClickListener() {
              //  holder-> usually refers to a ViewHolder object that is used to improve the performance of a RecyclerView.
              @Override
              public void onClick(View v) {
             Intent intent = new Intent(context, Chatdetailactivity.class);
                 // In Android Studio, putExtra() is a method in the Intent class that is used to pass data between activities.
                  // It adds extra data to the intent, which can be retrieved
                  //in the receiving activity using the corresponding getExtra() method.
                  intent.putExtra("userId",users.getUserid());
                  intent.putExtra("profilepic",users.getProfilePic());
                  intent.putExtra("username",users.getUsername());

             context.startActivity(intent);
              }
          });

    }

    @Override
    public int getItemCount() {
        return list.size();  // no of contacts in list
    }

    // know we have to create a model class here
    public class viewholder extends RecyclerView.ViewHolder{
         ImageView image;
         TextView user_name,Last_message;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            //  FindviewbyID-->This method is commonly used to access views such as buttons,
            // text fields, and images, in order to manipulate their
            // properties programmatically.
            image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.name);
            Last_message = itemView.findViewById(R.id.last_message);
        }
    }
}
