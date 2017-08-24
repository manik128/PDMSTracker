package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class Task extends Fragment {
    LayoutInflater mInflater;
    Button taskdet;
    TextView tasktm,taskdesc,notask;
    int empid;
     ProgressDialog pd1;
    Mainclass mclass;
    ProgressBar pd;
    LinearLayout tasklay;
    ConnectionInternet internet;
    ArrayList<NameValuePair> nameValuePairs;;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    public Task() {
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
        final View rootView = inflater.inflate(R.layout.activity_task, container, false);


        tasklay=(LinearLayout) rootView.findViewById(R.id.tasklay);
       notask=(TextView) rootView.findViewById(R.id.notask);
        pd=(ProgressBar) rootView.findViewById(R.id.progress);
        mclass=(Mainclass) getActivity().getApplicationContext();
        empid=sharedpreferences.getInt("empid",0);
        internet=new ConnectionInternet();;

        if (internet.isConnectingToInternet(getActivity())) {
            pd.setVisibility(View.VISIBLE);
            pd.setProgress(0);
            Mynewtask mt = new Mynewtask();
            mt.execute(new String[]{""});
       }
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


//             pd1 = new ProgressDialog(getActivity());
//
//            // Set progress dialog style horizontal
//            pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
//            // Set the progress dialog background color transparent
//            pd1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//            pd1.setIndeterminate(false);
//
//            // Finally, show the progress dialog
//            pd1.show();
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
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/RetrieveTask";
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
            System.out.println("conn:" + output);
           pd.setVisibility(View.GONE);
            try {

                JSONArray json_data = new JSONArray(output);

                System.out.print("succesful"+output+json_data.length());
                 int flag=0;
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);

                for (int i = 0; i < json_data.length(); i++) {
                    final JSONObject jo = json_data.getJSONObject(i);
                    if (jo.getString("status").equalsIgnoreCase("pending")) {
                        flag=1;
                        LayoutInflater layoutInflator = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View vw = layoutInflator.inflate(R.layout.taskpanel, null);
                        tasktm = (TextView) vw.findViewById(R.id.tasktime);
                        taskdesc = (TextView) vw.findViewById(R.id.taskdescr);
                        taskdet = (Button) vw.findViewById(R.id.genord);
                        final String tskdesc = jo.getString("taskdesc");
                        final String dt = jo.getString("taskdate");
                        final String frmtm = jo.getString("fromtime");
                        final String tasid= jo.getString("taskid");
                       // final String tascontper= jo.getString("personname");
                        final String totm= jo.getString("totime");
                        final String conper= jo.getString("personname");
                        final String conid= jo.getString("contactperson");
                        final String faddr= jo.getString("fromaddress");
                        final String connum= jo.getString("mobileno");
                       System.out.print("taskdesc"+tskdesc);
//                    mclass.setTaskdate(jo.getString("taskdate"));
//                    mclass.setFrtime(jo.getString("fromtime"));
//                    mclass.setTotime(jo.getString("totime"));
//                    mclass.setTaskdescription(jo.getString("taskdesc"));
//                    mclass.setContactperson(jo.getString("contactperson"));
//                    mclass.setFromaddress(jo.getString("fromaddress"));
//                    mclass.setStatus(jo.getString("status"));
                        tasktm.setText(dt + " " + frmtm);
                        taskdesc.setText(tskdesc);

                        tasklay.addView(vw);

                        taskdet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent n1 = new Intent(getActivity(), TaskDetail.class);
                                 n1.putExtra("tskid",tasid);
                                 n1.putExtra("tskdesc",tskdesc);
                                 n1.putExtra("dt", dt);
                                 n1.putExtra("frmtm", frmtm);
                                  n1.putExtra("ctnum", connum);
                                 n1.putExtra("totm",totm);
                                 n1.putExtra("conper",conper);
                                n1.putExtra("cond",conid);
                                 n1.putExtra("faddr", faddr);

                                startActivity(n1);

                            }
                        });
                    }
                    if(flag==0){
                        notask.setText("No task assign to you");
                    }
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