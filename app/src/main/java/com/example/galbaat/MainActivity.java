package com.example.galbaat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.example.galbaat.adpters.fragmentadapter;
import com.example.galbaat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


     ActivityMainBinding binding;
     // by this we can able to access button , text-fields,textview  in main activity without getting their variable
    FirebaseAuth auth; // Firebase auth (object)
    // we create this Firebase auth because we have to logout whenever
    // we click on logout button in menu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                           System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

                        System.out.println(token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
        // below color is used to change the color of action bar as same as tabview
      Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.watsappcolor)));
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        // know we initialize the upper object Firebase auth

        // know we set the view pager
        binding.viewpager.setAdapter(new fragmentadapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }
  // inside this method we set the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
//  this method is for message on selecting item in menu-bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         // we use switch case here because we have different items in menu bar

        // onOptionsItemSelected() is a callback method in Android Studio that
        //is called when an item in the options menu is selected. This method
        // is implemented in an activity or fragment that has an options menu defined.

        switch (item.getItemId()){
            case R.id.settings:
               // Toast.makeText(this, "Setting is clicked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this,Setting.class);
                startActivity(intent2);
                break;
                
            case R.id.groupchat:
              // we want that when ever the user select group chat from the menu the new activity of group chat is opened.
                // so for that purpose we create a Intent
                Intent intent1 = new Intent(MainActivity.this,groupchatActivity.class);
                startActivity(intent1);

                break;
            case R.id.Logout:
                // so when ever we click on logout we have to logout from out application
                auth.signOut();// it is predefined method provided by firebase for logout purpose
                // create intent so when ever we log out we jump to signing activity
                Intent intent = new Intent(MainActivity.this,signIn.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}