package com.example.galbaat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.galbaat.databinding.ActivitySettingBinding;
import com.example.galbaat.databinding.ActivitySignInBinding;
import com.example.galbaat.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.ref.Reference;
import java.util.HashMap;

public class Setting extends AppCompatActivity {
    ActivitySettingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // know we have to inititalize the firebaseauth and storage and database
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


       // code for back button
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this,MainActivity.class);
                startActivity(intent);
            }
        });
        // code for save button.....
        binding.savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what is user enter save button without write anything
                if(!binding.savebutton.getText().toString().equals("")&& !binding.txtusername.getText().toString().equals("")){
                    String status = binding.savebutton.getText().toString();
                    String username = binding.txtusername.getText().toString();

                    HashMap<String,Object> obj = new HashMap<>();
                    obj.put("username",username);
                    obj.put("status",status);

                    database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast.makeText(Setting.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Setting.this, "Please enter Username and Status", Toast.LENGTH_SHORT).show();
                }
               

            }
        });

        // code for fetching the image so image is shown on image view

        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                     Users users = snapshot.getValue(Users.class);

                     // for fetching image from firebase database we use picasso


                        if (users != null) {
                            Picasso.get()
                                    .load(users.getProfilePic())
                                    .placeholder(R.drawable.avatar)
                                    .into(binding.profileImage);
                        }

                        binding.savebutton.setText(users.getStatus());
                                  binding.txtusername.setText(users.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        // know we code for + button
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("Image/+");
            startActivityForResult(intent,25);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data.getData()!=null)
        {
           Uri sFile = data.getData();
           binding.profileImage.setImageURI(sFile);

           final StorageReference reference = storage.getReference().child("Profile_Pic")
                   .child(FirebaseAuth.getInstance().getUid());
           reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                   database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                           .child("profile_pic").setValue(uri.toString());
                  }
              });
               }
           });
        }
    }
}