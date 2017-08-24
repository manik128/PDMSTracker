package com.atss.admin.concealtracking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Set;

/**
 * Created by Admin on 27-06-2017.
 */

public class listviewAdapter extends BaseAdapter {
    public ArrayList<Model> productList;
    public ArrayList<Model>  remlist;
    public ArrayList<String>  newlist;
    Activity activity;
TextView  tvreason, tvactual,tvreturns,tvvalid;
    EditText  etactual, etreason,etrea;
    CheckBox checkbox;
    Button btnok;
    Model ret;
    Mainclass mclass;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String pid,rquant,tquant;
    AlertDialog alertDialog;
    ArrayList<NameValuePair> nameValuePairs;;
    public listviewAdapter(Activity activity, ArrayList<Model> productList) {
        super();
        this.activity = activity;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mSNo;
        TextView mitems;
        TextView mqty;
        ImageView mimage;
        ImageView rimage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        final LayoutInflater inflater = activity.getLayoutInflater();
        sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        remlist=new ArrayList<Model>();
        newlist=new ArrayList<String>();
        mclass=(Mainclass) activity.getApplicationContext();
        ret=new Model();
      int i=0;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.mSNo = (TextView) convertView.findViewById(R.id.sNo);
            holder.mitems = (TextView) convertView.findViewById(R.id.items);
            holder.mqty = (TextView) convertView
                    .findViewById(R.id.qty);
            holder.mimage = (ImageView) convertView.findViewById(R.id.ivimage);
            holder.rimage = (ImageView) convertView.findViewById(R.id.ivdelete);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Model item = productList.get(position);
        holder.mSNo.setText(Integer.toString(i));
        holder.mitems.setText(item.getItems().toString());
        holder.mqty.setText(item.getQty().toString());
        i++;
        holder.mimage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
//        builder.setCancelable(false);


        View vw = inflater.inflate(R.layout.popuplistitems, null);
        tvreason= (TextView) vw.findViewById(R.id.tvreason);
        tvactual= (TextView) vw.findViewById(R.id.tvactual);
        tvreason= (TextView) vw.findViewById(R.id.tvreason);
        tvreturns= (TextView) vw.findViewById(R.id.tvreturns);
        tvvalid= (TextView) vw.findViewById(R.id.validmsg);
        checkbox= (CheckBox) vw.findViewById(R.id.checkbox);
        etactual= (EditText) vw.findViewById(R.id.etactual);
        etreason= (EditText) vw.findViewById(R.id.etreason);
        btnok= (Button)vw.findViewById(R.id.btnok);
           tvreason.setText(item.getItems().toString());
           tvactual.setText(item.getQty().toString());

        etactual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etactual.getText().toString().equalsIgnoreCase("")){

                }
                else{
                int qty=Integer.parseInt(tvactual.getText().toString())-Integer.parseInt(etactual.getText().toString());
                tvreturns.setText(Integer.toString(qty));



                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etactual.getText().toString().equalsIgnoreCase("")){

                }
                        else if(Integer.parseInt(etactual.getText().toString())>Integer.parseInt(tvactual.getText().toString())){
                            etactual.setText("0");
                            tvreturns.setText("0");
                        }
            }
        });
