package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    LatLng sydney;
    Marker m1 = null;
    Polyline line;
    int empid;
    Mainclass mclass;
    Geocoder geocoder;
    List<Address> addresses = null;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<NameValuePair> nameValuePairs;
    ;
    String[] spiltaddr, spiltaddr2;
    TextView mapaddr;
    SharedPreferences sp;
    String fulladdr, access, address = null, province = null, country = null, postalCode = null, knownName = null, local = null, local2 = null;
    private BroadcastReceiver GPSBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sp = getSharedPreferences(Home.MyPREFERENCES, 0);
        mclass = (Mainclass) getApplicationContext();
        mapaddr = (TextView) findViewById(R.id.mapaddr);
        access = sp.getString("access", null);
        empid = sp.getInt("empid", 0);
        mapaddr.setText(mclass.getAddress());
        Typeface type = Typeface.createFromAsset(this.getAssets(), "GothamRoundedLight_21020.ttf");
        mapaddr.setTypeface(type);
        //gpstrack();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        // Add a marker in Sydney and move the camera

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
//        LatLng markerLoc=new LatLng(mclass.getLat(), mclass.getLang());
//        final CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(markerLoc)      // Sets the center of the map to Mountain View
//                .zoom(13)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                .build();
//        mMap.addMarker(new MarkerOptions().position(new LatLng(mclass.getLat(), mclass.getLang())).title("Marker"));
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                return true;
//            }
//        });
        //mMap.addMarker(new MarkerOptions().position(sydney).title(mclass.getLocadd()));
        //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(mclass.getLat(), mclass.getLang()) , 18.0f) );

    }
    @Override
    public void onConnected(Bundle bundle) {
        checkPermission();

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        gpstrack();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        ArrayList<LatLng> points = new ArrayList<LatLng>();
        points.add(latLng);
        Bitmap markerBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(markerBitmap);
        Drawable shape = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_excutiveman, null);
        shape.setBounds(0, 0, markerBitmap.getWidth(), markerBitmap.getHeight());
        shape.draw(canvas);
        if(m1!= null){
          m1.remove();
        }
        mclass.setLat(latitude);
        mclass.setLang(longitude);
        final MarkerOptions markerOptions = new MarkerOptions();

        sydney = new LatLng(mclass.getLat(), mclass.getLang());
        markerOptions.position(sydney);

        // Setting custom icon for the marker
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));

        // Setting title for the infowindow
        markerOptions.title(mclass.getBde());
     
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line=mMap.addPolyline(options);
        // Adding the marker to the map
        m1= mMap.addMarker(markerOptions);
        m1.showInfoWindow();
                //sydney = new LatLng(mclass.getLat(), mclass.getLang());
        geocoder = new Geocoder(this);
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

            mclass.setAddress(fulladdr);
            mapaddr.setText(fulladdr);
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

    public void gpstrack() {
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //String bestProvider = locationManager.getBestProvider(criteria, true);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        checkPermission();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0,  this);
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
    private boolean checkPermission() {
        boolean flag = true;
        String[] permissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
}
