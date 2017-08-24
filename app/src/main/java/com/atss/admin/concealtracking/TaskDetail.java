package com.atss.admin.concealtracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;

public class TaskDetail extends AppCompatActivity {
    TextView dt,frtim,totm,tskdesc,cntper,frmaddr,tvselect,tvname,tvreason,tvdeliver,tvactual,tvreturns,etreason,delitem;
    Mainclass mclass;
   LinearLayout itemlay;
    public ArrayList<Model>  list;
    public ArrayList<String>  list1;
    EditText reason,etactual;
    CheckBox chk1,checkbox;
    Button comp,notd, btnyes, btnno,del,notdone,can,ok,btnreturn,btnok;
    AlertDialog alertDialog,alertDialog2;
    int empid;
    Model item1;
    ProgressBar pd;
    ListView lview;
    private ArrayList<Model> productList;
    String id,desc,date,time,task,totime,person,faddr,des,ename,cnumber,reas,conid;
    ArrayList<NameValuePair> nameValuePairs;;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        sharedpreferences =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dt=(TextView)findViewById(R.id.tskdt);
       // pd=(ProgressBar)findViewById(R.id.progress);
        frtim=(TextView)findViewById(R.id.frtime);
        totm=(TextView)findViewById(R.id.totime);
        tskdesc=(TextView)findViewById(R.id.tskdesc);
        cntper=(TextView)findViewById(R.id.contper);
        frmaddr=(TextView)findViewById(R.id.address);
        comp=(Button)findViewById(R.id.btncomplete);
        del=(Button)findViewById(R.id.btndelay);
        //btnreturn=(Button)findViewById(R.id.btnreturn);
        notdone=(Button)findViewById(R.id.btnnotdone);
        notd=(Button)findViewById(R.id.btnnotdone);
        empid=sharedpreferences.getInt("empid",0);
        ename=sharedpreferences.getString("nm",null);
        id=getIntent().getExtras().getString("tskid");
        desc=getIntent().getExtras().getString("tskdesc");
        date=getIntent().getExtras().getString("dt");
        time=getIntent().getExtras().getString("frmtm");
        cnumber=getIntent().getExtras().getString("ctnum");
        totime=getIntent().getExtras().getString("totm");
        person=getIntent().getExtras().getString("conper");
       conid=getIntent().getExtras().getString("cond");
        faddr=getIntent().getExtras().getString("faddr");
        productList = new ArrayList<Model>();
        list = new ArrayList<Model>();
        list1 = new ArrayList<String>();
//   lview = (ListView) findViewById(R.id.listview);
        mclass=(Mainclass) getApplicationContext();
//        pd.setVisibility(View.VISIBLE);
        //pd.setProgress(0);
//        taskitem mt = new taskitem();
//        mt.execute(new String[]{""});
        getSupportActionBar().setTitle("Task");
        //populateList();
//    btnreturn.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
////        builder.setCancelable(false);
//
//        LayoutInflater layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View vw = null;
////        list=mclass.getAr();
////        list1=mclass.getAr1();
//        vw = getLayoutInflater().inflate(R.layout.popupreturnitem, null);
//
//        itemlay=(LinearLayout)vw.findViewById(R.id.relativeLayout2);
//        Iterator<String> it = list1.iterator();
//        while(it.hasNext()) {
//            for(int i=0;i<list.size();i++){
//            LayoutInflater layoutInflator1 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View vw1 = layoutInflator1.inflate(R.layout.itemlist, null);
//            tvreason = (TextView) vw1.findViewById(R.id.tvreason);
//            tvname = (TextView) vw1.findViewById(R.id.tvactual);
//            tvactual = (TextView) vw1.findViewById(R.id.tvrevised);
//            delitem = (TextView) vw1.findViewById(R.id.tvitem);
//            tvdeliver = (TextView) vw1.findViewById(R.id.tvdeliver);
//            tvreturns = (TextView) vw1.findViewById(R.id.tvreturns);
//            checkbox = (CheckBox) vw1.findViewById(R.id.checkbox);
//            // etactual= (EditText) vw.findViewById(R.id.etactual);
//            etreason = (EditText) vw1.findViewById(R.id.etreason);
//            btnok = (Button) vw1.findViewById(R.id.btnok);
//            tvname.setText(list.get(i).getItems().toString());
//            tvactual.setText(list.get(i).getVqty().toString());
//            tvreturns.setText(list.get(i).getTqty().toString());
//            tvdeliver.setText(list.get(i).getVqty().toString());
//
//
////         delitem.setText(list.get(i).getDitems().toString());
//            itemlay.addView(vw1);
//        }
//        }
////                etdescription= (EditText) vw.findViewById(R.id.etdescription);
////
////                grfont gr= new grfont(ViewdetailsActivity.this);
////                gr.grfonttxtkonbold(tvselect);
////                gr.grfontedkonbold(etdescription);
////                gr.grfontbutkonbold(btnyes);
////                gr.grfontbutkonbold(btnno);
//
//
//
//
////
//
//        builder.setView(vw);
//
//        alertDialog = builder.create();
//        alertDialog.show();
//
//    }
//});

//        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                String sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
//                String product = ((TextView)view.findViewById(R.id.items)).getText().toString();
//                String category = ((TextView)view.findViewById(R.id.qty)).getText().toString();
//              //  String price = ((ImageView)view.findViewById(R.id.ivimage)).setImageResource().toString();
//
////               // Toast.makeText(getApplicationContext(),
////                        "S no : " + sno +"\n"
////                                +"Product : " + product +"\n"
////                                +"Category : " +category +"\n"
////                                +"Price : " +price, Toast.LENGTH_SHORT).show();
//            }
//        });
//        mclass=(Mainclass) getApplicationContext();
        dt.setText(date);
        frtim.setText( time);
        totm.setText(totime);
        tskdesc.setText(desc);
        cntper.setText(person);
        frmaddr.setText(faddr);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
                builder.setCancelable(false);

