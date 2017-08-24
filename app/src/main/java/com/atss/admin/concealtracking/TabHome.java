package com.atss.admin.concealtracking;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TabHome extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String tken,msg,chkdut,comp;
    TextView menubtn;
    Boolean lgout;
    int emp, msgc;
    ListPopupWindow listPopupWindow;
    private BroadcastReceiver mIntentReceiver;;
    private GoogleApiClient mGoogleApiClient;
    Switch swt;
    boolean chktog,tokgen;
    ConnectionInternet internet;
    ArrayList<NameValuePair> nameValuePairs;;
    int pass;
    String[] menuitems={"Logout"};

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);

        sharedpreferences = TabHome.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
        getSupportActionBar().setIcon(R.drawable.logonew2);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        internet=new ConnectionInternet();;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        swt=(Switch)findViewById(R.id.switch1);
        menubtn=(TextView)findViewById(R.id.mn);
        setupViewPager(viewPager);
        final StartLocationAlert startLocationAlert = new StartLocationAlert(TabHome.this);
        //gpstrack();
        //viewPager.setCurrentItem(2);
       emp=sharedpreferences.getInt("empid",0);
        tken=sharedpreferences.getString("regId", null);

        tokgen=sharedpreferences.getBoolean("tokengen", false);
        lgout=sharedpreferences.getBoolean("logout", false);
        msgc=sharedpreferences.getInt("msgcnt",0);
        chkdut=sharedpreferences.getString("duty",null);

        listPopupWindow = new ListPopupWindow(
                TabHome.this);
        listPopupWindow.setAdapter(new ArrayAdapter(
                TabHome.this,
                R.layout.menulist, menuitems));

        listPopupWindow.setAnchorView(menubtn);
        listPopupWindow.setWidth(250);
        listPopupWindow.setHeight(100);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String swit="inactive";
                String onl="offline";
                updateduty upd = new updateduty(swit,onl);
                upd.execute(new String[]{""});
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("logout",true);
                editor.remove("empid");
                editor.remove("nm");
                editor.remove("login");
                editor.remove("duty");
                //editor.clear();
                editor.commit();
                Intent n = new Intent(getApplicationContext(), Login.class);

                startActivity(n);
                finish();
            }
        });
        listPopupWindow.setModal(true);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        //For some reason, setting minWidth in xml and then accessing it here doesn't work, returns 0
        int minWidth = 80;
        tabLayout.setMinimumWidth(minWidth);

        //If there are less tabs than needed to necessitate scrolling, set to fixed/fill
        if(tabLayout.getTabCount() < dpWidth/minWidth){
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else{
            //Otherwise, set to scrollable
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        }
       // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        menubtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });
        if (internet.isConnectingToInternet(TabHome.this)) {

            if(lgout){
                updatedevice mt = new  updatedevice();
                mt.execute(new String[]{""});
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.remove("logout");

            }
            else{
//           // //
}
            if(!tokgen){

            Mynewtask mt = new Mynewtask();
            mt.execute(new String[]{""});}
            else{
            chklogin chk = new chklogin();
            chk.execute(new String[]{""});}
//
      }
        else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("No Intenet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Exit Application",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    System.exit(0);
                              /*  LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                                        .addLocationRequest(mLocationRequest);
                                PendingResult<LocationSettingsResult> result =
                                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
                                result.setResultCallback(mResultCallbackFromSettings);*/
                                }
                            });
            alertDialogBuilder.setNegativeButton("Refresh",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent n = new Intent(getApplicationContext(), TabHome.class);

                            startActivity(n);
                            finish();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        if(chkdut!=null){
if(chkdut.equalsIgnoreCase("on")){
    swt.setChecked(true);
    swt.setText("OnDuty");
}
            else if(chkdut.equalsIgnoreCase("off")){
    swt.setChecked(false);
    swt.setText("OffDuty");
}

        }
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if(isChecked){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabHome.this);
                    alertDialogBuilder.setMessage("Your device activated for Conceal Tracking")
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int id){
                                            startLocationAlert.settingsrequest();
                                            mobiledataenable(true);
                                            String swit="active";
                                            String onl="online";
                                            updateduty upd = new updateduty(swit,onl);
                                            upd.execute(new String[]{""});
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("duty","on");
                                            editor.commit();
                                            swt.setText("OnDuty");
                              /*  LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                                        .addLocationRequest(mLocationRequest);
                                PendingResult<LocationSettingsResult> result =
                                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
                                result.setResultCallback(mResultCallbackFromSettings);*/
                                        }
                                    });
