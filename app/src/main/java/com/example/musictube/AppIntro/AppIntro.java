package com.example.musictube.AppIntro;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.musictube.AppIntro.SlidesFragment.FirstSlide;
import com.example.musictube.AppIntro.SlidesFragment.FourthSlide;
import com.example.musictube.AppIntro.SlidesFragment.SecondSlide;
import com.example.musictube.AppIntro.SlidesFragment.ThirdSlide;
import com.example.musictube.R;
import com.github.paolorotolo.appintro.AppIntro2;

public class AppIntro extends AppIntro2 {


    /* Här lägger man till slides som är fragments. AppIntro ärver från AppIntro2 som inte
    *  har onCreate metod utan init metod som man lägger till fragmnets som man har skapat*/
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(new FirstSlide());
        addSlide(new SecondSlide());
        addSlide(new ThirdSlide());
        addSlide(new FourthSlide());
        showStatusBar(false);  // Ser till att activity:n visar i full screen utan statusbar

        setCustomTransformer(new ZoomOutPageTransformer());

        // Tar bort knappar som next och prev så man kan bara switcha mellan olika fragments
        setProgressButtonEnabled(false);

    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    /* Metoden för när man scrollar mellan olika fragments så vissas dem som sidor*/
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