                LayoutInflater layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                View vw = getLayoutInflater().inflate(R.layout.taskalert, null);
                tvselect= (TextView) vw.findViewById(R.id.tvselect);
                btnyes= (Button)vw.findViewById(R.id. btnyes);
                btnno= (Button)vw.findViewById(R.id. btnno);
//                etdescription= (EditText) vw.findViewById(R.id.etdescription);
//
//                grfont gr= new grfont(ViewdetailsActivity.this);
//                gr.grfonttxtkonbold(tvselect);
//                gr.grfontedkonbold(etdescription);
//                gr.grfontbutkonbold(btnyes);
//                gr.grfontbutkonbold(btnno);
                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }

                });



//

                builder.setView(vw);

                alertDialog = builder.create();
                alertDialog.show();


                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        //voteBuilder.show();
                        Mynewtask mt = new Mynewtask();
                        mt.execute(new String[]{""});
                    }

                });
            }


        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
                builder.setCancelable(false);

                LayoutInflater layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                View vw = getLayoutInflater().inflate(R.layout.taskalert, null);
                tvselect= (TextView) vw.findViewById(R.id.tvselect);
                btnyes= (Button)vw.findViewById(R.id. btnyes);
                btnno= (Button)vw.findViewById(R.id. btnno);
                tvselect.setText("Are You wanted to Send delay message to client ? ");
//                etdescription= (EditText) vw.findViewById(R.id.etdescription);
//
//                grfont gr= new grfont(ViewdetailsActivity.this);
//                gr.grfonttxtkonbold(tvselect);
//                gr.grfontedkonbold(etdescription);
//                gr.grfontbutkonbold(btnyes);
//                gr.grfontbutkonbold(btnno);
                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }

                });



//

                builder.setView(vw);

                alertDialog = builder.create();
                alertDialog.show();


                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        //voteBuilder.show();
                        Senddelay mt = new  Senddelay();
                        mt.execute(new String[]{""});
                    }

                });
            }


        });
        notdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
                builder.setCancelable(false);

                LayoutInflater layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                View vw = getLayoutInflater().inflate(R.layout.popupbrand, null);
                reason= (EditText) vw.findViewById(R.id.edreason);
                can= (Button)vw.findViewById(R.id. btncan);
                ok= (Button)vw.findViewById(R.id. btnok);
                //tvselect.setText("? ");

                can.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }

                });
        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      if(reason.getText().toString().equalsIgnoreCase("")){
                                        Toast.makeText(TaskDetail.this,"You should provide reason",Toast.LENGTH_LONG).show();
                                      }
                                      else {
                                          alertDialog.dismiss();
                                          reas = reason.getText().toString();
                                          Notdonetask mt = new Notdonetask();
                                          mt.execute(new String[]{""});
                                      }
                                  }
                              }

        );


//

                builder.setView(vw);

                alertDialog = builder.create();
                alertDialog.show();


//                btnyes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                        //voteBuilder.show();
//                        Mynewtask mt = new Mynewtask();
//                        mt.execute(new String[]{""});
//                    }
//
//                });
            }


        });

//        chk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                  item1.setSelect(isChecked);
//                productList.add(item1);
//                listviewAdapter adapter = new listviewAdapter(TaskDetail.this, productList);
//                lview.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }
//        });

    }
