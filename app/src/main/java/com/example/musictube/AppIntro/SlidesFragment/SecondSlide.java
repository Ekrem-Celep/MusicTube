package com.example.musictube.AppIntro.SlidesFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musictube.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondSlide extends Fragment {


    /*  Fragment 2 för AppIntro activity
    *  Retunerar just nu bara layoute */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second_slide, container, false);
        return v;
    }

}
