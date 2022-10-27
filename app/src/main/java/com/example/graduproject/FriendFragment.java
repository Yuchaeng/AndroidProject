/*
package com.example.graduproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class FriendFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter adapter;
    private int num_page = 2;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabLayout = getView().findViewById(R.id.tabs_friend);
        viewPager2 = getView().findViewById(R.id.view_pager2);
        adapter = new VPAdapter(this, num_page);

        viewPager2.setAdapter(adapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setCurrentItem(1000);
        viewPager2.setOffscreenPageLimit(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }
}*/

//package com.example.graduproject;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//
//import android.os.Bundle;
//
//import com.google.android.material.tabs.TabLayout;
//
//public class FriendFragment extends Fragment {
//
//    TabLayout tabs;
//
//    FriendTabFragment fragment1;
//    FriendTabFragment2 fragment2;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_friend_tab);

//        fragment1 = new FriendTabFragment();
//        fragment2 = new FriendTabFragment2();

        //getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

        //tabs = findViewById(R.id.tabs);
//        tabs.addTab(tabs.newTab().setText("친구 목록"));
//        tabs.addTab(tabs.newTab().setText("추천 친구"));
//
//        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                Fragment selected = null;
//                if (position == 0)
//                    selected = fragment1;
//                else if (position == 1)
//                    selected = fragment2;
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }

        // });
//    }
//}

package com.example.graduproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }
}