//    private void populateList() {
//
//        Model item1, item2, item3, item4, item5,item6,item7,item8,item9,item10;
//
//        item1 = new Model("1", "Pen", "10", "");
//        productList.add(item1);
//
//        item2 = new Model("2", "Documents", "2", "");
//        productList.add(item2);
//
//        item3 = new Model("3", "File", "1", "");
//        productList.add(item3);
//
//
//
//
//    }
    public class Mynewtask extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Mynewtask() {
//            this.lat = Double.toString(lat);
//            this.lang = Double.toString(lang);
//            this.addr = addr;
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

            String url = "http://122.166.186.77:8082/ConcealTrackingApp/TaskStatus";
            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("taskid",id));

            nameValuePairs.add(new BasicNameValuePair("status", "Complete"));
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

            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
                int code = json_data.getInt("code");

                if (code == 1) {
                    //session(mail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
                    builder.setCancelable(false);
                    View vw = getLayoutInflater().inflate(R.layout.completetask, null);
                    Button ok=(Button)vw.findViewById(R.id.btnok);
                    builder.setView(vw);
                    alertDialog2 = builder.create();
                    alertDialog2.show();
              ok.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      Intent n1=new Intent(TaskDetail.this,TabHome.class);
                      startActivity(n1);
                      finish();
                  }
              });

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
    public class Senddelay extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Senddelay() {
//            this.lat = Double.toString(lat);
//            this.lang = Double.toString(lang);
//            this.addr = addr;
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

            String url = "http://122.166.186.77:8082/CT/delaymsg/senddelaymsg.php";
            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("bdename",ename));

            nameValuePairs.add(new BasicNameValuePair("cltnum", cnumber));
            nameValuePairs.add(new BasicNameValuePair("cltid", conid));
            nameValuePairs.add(new BasicNameValuePair("deviceid", Integer.toString(empid)));
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
            System.out.println("delay: " + output);
            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
                String code = json_data.getString("status");

                if (code.equalsIgnoreCase("success")) {
                    //session(mail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
                    builder.setCancelable(false);
                    View vw = getLayoutInflater().inflate(R.layout.completetask, null);
                    Button ok=(Button)vw.findViewById(R.id.btnok);
                    TextView tmsg=(TextView)vw.findViewById(R.id.tvservices);
                    tmsg.setText("Delay message Successfully Sent To Client");
                    builder.setView(vw);
                    alertDialog2 = builder.create();
                    alertDialog2.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent n1=new Intent(TaskDetail.this,TabHome.class);
                            startActivity(n1);
                            finish();
                        }
                    });

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
    public class Notdonetask extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;

        public Notdonetask() {
//            this.lat = Double.toString(lat);
//            this.lang = Double.toString(lang);
//            this.addr = addr;
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

            String url = "http://122.166.186.77:8082/ConcealTrackingApp/Tasknotdone";
            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("taskid",id));

            nameValuePairs.add(new BasicNameValuePair("status", "not done"));
            nameValuePairs.add(new BasicNameValuePair("reason", reas));
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

            try {

                JSONObject json_data = new JSONObject(output);
                //json_data.put("us", result);
                //Toast.makeText(getBaseContext(), "Inserted Successfully"+result+json_data,Toast.LENGTH_SHORT).show();
                //json_data.put("code", result);
                int code = json_data.getInt("code");

                if (code == 1) {
                    //session(mail);
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetail.this);
                    builder.setCancelable(false);
                    View vw = getLayoutInflater().inflate(R.layout.completetask, null);
                    Button ok=(Button)vw.findViewById(R.id.btnok);
                    TextView tmsg=(TextView)vw.findViewById(R.id.tvservices);
                    tmsg.setText("Task has been revoked from you and it will be allocated to someone else");
                    builder.setView(vw);
                    alertDialog2 = builder.create();
                    alertDialog2.show();
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent n1=new Intent(TaskDetail.this,TabHome.class);
                            startActivity(n1);
                            finish();
                        }
                    });

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
    public class taskitem extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        //ProgressDialog pd;
        String result = null;

        public taskitem() {
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
            String url = "http://122.166.186.77:8082/ConcealTrackingApp/RetrievItem";
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("taskid",id));
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
            System.out.println("conn:" + output+id);
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
                    item1=new Model();
                    //item1 = new Model(Integer.toString(i), jo.getString("productname"),jo.getString("quantity") , "");
                    item1.setsNo(jo.getString("pid"));
                    item1.setItems(jo.getString("productname"));
                    item1.setQty(jo.getString("quantity"));
                    item1.setsNo(Integer.toString(i+1));
                    item1.setSelect(false);

                    productList.add(item1);



                }
                System.out.print("itemlist"+productList);
                listviewAdapter adapter = new listviewAdapter(TaskDetail.this, productList);
                lview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
