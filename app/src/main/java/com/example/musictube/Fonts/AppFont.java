package com.example.musictube.Fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by Ekrem on 2016-05-07.
 */
public class AppFont {

    /* Ersätter default Fonten för hela appen
     * hämtar fonten från assets mappen och ersätter den befentliga fonten, självklart från
      * MainActivity */
    public static void replaceDefaultFont(Context context,
                                          String defaultFontNameToReplace, String customFontFileNameInAssets) {
        final Typeface customFontTypeface = Typeface.createFromAsset(
                context.getAssets(), customFontFileNameInAssets);
        replaceFont(defaultFontNameToReplace, customFontTypeface);


    }

    private static void replaceFont(String defaultFontNameToReplace, Typeface customFontTypeface) {
        try {
            Field myField = Typeface.class.getDeclaredField(defaultFontNameToReplace);
            myField.setAccessible(true);
            myField.set(null, customFontTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
