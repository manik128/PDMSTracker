package com.atss.admin.concealtracking;

/**
 * Created by Manik on 13-06-2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(status.equalsIgnoreCase("Not connected to Internet")){
        editor.putString("duty","off");
        editor.commit();}



        Toast.makeText(context, status+"you should off duty first", Toast.LENGTH_LONG).show();
    }
}