//                etdescription= (EditText) vw.findViewById(R.id.etdescription);
//
//                grfont gr= new grfont(ViewdetailsActivity.this);
//                gr.grfonttxtkonbold(tvselect);
//                gr.grfontedkonbold(etdescription);
//                gr.grfontbutkonbold(btnyes);
//                gr.grfontbutkonbold(btnno);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              if( etactual.getText().toString().equalsIgnoreCase("")){
                  //Toast.makeText(activity, "Revised qty should not blank ", Toast.LENGTH_SHORT).show();
                  tvvalid.setText("Revised qty should not blank");
              }
              else if(etactual.getText().toString().equalsIgnoreCase("0")){

              }
              else if(etreason.getText().toString().equalsIgnoreCase(" ")){
                  tvvalid.setText("Reason field should not blank");
              }
                else{
                  pid=holder.mSNo.getText().toString();
                  rquant=etactual.getText().toString();
                  tquant=tvreturns.getText().toString();
                //ret=remlist.get(position);
                //ret = productList.get(position);
                  ret.setItems(holder.mitems.getText().toString());
                  ret.setVqty(rquant);
                  ret.setTqty(tquant);
                  ret.setDitems("");
                  ret.setDelqty("");
                  remlist=productList;
                  //remlist.set(position,ret);
                  //remlist.add(ret);
//                   newlist.add(holder.mitems.getText().toString());
//                   newlist.add(rquant);
//                  newlist.add(tquant);
                 // System.out.print("values:"+remlist.toString());

//                  newlist.add("new1");
//                  newlist.add("new2");

                  mclass.setAr(remlist);
//                  Set<String> hs = sharedpreferences.getStringSet("set", remlist);
//                  SharedPreferences.Editor editor =  sharedpreferences.edit();
//                  editor.putString("plsit",remlist);
//
//                  editor.commit();
                  //remlist.add(ret);
                  item.setQty(rquant);
                  productList.set(position,item);
                notifyDataSetChanged();
                  //item.setTqty(tquant);
                  //holder.mqty.setText(rquant);
//                  updatetaskitem mt = new updatetaskitem(holder);
//                  mt.execute(new String[]{""});
                  alertDialog.dismiss();
              }
                //holder.mqty.setText(etactual.getText().toString());


            }

        });


//

        builder.setView(vw);

        alertDialog = builder.create();
        alertDialog.show();

    }
});

        holder.rimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
//        builder.setCancelable(false);


                View vw = inflater.inflate(R.layout.popupdelete, null);
//              tvreason= (TextView) vw.findViewById(R.id.tvreason);
//                tvactual= (TextView) vw.findViewById(R.id.tvactual);
//                tvreason= (TextView) vw.findViewById(R.id.tvreason);
//                tvreturns= (TextView) vw.findViewById(R.id.tvreturns);
//                checkbox= (CheckBox) vw.findViewById(R.id.checkbox);
//                etactual= (EditText) vw.findViewById(R.id.etactual);
               etrea= (EditText) vw.findViewById(R.id.edreas);
               btnok= (Button)vw.findViewById(R.id. btncan);

//                etdescription= (EditText) vw.findViewById(R.id.etdescription);
//
//                grfont gr= new grfont(ViewdetailsActivity.this);
//                gr.grfonttxtkonbold(tvselect);
//                gr.grfontedkonbold(etdescription);
//                gr.grfontbutkonbold(btnyes);
//                gr.grfontbutkonbold(btnno);

                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etrea.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(activity,"You should provide reason",Toast.LENGTH_LONG).show();


                        }
                        else{
                            ret.setItems("");
                            ret.setVqty("");
                            ret.setTqty("");
                            ret.setDitems(holder.mitems.getText().toString());
                            ret.setDelqty(rquant);
                            productList.remove(position);
                            notifyDataSetChanged();

                            //notifyItemRemoved(position);
                           // notifyItemRangeChanged(position, productList.size());
                            Toast.makeText(activity,"You should provide reason",Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }
                        //

                    }

                });


//

                builder.setView(vw);

                alertDialog = builder.create();
                alertDialog.show();

            }
        });

        //holder.mimage.setText(item.getSelect().toString());
        //holder.mselect.setChecked(item.getSelect());

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        return convertView;
    }
    public class updatetaskitem extends AsyncTask<String, Void, String> {
        String lat, lang, addr;
        Context con;
        ProgressDialog pd;
        String result = null;
        ViewHolder hold;
        public updatetaskitem(ViewHolder hldr) {
          hold=hldr;
// this.lat = Double.toString(lat);
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

            String url = "http://concealtracking.com/PdmsTrackapp/updatetaskitem";
            nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("pid",pid));

            nameValuePairs.add(new BasicNameValuePair("revqty", rquant));
            nameValuePairs.add(new BasicNameValuePair("retqty", tquant));
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

                  hold.mqty.setText(rquant);
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
