package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**

 */
public class Chat extends Fragment {
    LayoutInflater mInflater;
    Button sendmsg;
    localdb ldb;
    Timer t;
    View mview,mview1;
    ArrayList<NameValuePair> nameValuePairs;;
    TextView tx1,back;
    String mesge,loc,s1,fmsg,phno,lastmsg,tokenmsg,newmessage;
    //ArrayList<ItemPojo> allmsg;
    int emp,msgc;
    LinearLayout msgbox,lattach,menulayout;
    ArrayList<ItemPojo> allmsg;
    EditText message;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    ScrollView scroller;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public Chat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            mInflater = LayoutInflater.from(getActivity());
            sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            final View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            sendmsg=(Button)rootView.findViewById(R.id.sendmsg);
            phno=sharedpreferences.getString("phno", null);
        tokenmsg=sharedpreferences.getString("regId", null);
        newmessage=sharedpreferences.getString("newmsg", null);
        emp=sharedpreferences.getInt("empid",0);

            lattach=(LinearLayout)rootView.findViewById(R.id.attachlay);
            scroller=(ScrollView)rootView.findViewById(R.id.scrlbr);
        message=(EditText)rootView.findViewById(R.id.tbox);
        msgbox=(LinearLayout)rootView.findViewById(R.id.messagebox);
        final int[] location = new int[2];
        ldb=new localdb(getActivity());
        allmsg=new ArrayList<ItemPojo>();
        allmsg=ldb.allmessage();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        scroller.post(new Runnable() {

            @Override
            public void run() {
                scroller.fullScroll(ScrollView.FOCUS_DOWN);

            }

        });

        //hideSoftKeyboard();
        operation();


        if(newmessage!=null){
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm a");
            final String strDate1 = sdf1.format(c1.getTime());
            ldb.rcvmsg(newmessage,strDate1);
            mview1 = mInflater.inflate(R.layout.recievemsg, null);
            TextView tx3 = (TextView) mview1.findViewById(R.id.msg2);
            TextView tx4 = (TextView) mview1.findViewById(R.id.msgt2);

            tx4.setText(strDate1);
            //tx3.setMovementMethod(new ScrollingMovementMethod());
            tx3.setText(newmessage);
            msgbox.addView(mview1);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.remove("newmsg");


            editor.commit();
        }
        //message.setText(scurrmsg);
        //message.setText("");
        //scroller.smoothScrollTo(0, message.));
        //msgbox.addView(mview);
        sendmsg.setTextColor(Color.parseColor("#5DFFFFFF"));

        sendmsg.setEnabled(false);

        if(getActivity().getIntent().getExtras()!=null){
            mesge=getActivity().getIntent().getExtras().getString("token");
            Toast.makeText(getActivity(),"msge come"+mesge,Toast.LENGTH_LONG).show();
        }
        //getActivity().getActionBar().setTitle("WEB CHAT");
        scroller.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                scroller.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 10);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                sendmsg.setTextColor(Color.parseColor("#FFFFFF"));
                sendmsg.setEnabled(true);
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
        // Inflate the layout for this fragment
        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().trim().equals("")){
                    sendmsg.setTextColor(Color.parseColor("#5DFFFFFF"));
                    sendmsg.setEnabled(false);
                }
                else {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm a");
                    final String strDate = sdf.format(c.getTime());
                    ldb.sendmsg(message.getText().toString().trim(),strDate);

                    //sendmsg.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    //sendmsg.setTextColor(Color.parseColor("#6F0708"));
                    //ldb.sendmsg(message.getText().toString().trim());
                    Toast.makeText(getActivity(),"msge come"+tokenmsg,Toast.LENGTH_LONG).show();

                    /*mview1 = mInflater.inflate(R.layout.recievemsg, null);
                    TextView tx3 = (TextView) mview1.findViewById(R.id.msg2);
                    TextView tx4 = (TextView) mview1.findViewById(R.id.msgt2);
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat(" HH:mm a");
                    final String strDate1 = sdf1.format(c1.getTime());
                    tx4.setText(strDate1);
                    //tx3.setMovementMethod(new ScrollingMovementMethod());
                    tx3.setText("manik");
                    msgbox.addView(mview1);*/
                   mview = inflater.inflate(R.layout.msgbox, null);
                    tx1 = (TextView) mview.findViewById(R.id.msg);
                    TextView tx2 = (TextView) mview.findViewById(R.id.msgt);

                    tx1.setText(message.getText().toString().trim());
                    tx2.setText(strDate);
                    //insert();
                    Mynewtask mt = new Mynewtask();
                    mt.execute(new String[]{""});
                    fmsg=message.getText().toString().trim();
                    message.setText("");
                    //message.setText("");
                    //scroller.smoothScrollTo(0, message.));


                    sendmsg.setTextColor(Color.parseColor("#5DFFFFFF"));
                    sendmsg.setEnabled(false);

                    msgbox.addView(mview);
                    scroller.post(new Runnable() {

                        @Override
                        public void run() {
                            scroller.fullScroll(ScrollView.FOCUS_DOWN);

                        }

                    });

                    //ldb.rcvmsg("");
                }
            }
        });
        return rootView;
    }
    View.OnKeyListener keyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                //do something here
                scroller.post(new Runnable() {

                    @Override
                    public void run() {
                        scroller.fullScroll(ScrollView.FOCUS_DOWN);

                    }

                });

            }
            return false;
        }
    };
    public void  operation() {
        if (allmsg.size() != 0) {
            //do here
            // mesge = getIntent().getExtras().getString("msg");


            // message.setText("");
            //scroller.smoothScrollTo(0, message.));

            for (int i = 0; i < allmsg.size(); i++) {
                String rcurrmsg = allmsg.get(i).getRmsg();
                String scurrmsg = allmsg.get(i).getSmsg();
                final String tm = allmsg.get(i).getImmsg();
                //Toast.makeText(WebChat.this,"Sucess"+imgpt,Toast.LENGTH_LONG).show();
                mview = mInflater.inflate(R.layout.recievemsg, null);
                TextView tx3 = (TextView) mview.findViewById(R.id.msg2);
                TextView tx4 = (TextView) mview.findViewById(R.id.msgt2);
                Calendar c1 = Calendar.getInstance();
                SimpleDateFormat sdf1 = new SimpleDateFormat(" HH:mm a");
                final String strDate1 = sdf1.format(c1.getTime());
                final int i3 = i;

                // tx3.setText(mesge);
                if (rcurrmsg.equals("")) {
                    mview = mInflater.inflate(R.layout.msgbox, null);
                    tx1 = (TextView) mview.findViewById(R.id.msg);
                    TextView tx2 = (TextView) mview.findViewById(R.id.msgt);
                    final LinearLayout chatc = (LinearLayout) mview.findViewById(R.id.chatcon);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat(" HH:mm a");
                    final String strDate = sdf.format(c.getTime());
                    tx1.setText(scurrmsg);
                    tx2.setText(tm);
                    //message.setText(scurrmsg);
                    //message.setText("");
                    //scroller.smoothScrollTo(0, message.));
                    msgbox.addView(mview);

                }
                else{
                tx4.setText(tm);
                //tx3.setMovementMethod(new ScrollingMovementMethod());
                tx3.setText(rcurrmsg);
                msgbox.addView(mview);}
            }

        }
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
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/SendMessage";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(emp)));


            nameValuePairs.add(new BasicNameValuePair("message", fmsg));

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

}
