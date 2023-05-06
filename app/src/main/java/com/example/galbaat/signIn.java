package com.example.galbaat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.galbaat.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signIn extends AppCompatActivity {
// firstly creating some variables

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    // know for progress dialog box we create a variable
    // which shows that process is going on when ever user click on signing button
    FirebaseAuth auth;
    // know create variable for firebase authentication
    FirebaseDatabase firebaseDatabase;
    // creating database object for later use


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivitySignInBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
        getSupportActionBar().hide();
        // know initialize the variables that we make at above
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // know we set the progress bar
        progressDialog = new ProgressDialog(signIn.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("please wait\nValidation in progress");
        //know set OnClickListener for even occur after click signing button
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //know we have to check that either user set email or password or not
                if(!binding.email.getText().toString().isEmpty()&&!binding.password.getText().toString().isEmpty())
                {

                    // firstly after click on signing button firstly the progress dialog is visible
                    progressDialog.show();

                    // signInWithEmailAndPassword is predefined method that we use to declare on above as a firebase auth
                    // by this we can easily validate the email and password from database and decrease many lines of code
                    auth.signInWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString())
                   // addOnCompleteListener() is a listener method
                   // in Android Studio that can be used to perform an action when a particular task is completed in a Firebase database.
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // when signing is completely done then come inside this method
                                    // so after complete of signing we dismiss progress dialog
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        // if the signing is complete then we jump to main activity
                                        // by using intent:--> used for jump from one activity to another activity
                                        Intent intent = new Intent(signIn.this,MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(signIn.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(signIn.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                }


            }
        });
        // know these line of code we check if the user is already login one time
        // then when ever the user open application then there is not need to
        // show login page to user
        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(signIn.this,MainActivity.class);
            startActivity(intent);
        }
        // know we code for click to signup textview if user want to sign up
        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // create Intent to jump to directly on signup activity
                Intent intent = new Intent(signIn.this,signup.class);
                startActivity(intent);
            }
        });
    }
}