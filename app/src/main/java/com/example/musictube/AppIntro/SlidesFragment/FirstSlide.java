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
public class FirstSlide extends Fragment {

    /* Fragment 1 för AppIntro activity
    *  Retunerar fragment_first_slide layouten. Kan ändras beroende på hur man
    *  vill att leyouten ska se ut. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_slide, container, false);
    }

}
