package com.atss.admin.concealtracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Manik on 18-04-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        Intent registrationComplete = null;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        System.out.print( "Refreshed token: " + refreshedToken);
        registrationComplete = new Intent(REGISTRATION_SUCCESS);
        registrationComplete.putExtra("token",refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        //Toast.makeText(getApplicationContext(),"new msg"+refreshedToken,Toast.LENGTH_LONG).show();
        //Putting the token to the intent

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        SharedPreferences pref = getApplicationContext().getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
