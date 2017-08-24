package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Enquiry extends AppCompatActivity {
    Button enq;
    String name,mob,mail;
    EditText nam,phno,email;
     Mainclass mclass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ArrayList<NameValuePair> nameValuePairs;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        enq=(Button)findViewById(R.id.enqbtn);
        nam=(EditText)findViewById(R.id.nam);
        phno=(EditText)findViewById(R.id.mob);
        email=(EditText)findViewById(R.id.mail);
        mclass=(Mainclass) getApplicationContext();
        enq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nam.getText().toString().equals("")&&phno.getText().toString().equals("")&&phno.getText().toString().equals("")){
                    Toast.makeText(Enquiry.this, "Fields are blank", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidPhonenumber(phno.getText().toString())){
                    Toast.makeText(Enquiry.this, "Phone Number is not valid", Toast.LENGTH_SHORT).show();
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(Enquiry.this, "Email Id is not valid", Toast.LENGTH_SHORT).show();
                }
                else{
                     name=nam.getText().toString();
                    mob=phno.getText().toString().trim();
                    mail=email.getText().toString().trim();
                    Mynewtask2 mt = new Mynewtask2();
                    mt.execute(new String[]{""});
                }

            }
        });
    }
   /* */
    private boolean isValidPhonenumber(String phonenumber) {

        if (phonenumber != null && phonenumber.length() >= 10 && phonenumber.length() <= 10) {
            return true;
        }
        return false;
    }
    public class Mynewtask2 extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask2() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Enquiry.this);
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
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/Otpgen";
            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("mobile", mob));

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
                int code = json_data.getInt("code");
                if(code==0){
                    pd.dismiss();
                    Toast.makeText(getBaseContext(), "You Already Register With Us ",Toast.LENGTH_SHORT).show();
                }
                else {
                    mclass.setOtp(code);
                    System.out.print(code);
                    // if (code == 1) {
                    //session(mail);
                    pd.dismiss();
                    mclass.setName(name);
                    mclass.setMobile(mob);
                    mclass.setEmail(mail);
                    Intent en = new Intent(getApplicationContext(), Otp.class);
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
}
