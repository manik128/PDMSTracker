package com.atss.admin.concealtracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements LocationListener {
  Button addstr,existstr;
    Geocoder geocoder;
    List<Address> addresses = null;
    String[] spiltaddr,spiltaddr2;
    ConnectionInternet cn;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sp;
    Mainclass mclass;
    ArrayList<NameValuePair> nameValuePairs;;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private String fulladdr,username,access;
    int empid;
    TextView loc,bde;
    Button gtmap;
    String address = null,province = null,country = null,postalCode = null,knownName = null,local=null,local2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        sp  = getSharedPreferences(Home.MyPREFERENCES, 0);
        loc=(TextView)findViewById(R.id.location);
        bde=(TextView)findViewById(R.id.bdename);
        gtmap=(Button)findViewById(R.id.gtmap);
        username=sp.getString("nm",null);
       access=sp.getString("access",null);
        empid=sp.getInt("empid",0);
        cn=new ConnectionInternet();
        bde.setText(username);
        Typeface type = Typeface.createFromAsset(this.getAssets(), "GothamRoundedLight_21020.ttf");
        Typeface type2 = Typeface.createFromAsset(this.getAssets(), "Gotham-Rounded-Medium_21022.ttf");
        loc.setTypeface(type);
        bde.setTypeface(type2);
        mclass=(Mainclass) getApplicationContext();
        mclass.setBde(username);

        gtmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n1=new Intent(Home.this,MapActivity.class);
                startActivity(n1);
            }
        });
        if(cn.isConnectingToInternet(Home.this)){
            Activity mContext = Home.this;//change this your activity name
            StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
            gpstrack();
        }
        else{
            //Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG)
            //.setAction("OK", null).show();
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
                            Intent n = new Intent(getApplicationContext(), Home.class);

                            startActivity(n);
                            finish();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mclass.setLat(latitude);
        mclass.setLang(longitude);

        geocoder = new Geocoder(Home.this);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            //get current Street name
            address = addresses.get(0).getAddressLine(0)+addresses.get(0).getAddressLine(1);

            //get current province/City

            local2=addresses.get(0).getAddressLine(2);
            //local= "*"+addresses.get(0).getAddressLine(2);
            country = addresses.get(0).getAddressLine(3);
            spiltaddr= local2.split(",");
            spiltaddr2= spiltaddr[1].split("\\s");
            province = spiltaddr2[1];
            local=spiltaddr[0];
            postalCode=spiltaddr2[2];
            fulladdr=address+","+local+","+province+","+postalCode+","+country;

            //fulladdr= fulladdr.replace(',',' ');
            //fulladdr=fulladdr.replaceAll("\\s+","");
            // a.replaceAll("\\s+","");
            System.out.println("full addr"+fulladdr);
            loc.setText(fulladdr);
            mclass.setAddress(fulladdr);
            //fulladdr.replaceAll(","," ");
            //get postal code
            // postalCode = "*"+addresses.get(0).getAddressLine(4);
            //Toast.makeText(GpsTrack.this,"add:"+spiltaddr[0]+spiltaddr2[1]+spiltaddr2[2],Toast.LENGTH_LONG).show();
            //get place Name
            //knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            /*return  address + "\t"  + province + "\t " + country
                    + "\t" + postalCode + "\t"  + knownName;*/
            //Toast.makeText(Home.this, "lat:" + fulladdr, Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            ex.printStackTrace();


        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();

        }
        Mynewtask mt = new Mynewtask(latitude, longitude,fulladdr);
        mt.execute(new String[]{""});
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public class Mynewtask extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask(Double lat, Double lang, String addr) {
            this.lat = Double.toString(lat);
            this.lang = Double.toString(lang);
            this.addr = addr;
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
            String url = "http://pdms.routetrees.com/PdmsTrackapp/latlang";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("empid",Integer.toString(empid)));
            nameValuePairs.add(new BasicNameValuePair("lat", lat));
            nameValuePairs.add(new BasicNameValuePair("accessid", access));
            nameValuePairs.add(new BasicNameValuePair("lang",lang));
            nameValuePairs.add(new BasicNameValuePair("addr", addr));
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
            System.out.println("conn:" + output+access);
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
                    Toast.makeText(con, "Sorry, Phone Number is not correct",
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private String getOutputFromUrl(String url) {
            String output = null;
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
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
    }
    public void gpstrack() {
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //String bestProvider = locationManager.getBestProvider(criteria, true);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        checkPermission();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, this);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            System.out.println("passing address: " + location);
            if (location != null) {
                onLocationChanged(location);
                //locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);


                //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

                //}
            }
        }
        else{
            //showGPSDisabledAlertToUser();
        }
    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                              /*  LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                                        .addLocationRequest(mLocationRequest);
                                PendingResult<LocationSettingsResult> result =
                                        LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
                                result.setResultCallback(mResultCallbackFromSettings);*/
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case 0:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        if (mGoogleApiClient.isConnected() ) {
                            //startLocationUpdates();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
       Activity mContext = Home.this;//change this your activity name
        StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
        gpstrack();
        //requestLocationUpdates();

    }
    private boolean checkPermission() {
        boolean flag = true;
        String[] permissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION","android.permission.RECEIVE_SMS"};
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 3);
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;

    }


}
