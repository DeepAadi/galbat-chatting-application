// work of Fragment adapter

// adapters are an essential component of building
// Android applications as they enable developers to
// connect their UI components with the data they want
// to display,
package com.example.galbaat.adpters;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.galbaat.fregments.Calls;
import com.example.galbaat.fregments.Chats;
import com.example.galbaat.fregments.Status;

public class fragmentadapter extends FragmentPagerAdapter
{
    public fragmentadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // inside this method we use switch case for different selection for different cases
        switch (position) {
            case 0: return new Chats();
            case 1: return new Status();
            case 2: return new Calls();
            default:return new Chats();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
    // now we create new method that is get pager title

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if(position == 0){
            title = "CHATS";
        }
        if(position == 1){
            title = "STATUS";
        }
        if(position == 2){
            title = "CALLS";
        }
        return title;
    }
}
