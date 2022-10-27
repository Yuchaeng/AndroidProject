package com.example.graduproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends FragmentStateAdapter {
   /*private List<Fragment> fragmentList = new ArrayList<>();*/
   public int mCount;

   public VPAdapter(FriendFragment fa, int count){
       super(fa);
       mCount = count;
    }
    /*public void addFragment(Fragment fragment){
       fragmentList.add(fragment);
    }
*/
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new FriendTabFragment();
        else return new FriendTabFragment2();

        /*return fragmentList.get(position);*/
    }

    @Override
    public int getItemCount() {
        return 2000 ;
    }

    public int getRealPosition(int position) {return position &mCount;}
}