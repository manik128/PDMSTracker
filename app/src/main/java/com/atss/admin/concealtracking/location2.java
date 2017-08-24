package com.atss.admin.concealtracking;

/**
 * Created by Admin on 15-06-2017.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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

public class location2 extends Fragment implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    // LogCat tag
    private static final String TAG = location2.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    List<Address> addresses = null;
    String[] spiltaddr,spiltaddr2;
    private Location mLastLocation;
    Mainclass mclass;
    TextView bde,cmpname;
    Integer empid;
    ArrayList<NameValuePair> nameValuePairs;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sp;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;
    Geocoder geocoder;
    private String fulladdr,username,access;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    // UI elements
    private TextView lblLocation;
    private Button btnShowLocation, btnStartLocationUpdates;
    String comp,address = null,province = null,country = null,postalCode = null,knownName = null,local=null,local2=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);
        sp  =  getActivity().getSharedPreferences(MyPREFERENCES, 0);
        username=sp.getString("nm",null);
        empid=sp.getInt("empid",0);
        comp=sp.getString("comp", null);
        View vw = inflater.inflate(R.layout.fragment_location, container, false);
        lblLocation = (TextView) vw.findViewById(R.id.location);
        btnShowLocation = (Button) vw.findViewById(R.id.gtmap);
        bde=(TextView) vw.findViewById(R.id.bdename);
        cmpname=(TextView) vw.findViewById(R.id.cmpnm);
        mclass=(Mainclass) getActivity().getApplicationContext();
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "GothamRoundedLight_21020.ttf");
        bde.setTypeface(type);
        lblLocation.setTypeface(type);
        mclass.setBde(username);
        bde.setText(username);
        cmpname.setText(comp);
        //btnStartLocationUpdates = (Button) vw.findViewById(R.id.gtmap2);

        // First we need to check availability of play services
        //if (checkPlayServices()) {

        // Building the GoogleApi client
        buildGoogleApiClient();

        createLocationRequest();
        //displayLocation();
        //}

        // Show location button click listener
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent n1=new Intent(getActivity(),MapActivity.class);
                startActivity(n1);

            }
        });

        // Toggling the periodic location updates
//        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                togglePeriodicLocationUpdates();
//            }
//        });
        return vw;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {
        checkPermission();
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            mclass.setLat(latitude);
            mclass.setLang(longitude);
            //lblLocation.setText(latitude + ", " + longitude);
            geocoder = new Geocoder(getActivity());
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
                lblLocation.setText(fulladdr);
                mclass.setAddress(fulladdr);
                //fulladdr= fulladdr.replace(',',' ');
                //fulladdr=fulladdr.replaceAll("\\s+","");
                // a.replaceAll("\\s+","");
                System.out.println("full addr"+fulladdr);
               // loc.setText(fulladdr);
                //mclass.setAddress(fulladdr);
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

        } else {

            lblLocation
                    .setText("Getting location.........");
        }
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
//            btnStartLocationUpdates
//                    .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
//            btnStartLocationUpdates
//                    .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        checkPermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getActivity(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    private boolean checkPermission() {
        boolean flag = true;
        String[] permissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.RECEIVE_SMS"};
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 3);
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;

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
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/latlang";
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
}