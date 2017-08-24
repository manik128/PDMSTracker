package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Otp extends AppCompatActivity {
   EditText ot,ot1,ot2,ot3;
    private BroadcastReceiver mIntentReceiver;;
    Mainclass mclass;
    String body,recotp;
    String name,mob,mail,msg;
    ArrayList<NameValuePair> nameValuePairs;
    Button verify;
    StringBuilder myNumbers;
    TextView tx;
   // EditText rs;
    private static Otp ins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mclass=(Mainclass) getApplicationContext();
        name=mclass.getName();
        mob=mclass.getMobile();
        mail=mclass.getEmail();
        ins = this;
        ot=(EditText)findViewById(R.id.ot);
        ot1=(EditText)findViewById(R.id.ot1);
        ot2=(EditText)findViewById(R.id.ot2);
        ot3=(EditText)findViewById(R.id.ot3);
        tx=(TextView) findViewById(R.id.tit);
       // rs=(EditText) findViewById(R.id.tit2);
        verify=(Button)findViewById(R.id.verify);
        mclass=(Mainclass) getApplicationContext();
        recotp=Integer.toString(mclass.getOtp());
         myNumbers = new StringBuilder();
        tx.setText("One time password sent to "+mclass.getMobile());
        ot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ot.getText().toString().equalsIgnoreCase("")){

                }
                else{
                ot1.requestFocus();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ot1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(ot1.getText().toString().equalsIgnoreCase("")){

                }
                else{
                ot2.requestFocus();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ot2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(ot2.getText().toString().equalsIgnoreCase("")){

                }
                else{
                ot3.requestFocus();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ot.getText().toString().equals("")&&ot1.getText().toString().equals("")&&ot2.getText().toString().equals("")&&ot3.getText().toString().equals("")){
                    Toast.makeText(Otp.this, "Fields are blank", Toast.LENGTH_SHORT).show();
                }
                else{
                    String otp=ot.getText().toString()+ot1.getText().toString()+ot2.getText().toString()+ot3.getText().toString();
                    if(otp.equalsIgnoreCase(recotp)){
                       Mynewtask mt = new Mynewtask();
                        mt.execute(new String[]{""});
                    }
                    else{
                        Toast.makeText(Otp.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("get_msg");

//Process the sms format and extract body &amp; phoneNumber
                msg = msg.replace("\n", "");
               body = msg.substring(msg.lastIndexOf(":")+1, msg.length());
                //String pNumber = msg.substring(0,msg.lastIndexOf(":"));
                System.out.print("body:"+body);
                for (int i = 0; i < body.length(); i++) {
                    if (Character.isDigit(body.charAt(i))) {
                        myNumbers.append(body.charAt(i));

                        System.out.println(body.charAt(i) + " is a digit.");
                    } else {
                        System.out.println(body.charAt(i) + " not a digit.");
                    }
                }
               ot.setText( String.valueOf(myNumbers.charAt(0)));
                ot1.setText( String.valueOf(myNumbers.charAt(1)));
                ot2.setText( String.valueOf(myNumbers.charAt(2)));
                ot3.setText( String.valueOf(myNumbers.charAt(3)));
                String otp=ot.getText().toString()+ot1.getText().toString()+ot2.getText().toString()+ot3.getText().toString();
                if(otp.equalsIgnoreCase(recotp)){
                 Mynewtask mt = new Mynewtask();
                    mt.execute(new String[]{""});
                }
                //rs.setText("r");
                //System.out.print("pNumber:"+pNumber);
                //extractnum(body);

               //Otp.getInstace().updateTheTextView();


//Add it to the list or do whatever you wish to

            }
        };
        this.registerReceiver(mIntentReceiver, intentFilter);

    }

    @Override
    protected void onPause() {

        super.onPause();
        //this.unregisterReceiver(this.mIntentReceiver);

    }
    public void extractnum(String msg){
        String str = msg;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                myNumbers.append(str.charAt(i));

                System.out.println(str.charAt(i) + " is a digit.");
            } else {
                System.out.println(str.charAt(i) + " not a digit.");
            }
            /*Otp.this.runOnUiThread(new Runnable() {
                public void run() {
                    ot.setText(myNumbers.charAt(0));
                     ot1.setText(myNumbers.charAt(1));
                    ot2.setText(myNumbers.charAt(2));
                    ot3.setText(myNumbers.charAt(3));

                }
            });*/
           // ot.setText(myNumbers.charAt(0));
           // ot1.setText(myNumbers.charAt(1));
           // ot2.setText(myNumbers.charAt(2));
            //ot3.setText(myNumbers.charAt(3));
        }
        System.out.println("Your numbers: " + myNumbers.toString()+myNumbers.charAt(0));
    }

    public static Otp  getInstace(){
        return ins;
    }
    public void updateTheTextView() {
       Otp.this.runOnUiThread(new Runnable() {
            public void run() {
                ot.setText(myNumbers.charAt(0));
                ot1.setText(myNumbers.charAt(1));
                ot2.setText(myNumbers.charAt(2));
                ot3.setText(myNumbers.charAt(3));
            }
        });
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
            pd = new ProgressDialog(Otp.this);
            pd.setMessage("wait.....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //insert();
            String output = null;
            String addr1 = "1554,sdfjfkfds,sdfhdjkfsdf";
            System.out.println("passing address: " + addr);
            String url = "http://concealtracking.com/PdmsTrackapp/Enquiry";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name",name));
            nameValuePairs.add(new BasicNameValuePair("mobile",mob));
            nameValuePairs.add(new BasicNameValuePair("email",mail));
            //nameValuePairs.add(new BasicNameValuePair("message",msg));
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

                if (code == 1) {
                    //session(mail);
                    pd.dismiss();
                    Intent en = new Intent(getApplicationContext(), messagebox.class);
                    startActivity(en);
                    finish();

                    ///


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
}
