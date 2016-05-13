package com.example.musictube.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PanicResponderActivity extends Activity {

    public static final String PANIC_TRIGGER_ACTION = "info.guardianproject.panic.action.TRIGGER";

    /* Den här activity:n hjälper till att rensa bort videon om appen startar om
     * eller om appen crashar */
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && PANIC_TRIGGER_ACTION.equals(intent.getAction())) {
            // TODO rensa sökresultaten när appen startar om
            // Eller om appen laddar den aktuella video efter att den har dödas , som ska rensas också
            ExitActivity.exitAndRemoveFromRecentApps(this);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}

