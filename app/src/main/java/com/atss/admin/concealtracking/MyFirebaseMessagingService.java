package com.atss.admin.concealtracking;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Manik on 18-04-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String msg="",newmessage="",mainmsg="";
    int mcont=0,msgc;
    UtilityClass uc;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        int ind1=remoteMessage.getData().toString().indexOf("=")+1;
//       int ind2=remoteMessage.getData().toString().indexOf(",");
//       String msg=remoteMessage.getData().toString().substring(ind1,ind2);
//        System.out.print("message:message not coming");
        //sendNotification(remoteMessage.getData().toString());
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());

            JSONObject data = json.getJSONObject("data");
            //String msg=json.getString("body");
            //System.out.print("message2:"+msg);
            //sendNotification(msg);
            mainmsg=data.getString("message");
            //System.out.println("messagenew:"+mainmsg);
            //sendNotification(data.getString("message"));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        uc=new UtilityClass();
        msgc=sharedpreferences.getInt("msgcnt",0);

        newmessage=sharedpreferences.getString("newmsg", null);
        if(newmessage==null){
            newmessage="";
        }
        if(!uc.isAppIsInBackground(this)){
            Intent intent = new Intent(this, TabHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pass",1);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("newmsg", mainmsg);
            editor.putInt("msgcnt", mcont);

            editor.commit();
            startActivity(intent);

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
            r.play();
       }
        else{
            sendNotification(mainmsg);
        }

//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//       // Toast.makeText(this,"message"+remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();
//         System.out.print("message:"+remoteMessage.getNotification().getBody());
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String message) {
        mcont++;
        Intent intent = new Intent(this, TabHome.class);
        intent.putExtra("pass",1);
        intent.putExtra("msgcnt", mcont);
//        String[] events = new String[6];
//        events[0] = new String("This is first line....");
//        events[1] = new String("This is second line...");
//        events[2] = new String("This is third line...");
//        events[3] = new String("This is 4th line...");
//        events[4] = new String("This is 5th line...");
//        events[5] = new String("This is 6th line...");


        //String msg = message;
        msg=newmessage+"\n"+message;

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("newmsg", mainmsg);
        editor.putInt("msgcnt", mcont);

        editor.commit();
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TabHome.class);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            inboxStyle.addLine(mainmsg+"\n");

        //inboxStyle.addLine(msg);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sound);
        r.play();

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logofinal2)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setNumber(mcont)
                .setContentText(mainmsg)
                 .setStyle(inboxStyle)

                .setAutoCancel(true)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build());

        //Intent intent2 = getIntent();
        //Bundle extras = intent2.getExtras();
        //String jsonData = extras.getString("com.parse.Data");
        // Log.d("MyApp",jsonData);
        //tx1.setText(jsonData);//0 = ID of notification
        //Toast.makeText(getApplicationContext(),"message"+message,Toast.LENGTH_LONG).show();
    }
    public boolean isForeground(String PackageName){
        // Get the Activity Manager
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        // Get a list of running tasks, we are only interested in the last one,
        // the top most so we give a 1 as parameter so we only get the topmost.
        List< ActivityManager.RunningTaskInfo > task = manager.getRunningTasks(1);

        // Get the info we need for comparison.
        ComponentName componentInfo = task.get(0).topActivity;

        // Check if it matches our package name.
        if(componentInfo.getPackageName().equals(PackageName)) return true;

        // If not then our app is not on the foreground.
        return false;
    }
}
