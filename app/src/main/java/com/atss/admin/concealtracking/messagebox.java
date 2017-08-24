package com.atss.admin.concealtracking;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

public class messagebox extends AppCompatActivity {
     //EditText msgbox;
    String name,mob,mail,msg;
    Mainclass mclass;
    Button sendmsg;
    TextView lnk;
    RadioGroup rdgr;
    RadioButton rdbt;
    ArrayList<NameValuePair> nameValuePairs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagebox);
        //msgbox=(EditText)findViewById(R.id.msgbox);
        lnk=(TextView)findViewById(R.id.link);
        sendmsg=(Button)findViewById(R.id.msgsub);
        rdgr=(RadioGroup)findViewById(R.id.radioGroup1);
        mclass=(Mainclass) getApplicationContext();
        name=mclass.getName();
        mob=mclass.getMobile();
        mail=mclass.getEmail();
        lnk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://atsspl.com/TrackingSoftware.html"));
                startActivity(browserIntent);
                return false;
            }
        });
        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=rdgr.getCheckedRadioButtonId();
                rdbt=(RadioButton)findViewById(selectedId);
                if(rdbt.getText().toString().equalsIgnoreCase("Interested In it")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://atsspl.com/pricing2.html"));
                    startActivity(browserIntent);
                }
                else if(rdbt.getText().toString().equalsIgnoreCase("For more Info Visit Us")){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://atsspl.com/TrackingSoftware.html"));
                    startActivity(browserIntent);
                }
                else{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://atsspl.com/contact.php"));
                    startActivity(browserIntent);
                }
                /*if() {
                    Toast.makeText(getBaseContext(), "Message Box Should not be empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    //msg = msgbox.getText().toString();
                   // Mynewtask mt = new Mynewtask();
                    //mt.execute(new String[]{""});
                }*/
            }
        });

    }
   /* public class Mynewtask extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(messagebox.this);
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
            String url = "http://pdms.routetrees.com/PdmsTrackapp/Enquiry";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("name",name));
            nameValuePairs.add(new BasicNameValuePair("mobile",mob));
            nameValuePairs.add(new BasicNameValuePair("email",mail));
            nameValuePairs.add(new BasicNameValuePair("message",msg));
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
                    Intent en = new Intent(getApplicationContext(),Home.class);
                    startActivity(en);

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



    }*/
}
