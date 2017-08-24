package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Admin on 05-06-2017.
 */

public class Event extends Fragment {
    LayoutInflater mInflater;
    Button taskdet;
    TextView tasktm,taskdesc;
    int empid;
    ProgressBar pd;
    Mainclass mclass;
    LinearLayout tasklay;
    ConnectionInternet internet;
    ArrayList<NameValuePair> nameValuePairs;;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public Event() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = LayoutInflater.from(getActivity());
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final View rootView = inflater.inflate(R.layout.events, container, false);


        tasklay=(LinearLayout) rootView.findViewById(R.id.tasklay);
        pd=(ProgressBar) rootView.findViewById(R.id.progressev);
        mclass=(Mainclass) getActivity().getApplicationContext();
        empid=sharedpreferences.getInt("empid",0);
        internet=new ConnectionInternet();;
        if (internet.isConnectingToInternet(getActivity())) {
            pd.setVisibility(View.VISIBLE);
            pd.setProgress(0);
            Mynewtask mt = new Mynewtask();
            mt.execute(new String[]{""});}
        else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                            Intent n = new Intent(getActivity().getApplicationContext(), TabHome.class);

                            startActivity(n);
                            getActivity().finish();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        return rootView;
    }
    public class Mynewtask extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        //ProgressDialog pd;
        String result = null;

        public Mynewtask() {
            //this.lat = Double.toString(lat);
            //this.lang = Double.toString(lang);
            this.addr = addr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pd = new ProgressDialog(getActivity());
            pd.setMessage("Item Loading.....");
            pd.setCancelable(false);
            pd.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            //System.out.println("passing address: " + addr);
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/Event";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(empid)));
            //nameValuePairs.add(new BasicNameValuePair("lat", lat));
            // nameValuePairs.add(new BasicNameValuePair("accessid", access));
            //nameValuePairs.add(new BasicNameValuePair("lang",lang));
            // nameValuePairs.add(new BasicNameValuePair("addr", addr));
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
            pd.setVisibility(View.GONE);
            try {

                JSONArray json_data = new JSONArray(output);

                System.out.print("succesful"+output);

                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);

                for (int i = 0; i < json_data.length(); i++) {
                    final JSONObject jo = json_data.getJSONObject(i);

                        LayoutInflater layoutInflator = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View vw = layoutInflator.inflate(R.layout.eventpanel, null);
                        tasktm = (TextView) vw.findViewById(R.id.tasktime);
                        taskdesc = (TextView) vw.findViewById(R.id.taskdescr);
                        //taskdet = (Button) vw.findViewById(R.id.genord);
                        final String tskdesc = jo.getString("title");
                        //final String dt = jo.getString("start");
                        final String frmtm = jo.getString("start");
                        //final String tasid= jo.getString("taskid");

                        final String totm= jo.getString("end");
                        //final String conper= jo.getString("contactperson");
                        //final String faddr= jo.getString("fromaddress");
                        //System.out.print("taskdesc"+tskdesc);
//                    mclass.setTaskdate(jo.getString("taskdate"));
//                    mclass.setFrtime(jo.getString("fromtime"));
//                    mclass.setTotime(jo.getString("totime"));
//                    mclass.setTaskdescription(jo.getString("taskdesc"));
//                    mclass.setContactperson(jo.getString("contactperson"));
//                    mclass.setFromaddress(jo.getString("fromaddress"));
//                    mclass.setStatus(jo.getString("status"));
                        tasktm.setText(tskdesc);
                        taskdesc.setText("Start Date :"+frmtm + "  "+"End Date :" + totm);

                        tasklay.addView(vw);



                }
                // tasklay.setView(vw);
                //pd.dismiss();
                //session(mail);
                //Intent n1=new Intent(RegistrationPage.this,LoginPage.class);
                //n1.putExtra("name", nam);
                //startActivity(n1);
                //finish();




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
