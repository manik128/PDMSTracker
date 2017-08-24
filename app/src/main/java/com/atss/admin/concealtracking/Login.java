package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    ActionBar abr;
    Button b1;
    String token;
    String id;
    EditText ed1,ed2;
    TextView tx1,greet;
    ArrayList<NameValuePair> nameValuePairs;;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sp;
    ConnectionInternet internet;
    HttpURLConnection urlConnection;
    BufferedReader bufferedReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().hide();
        ed1=(EditText)findViewById(R.id.login);
        ed2=(EditText)findViewById(R.id.pass);
        greet=(TextView)findViewById(R.id.greet);
        //ed3=(EditText)findViewById(R.id.access);
        tx1=(TextView)findViewById(R.id.enquiry);
        internet=new ConnectionInternet();
        sp  = getSharedPreferences(Login.MyPREFERENCES, 0);
        //token=sp.getString("regId", null);
        abr=getSupportActionBar();
        abr.setTitle("Login");
        checkPermission();
        checkPermission2();
        checkPermission3();
     //   abr.hide();




        b1=(Button)findViewById(R.id.button);
        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ed1.getText().toString().equalsIgnoreCase("")){greet.setText("");}
                  else{greet.setText("Hi  "+ed1.getText().toString());}

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
        tx1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent n = new Intent(getApplicationContext(),Enquiry.class);
                startActivity(n);
                return false;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(ed1.getText().toString().equals("")&&ed2.getText().toString().equals("")){
                     Toast.makeText(Login.this, "Fields are blank", Toast.LENGTH_SHORT).show();
                 }
                else if(ed1.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Fields are blank", Toast.LENGTH_SHORT).show();
                }
                if(ed2.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Fields are blank", Toast.LENGTH_SHORT).show();
                }
                else {
                     if (internet.isConnectingToInternet(Login.this)) {
                         Mynewtask2 mt = new Mynewtask2(ed1.getText().toString(), ed2.getText().toString());
                         mt.execute(new String[]{""});
//                         Intent en = new Intent(getApplicationContext(), TabHome.class);
//                         startActivity(en);

                         //finish();
                     }
                     else{
                         Toast.makeText(Login.this, "Internet Connection error", Toast.LENGTH_SHORT).show();
                     }
                 }


            }
        });

/*
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
      //  collapsingToolbar.setTitle("Title");

*/



    }
    public class Mynewtask2 extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        String user, pass,access;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask2(String userid,String pass) {
            this.user=userid;
            this.pass=pass;
            //this.access=access;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this);
            pd.setMessage("Wait.....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            System.out.println("passing address: " + addr);
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/Bdeuser";
            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("userid", user));
            //nameValuePairs.add(new BasicNameValuePair("accessid",access));
            nameValuePairs.add(new BasicNameValuePair("password", pass));

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
                int code = json_data.getInt("deviceid");
                String comp=json_data.getString("company");
                if(code==0){
                    pd.dismiss();
                    Toast.makeText(getBaseContext(), "Wrong Login id and password with this "+user+pass,Toast.LENGTH_SHORT).show();
                }
//                else if(log.equalsIgnoreCase("yes")){
//                    pd.dismiss();
//                    Toast.makeText(getBaseContext(), "You already login other device with username"+user+"",Toast.LENGTH_SHORT).show();
//                }

                else {

                    System.out.print(code);
                    // if (code == 1) {
                    //session(mail);
                    pd.dismiss();
                    SharedPreferences.Editor editor =  sp.edit();
                    editor.putBoolean("login", true);
                    editor.putString("nm", user);
                    editor.putString("access", access);
                    editor.putString("comp", comp);
                    editor.putInt("empid", code);
                    editor.commit();
                    Intent en = new Intent(getApplicationContext(), TabHome.class);
                    startActivity(en);

                    finish();
                }
                ///


                // }
                //session(mail);
                //Intent n1=new Intent(RegistrationPage.this,LoginPage.class);
                //n1.putExtra("name", nam);
                //startActivity(n1);
                //finish();


                //else {
                // Toast.makeText(con, "Sorry, Phone Number is not correct",
                //Toast.LENGTH_LONG).show();
                //}
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
   /* public class Mynewtask2 extends AsyncTask<String, Void, Boolean> {
        String name, pass;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask2(String name, String pass) {
          this.name=name;
          this.pass=pass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this );
            pd.setMessage("Loading.....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //insert();
            boolean user=false;
            String output = null;
            ArrayList<String> ar1;
            ar1=new ArrayList<>();
           user = getOutputFromUrl("http://concealtracking.com/PdmsTrackapp/Bdeuser");

            return user;
        }

        @Override
        protected void onPostExecute(Boolean output) {
            System.out.println("conn:" + output);
            if(output==true){
            SharedPreferences.Editor editor =  sp.edit();
            editor.putBoolean("login", true);
                editor.putString("nm", name);
            editor.putString("ps", pass);
                editor.putString("empid", id);
            editor.commit();
            Intent n = new Intent(getApplicationContext(),Home.class);
            startActivity(n);
            finish();}
            else{
                Toast.makeText(Login.this, "Username and password are wrong", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();

        }
        private Boolean getOutputFromUrl(String url) {
            String output = "";
            Boolean value=false;
            try {
                URL url2 = new URL
                        (url);
                urlConnection =
                        (HttpURLConnection) url2.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;
                while ((next = bufferedReader.readLine()) != null){
                    JSONArray ja = new JSONArray(next);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo =  ja.getJSONObject(i);


                        if(jo.getString("loginId").equalsIgnoreCase(name)&& jo.getString("password").equalsIgnoreCase(pass))
                        {
                             value=true;
                            id=jo.getString("deviceid");

                        }
                        //output=output+jo.getString("name");
                        //System.out.println("Insert successful"+jo.getString("pname"));



                    }

                    //Log.w("Insert","Parent insert successfully");
                    //System.out.println("custid "+item);

                    Log.e("sample", "Insert successful");
                }
            }
            catch(Exception e)
            {
                Log.e("Fail 1", e.toString());

            }
            return value;
        }

    }*/
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
    private boolean checkPermission2() {
        boolean flag = true;
        String[] permissions = {"android.permission.RECEIVE_SMS","android.permission.READ_SMS"};
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 3);
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;

    }
    private boolean checkPermission3() {
        boolean flag = true;
        String[] permissions = {"android.permission.UPDATE_DEVICE_STATS","android.permission.MODIFY_PHONE_STATE","android.permission.READ_PHONE_STATE"};
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
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
