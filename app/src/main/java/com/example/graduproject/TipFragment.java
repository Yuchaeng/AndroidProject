package com.example.graduproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class TipFragment extends Fragment {
    ImageView [] images = new ImageView[5];
    int getId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tip, container, false);

        for(int i=0;i<images.length;i++) {
            getId = getResources().getIdentifier("image"+i,"id","com.example.graduproject");
            images[i]= (ImageView) v.findViewById(getId);
        }












        return v;
    }

}