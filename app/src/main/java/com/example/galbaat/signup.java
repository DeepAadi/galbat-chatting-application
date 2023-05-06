package com.example.galbaat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.galbaat.databinding.ActivitySignupBinding;
import com.example.galbaat.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    // FirebaseAuth(object) is used to perform sign in like activities
    ActivitySignupBinding binding;
    // by using this binding variable we can access all the all field in xml like edittext, editview etc
    // by this we also no need to use (find view by id concept)
    FirebaseDatabase database; // variable for database
    ProgressDialog progressDialog;  // know we create a object for progress dialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySignupBinding.inflate(getLayoutInflater());
        //activity signup binding--> refers to the process of automatically
        // generating code that binds UI elements (such as buttons, text views, etc.)
        // in a layout file to corresponding variables in a Kotlin or Java class.
        //inflate is to convert the xml file into view object to view them on screen
        setContentView(binding.getRoot());
        // setContentView() method is used to set the user interface (UI) layout for an activity.
        mAuth = FirebaseAuth.getInstance(); // initialize firebase auth
        database = FirebaseDatabase.getInstance();// know we initiate the database
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(signup.this);
        progressDialog.setTitle("Creating Account");  // this is for set title in progress bar
        progressDialog.setMessage("We are creating your account. ");  // message that appear inside the progress bar

        // know we create button click event for signup
        binding.button.setOnClickListener(new View.OnClickListener() {
            //  setOnClickListener() method is used to set an event listener
            //  for a UI element such as a button, text view, or image view.
            @Override
            public void onClick(View v) {
            //  code to executed when button is clicked
                if(!binding.username.getText().toString().isEmpty() &&!binding.email.getText().toString().isEmpty()&&!binding.password.getText().toString().isEmpty()){
                    // this if statement is to check that username is not empty..and also for email,password,
                    progressDialog.show(); // means first this appear then account is created
                    mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss(); // to disappear the progress bar after completer of creating account
                                    if(task.isSuccessful()){
                                        Users user = new Users(binding.username.getText().toString(),binding.email.getText().toString(),binding.password.getText().toString());
                                        // firstly we have to fetch uiID from authentication to show in realtime database in firebase
                                        String id = task .getResult().getUser().getUid();
                                        // now create table
                                        database.getReference().child("Users").child(id).setValue(user);
                                        Toast.makeText(signup.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(signup.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    // this contain toast message if user name is empty
                    Toast.makeText(signup.this, " Please enter Credentials", Toast.LENGTH_SHORT).show();
                    // Toast is used to print a message on particular event for short period of time

                }
            }
        });
        // when user click on Already have an acoount
        binding.textviewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this,signIn.class);
                startActivity(intent);
            }
        });
    }
}