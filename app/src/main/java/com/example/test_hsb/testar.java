package com.example.test_hsb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class testar extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
//            Intent i = new Intent(context, MyOwnMain.class);

            Intent i = new Intent(context, MainActivity.class);
            // we want to start an activity outside our app
            // otherwise it will crash om some api-levels
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
