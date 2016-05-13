package com.example.musictube.AppIntro.SlidesFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.musictube.R;
import com.example.musictube.SplashScreen.SplashScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourthSlide extends Fragment {


    /*  Fragment 4 för AppIntro activity
     *  Har en knapp som anroppar SplashScreen activity. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fourth_slide, container, false);

        Button mButton = (Button) v.findViewById(R.id.start_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_activity = new Intent(getActivity(), SplashScreen.class);
                getActivity().startActivity(start_activity);
            }
        });
        return v;
    }
}
