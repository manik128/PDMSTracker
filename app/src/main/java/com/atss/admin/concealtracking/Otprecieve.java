package com.atss.admin.concealtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Admin on 25-02-2017.
 */

public class Otprecieve extends BroadcastReceiver {
    String body;
    IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
    final SmsManager sms = SmsManager.getDefault();
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                        // Show Alert
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: "+ senderNum + ", message: " + message, duration);
                        toast.show();

                       //Otp.getInstace().extractnum(message);
                        //Otp.getInstace().updateTheTextView();

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }

//Add it to the list or do whatever you wish to


    }