//                    alertDialogBuilder.setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    //swt.setChecked(false);
//                                   dialog.dismiss();
//                                }
//                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
                else{

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabHome.this);
                    alertDialogBuilder.setMessage("Your device deactivated for Conceal Tracking")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int id){
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString("duty","off");
                                            editor.commit();
                                            swt.setText("OffDuty");
                                            String swit="inactive";
                                            String onl="offline";
                                            updateduty upd = new updateduty(swit,onl);
                                            upd.execute(new String[]{""});
                                            mobiledataenable(false);
                              /*  LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                                        .addLocationRequest(mLocationRequest);
                                PendingResult<LocationSettingsResult> result =
                                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
                                result.setResultCallback(mResultCallbackFromSettings);*/
                                        }
                                    });
//                    alertDialogBuilder.setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.dismiss();
//                                    //swt.setChecked(true);
//                                }
//                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
            }
        });

        //Toast.makeText(TabHome.this,"passvalue"+tken,Toast.LENGTH_LONG).show();
        //tabLayout.set
        if(getIntent().getExtras()!=null) {
            pass = getIntent().getExtras().getInt("pass");
            msg= getIntent().getExtras().getString("msgcnt");
            //Toast.makeText(MainActivity.this, "succes" + pass, Toast.LENGTH_LONG).show();
            if (pass == 1) {
                //Chat ab = new Chat();
                //tabLayout.getTabAt(2);
                selectPage(2);
               // Toast.makeText(TabHome.this,"passvalue"+msgc,Toast.LENGTH_LONG).show();


            }
        }
        //Toast.makeText(TabHome.this,"msge come"+tken,Toast.LENGTH_LONG).show();
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new location2(), "Location");
       adapter.addFrag(new Task(), "Task");
        adapter.addFrag(new Chat(), "Chat");
        adapter.addFrag(new Event(), "Events");

        //adapter.addFragment(new TwoFragment(), "TWO");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(TabHome.this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(TabHome.this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }
    void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }
    public class Mynewtask extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = new ProgressDialog(Home.this);
            //pd.setMessage("Item Loading.....");
            //pd.setCancelable(false);
            //pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            System.out.println("passing address: " + addr);
            String url = test.FILE_PATH+"/Registeringdevice";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("empid", Integer.toString(emp)));


            nameValuePairs.add(new BasicNameValuePair("token", tken));
            nameValuePairs.add(new BasicNameValuePair("login", "yes"));

            //output = getOutputFromUrl(url);
            //output = getOutputFromUrl(url);
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            //System.out.println("conn:" + output+access);
            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
                int code = json_data.getInt("code");

                if (code == 1) {
                    //session(mail);
                    SharedPreferences.Editor editor =  sharedpreferences.edit();
                    editor.putBoolean("tokengen", true);

                    editor.commit();

                    /// pd.dismiss();


                }
                //session(mail);
                //Intent n1=new Intent(RegistrationPage.this,LoginPage.class);
                //n1.putExtra("name", nam);
                //startActivity(n1);
                //finish();


                else {
//                    Toast.makeText(con, "Sorry, Phone Number is not correct",
//                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class updateduty extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null,switc,stat;

        public updateduty(String sw,String stat) {
            this.switc=sw;
            this.stat=stat;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = new ProgressDialog(Home.this);
            //pd.setMessage("Item Loading.....");
            //pd.setCancelable(false);
            //pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            System.out.println("passing address: " + addr);
            String url = test.FILE_PATH+"/updatedduty";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(emp)));


            nameValuePairs.add(new BasicNameValuePair("duty", switc));
            nameValuePairs.add(new BasicNameValuePair("active", stat));


            //output = getOutputFromUrl(url);
            //output = getOutputFromUrl(url);
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            System.out.println("DUTY:" + output);
            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
                int code = json_data.getInt("code");

                if (code == 1) {
                    //session(mail);


                    /// pd.dismiss();


                }
                //session(mail);
                //Intent n1=new Intent(RegistrationPage.this,LoginPage.class);
                //n1.putExtra("name", nam);
                //startActivity(n1);
                //finish();


                else {
                    Toast.makeText(TabHome.this, "Sorry, Phone Number is not correct",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class updatedevice extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public updatedevice() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = new ProgressDialog(Home.this);
            //pd.setMessage("Item Loading.....");
            //pd.setCancelable(false);
            //pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            System.out.println("passing address: " + addr);
            String url = test.FILE_PATH+"/updatedevice";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("empid", Integer.toString(emp)));


            nameValuePairs.add(new BasicNameValuePair("token", tken));

            //output = getOutputFromUrl(url);
            //output = getOutputFromUrl(url);
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            //System.out.println("conn:" + output+access);
            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
                int code = json_data.getInt("code");

                if (code == 1) {
                    //session(mail);
                    SharedPreferences.Editor editor =  sharedpreferences.edit();
                    editor.putBoolean("tokengen", true);

                    editor.commit();

                    /// pd.dismiss();


                }
                //session(mail);
                //Intent n1=new Intent(RegistrationPage.this,LoginPage.class);
                //n1.putExtra("name", nam);
                //startActivity(n1);
                //finish();


                else {
                    Toast.makeText(con, "Sorry, Phone Number is not correct",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class chklogin extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null,switc;

        public chklogin() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd = new ProgressDialog(Home.this);
            //pd.setMessage("Item Loading.....");
            //pd.setCancelable(false);
            //pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            System.out.println("passing address: " + addr);
            String url = test.FILE_PATH+"/Devicechk";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("empid", Integer.toString(emp)));


            nameValuePairs.add(new BasicNameValuePair("token", tken));

            //output = getOutputFromUrl(url);
            //output = getOutputFromUrl(url);
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            System.out.println("conn:" + output);
            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
              String code = json_data.getString("status");

                if (code.equalsIgnoreCase("failure")) {
                    //session(mail);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("empid");
                    editor.remove("nm");
                    editor.remove("login");
                    editor.remove("duty");
                    editor.commit();
//                    Intent n = new Intent(getApplicationContext(),Login.class);
//                    startActivity(n);
//                    finish();
                    /// pd.dismiss();


                }


                //session(mail);
                //Intent n1=new Intent(RegistrationPage.this,LoginPage.class);
                //n1.putExtra("name", nam);
                //startActivity(n1);
                //finish();


                else {
                    Toast.makeText(TabHome.this, "Sorry, Phone Number is not correct",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void mobiledataenable(boolean enabled) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class<?> conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        //IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    Log.d("NetworkCheckReceiver", "NetworkCheckReceiver invoked...");


                    boolean noConnectivity = intent.getBooleanExtra(
                            ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

                    if (!noConnectivity) {
                        Log.d("NetworkCheckReceiver", "connected");
                        Toast.makeText(TabHome.this, "connected",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.d("NetworkCheckReceiver", "disconnected");
                        Toast.makeText(TabHome.this, "disconnected",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        };
        //this.registerReceiver(mIntentReceiver, intentFilter);

    }
    public void listpopup() {

        //listPopupWindow.setOnItemClickListener(TabHome.this);


    }
}
