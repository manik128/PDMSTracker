package com.atss.admin.concealtracking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


public class localdb {
	private static final String DATABASE_NAME = "allmsgstore.db";
	 private static final int DATABASE_VERSION = 2;
	 public static final String EMPLOYEES_TABLE = "msgstore";
	 private static final String DATABASE_PATH="/data/data/com.atss.getrepair4/databases/";
	 public static final String TABLE = "orderdetails";
	 public static final String TABLE3 = "productoncart";
	     public static final String msgid = "mid";
		 public static final String Productname = "proname";
		 public static final String recieve= "reciev";
		 public static final String send = "snd";
		 public static final String prc= "price";
		 public static final String img= "image";
		 public static final String nam = "name";
		 public static final String mail = "email";
		 public static final String addr = "address";
		 public static final String phn= "phone";
		 public static final String wgt= "weight";
	
	
	
public static final String CREATE_TABLE1 = "create table "
			   +   EMPLOYEES_TABLE + " (" + msgid
			   + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +  recieve
			   + " TEXT , " +send + " TEXT, " +img + " TEXT)";
/*public static final String CREATE_TABLE2 = "create table "
		   + TABLE + " (" + Productid
		   + " TEXT," + nam
		   + " TEXT, " + mail
		   + " TEXT , " + addr+ " TEXT , " + phn+ " TEXT )";
public static final String CREATE_TABLE3 = "create table "
		   + TABLE3 + " (" + Productid
		   + " TEXT, " + Productname
		   + " TEXT , " + pqty+ " TEXT , " + prc+ " TEXT , " +wgt + " TEXT)";*/
	// static final  String ctabel="CREATE TABLE "+EMPLOYEES_TABLE+" ("+EMP_ID+" integer primary key autoincrement, "+
			// EMP_FNAME+" TEXT, "+EMP_LNAME+" TEXT, "+EMP_AGE+" integer, "+EMP_EMAIL+" TEXT, " +EMP_PH+" integer,"+EMP_PHOTO+"blob)";

	private final Context context;
	private dbhelper dbHelper;
	
	public localdb(Context _context)
	{
		
		context = _context;
		dbHelper = new dbhelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		dbHelper = new dbhelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public  long sendmsg(String msg,String tm)
	{
		long id;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();


		cv.put("snd",msg);

		cv.put("reciev","");
		cv.put("image",tm);
	  //cv.put("image",image);
	   id=  db.insert(EMPLOYEES_TABLE, null, cv);
	   db.close();
		//Log.w("Insert","Parent insert successfully");
		System.out.println("Insert successful");
		 return id;
	}
	public  long sendimg(String msg)
	{
		long id;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();



		cv.put("image",msg);
		cv.put("snd","");
		cv.put("reciev","");
		//cv.put("image",image);
		id=  db.insert(EMPLOYEES_TABLE, null, cv);
		db.close();
		//Log.w("Insert","Parent insert successfully");
		System.out.println("Insert successful");
		return id;
	}


	public  long rcvmsg(String msg,String tm)
	{
		long id;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();


		cv.put("image",tm);
		cv.put("reciev",msg);
		cv.put("snd","");
		//cv.put("image",image);
		id=  db.insert(EMPLOYEES_TABLE, null, cv);
		db.close();
		//Log.w("Insert","Parent insert successfully");
		System.out.println("Insert successful");
		return id;
	}
	public String lastmessage() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<ItemPojo> arrayinfo = new ArrayList<>();
		//String str1="MOBILES AND LAPTOP";
		String last = null;
		String sqlQuery = "SELECT reciev FROM  msgstore ORDER BY mid DESC LIMIT 1";
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		String mid = null;
		if (mCursor != null && mCursor.moveToFirst()) {
			do {

				last= mCursor.getString(0);

			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
				mCursor.close();
				db.close();
			}
		}


		System.out.println("token3" + mid);


		return last;
	}
	public ArrayList<ItemPojo> allmessage() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ArrayList<ItemPojo> arrayinfo = new ArrayList<>();
		//String str1="MOBILES AND LAPTOP";
		String sqlQuery = "select *  from  msgstore ";
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		String mid = null;
		if (mCursor != null && mCursor.moveToFirst()) {
			do {

				mid = mCursor.getString(0);
				String recive = mCursor.getString(1);
				String send = mCursor.getString(2);
				String img = mCursor.getString(3);
				ItemPojo itp = new ItemPojo();
				itp.setMid(mid);
				itp.setRmsg(recive);
				itp.setSmsg(send);
				itp.setImmsg(img);
				arrayinfo.add(itp);
			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
				mCursor.close();
				db.close();
			}
		}


		System.out.println("token3" + mid);


		return arrayinfo;
	}
	public void deletemsg(String mid)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
		Boolean id = false;
		//String str1="MOBILES AND LAPTOP";0.
		db.execSQL("delete from msgstore where mid="+mid+"");
		db.close();
	}
	public int cartsize()
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
		int size = 0;
        //String str1="MOBILES AND LAPTOP";
		String sqlQuery = "select count(*)  from cartpage " ; 
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		if (mCursor != null && mCursor.moveToFirst()) { 
			do {
				
				
				size= mCursor.getInt(0);
				
				
				
	              
			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
	            mCursor.close();
	            db.close();
	        }
		}
		

		
		
		return size;
	}
	public  long orderdetail(String productid,String name,String email,String phone,String address)
	{
		long id;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		 cv.put("proid",productid);
	  cv.put("name",name);
	  cv.put("email",email);
	  cv.put("phone",phone);
	  cv.put("address",address);
	  
	   id=  db.insert(TABLE, null, cv);
	   db.close();
		//Log.w("Insert","Parent insert successfully");
		System.out.println("Insert successful");
		 return id;
	}
	public  long productselect(String productid,String productname,String qty,String price,String weight,String imgurl)
	{
		long id;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		
	  cv.put("proid",productid);
	  cv.put("proname",productname);
	  cv.put("pqty",qty);
	  cv.put("price",price);
	  cv.put("total", price);
	  cv.put("weight",weight);
	  cv.put("image",imgurl);
	   id=  db.insert(EMPLOYEES_TABLE, null, cv);
	   db.close();
		//Log.w("Insert","Parent insert successfully");
		System.out.println("Insert successful");
		 return id;
	}
	public ArrayList<String> reteriveproduct(String prodid)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
        //String str1="MOBILES AND LAPTOP";
		String sqlQuery = "select proname,pqty,price,image  from  cartpage where proid ="+prodid; 
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		if (mCursor != null && mCursor.moveToFirst()) { 
			do {
				
				
				String prdid= mCursor.getString(0);
				String prdnam= mCursor.getString(1);
				String qty= mCursor.getString(2);
				String img=mCursor.getString(3);
				
	              arrayinfo.add(prdid);
	              arrayinfo.add(prdnam);
	              arrayinfo.add(qty);
	              arrayinfo.add(img);
	              
			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
	            mCursor.close();
	            db.close();
	        }
		}

		
		
		return arrayinfo;
	}
	
	public  long updateqty(String productid,String qty,String price)
	{
		long id;
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		
		
	 
	  cv.put("pqty",qty);
	  cv.put("total",price);
	 
	  
	   id=  db.update(EMPLOYEES_TABLE, cv, "proid="+productid, null);
	   db.close();
		//Log.w("Insert","Parent insert successfully");
		System.out.println("Insert successful");
		 return id;
	}
	public ArrayList<String> reteriveforcart(String prodname)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
        //String str1="MOBILES AND LAPTOP";
		String pname="'"+prodname+"'";
		String sqlQuery = "select proid,pqty,price from cartpage where proname ="+pname; 
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		if (mCursor != null && mCursor.moveToFirst()) { 
			do {
				
				
				String prdid= mCursor.getString(0);
				String prdnam= mCursor.getString(1);
				String qty= mCursor.getString(2);
				
				
	              arrayinfo.add(prdid);
	              arrayinfo.add(prdnam);
	              arrayinfo.add(qty);
	             
	              
			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
	            mCursor.close();
	            db.close();
	        }
		}

		
		
		return arrayinfo;
	}
	public ArrayList<String> deletecartproduct(String prodid)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
        //String str1="MOBILES AND LAPTOP";
		String sqlQuery = "delete from  msgstore where proid ="+prodid;
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		if (mCursor != null && mCursor.moveToFirst()) { 
			do {
				
				
				String prdid= mCursor.getString(0);
				String prdnam= mCursor.getString(1);
				String qty= mCursor.getString(2);
				
				
	              arrayinfo.add(prdid);
	              arrayinfo.add(prdnam);
	              arrayinfo.add(qty);
	             
	              
			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
	            mCursor.close();
	            db.close();
	        }
		}

		
		
		return arrayinfo;
	}
	public void  deleteall()
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
		Boolean id = false;
		//String str1="MOBILES AND LAPTOP";0.
		db.execSQL("delete from msgstore");
		db.close();
	}
	public Boolean checkcartproduct(String prodid)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
	Boolean id = false;
        //String str1="MOBILES AND LAPTOP";
		String sqlQuery = "select proname from cartpage"; 
		Cursor mCursor = db.rawQuery(sqlQuery, null);
		//arrayinfo.add("Select Issue");
		if (mCursor != null && mCursor.moveToFirst()) { 
			do {
				
				
				String prdid= mCursor.getString(0);
				
				
				
	              arrayinfo.add(prdid);
	             
	             
	              
			} while (mCursor.moveToNext());
			if (mCursor != null && !mCursor.isClosed()) {
	            mCursor.close();
	            db.close();
	        }
			Iterator< String> tr=arrayinfo.iterator();
			while(tr.hasNext()){
				if(tr.next().toString().equalsIgnoreCase(prodid))
				{
					id=true;
				}
			}
		}

		
		
		return  id;
	}
	public void deletecartitem(String prodid)
	{
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ArrayList<String> arrayinfo = new ArrayList<String>();
	Boolean id = false;
        //String str1="MOBILES AND LAPTOP";0.
		db.execSQL("delete from cartpage where proid="+prodid+"");
		db.close();
	}
	 public boolean isConnectedToServer(int timeout) {
         try{
             URL myUrl = new URL("http://www.google.com");
             URLConnection connection = myUrl.openConnection();
             connection.setConnectTimeout(timeout);
             connection.connect();
             return true;
         } catch (Exception e) {
             return false;
         }
     }
	 public boolean isConnectingToInternet(Context con){
			ConnectivityManager connectivity = (ConnectivityManager)  con.getSystemService(Context.CONNECTIVITY_SERVICE);
			 
			if (connectivity != null) 
			  {
				  NetworkInfo[] info = connectivity.getAllNetworkInfo();
				  if (info != null) 
					  for (int i = 0; i < info.length; i++) 
						  if (info[i].getState() == NetworkInfo.State.CONNECTED)
						  {
							 
								 return true;
							
						  }
				  
			  }
			  return false;
		}
	 public void showAlertDialog(final Context context, String title, String message, Boolean status) {
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();

			// Setting Dialog Title
			alertDialog.setTitle(title);

			// Setting Dialog Message
			alertDialog.setMessage(message);
			
			// Setting alert dialog icon
			//alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

			// Setting OK Buttonf
			alertDialog.setButton("Refresh", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					   
					
					
				}
			});

			// Showing Alert Message
			alertDialog.show();
		}


	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi")

	public boolean checkphn(String nam)
	{
		long id;
		//SQLiteDatabase db=dbHelper.getReadableDatabase();
		//launchRingDialog();
		boolean f=false;
		//String sqlQuery = "select nam,pass from get_repair ";
		//Cursor mCursor = db.rawQuery(sqlQuery, null);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		ArrayList<String> item =new ArrayList<String>();
		try {

			URL url = new URL
					("http://demo2.atplgroups.com/csapp/login.php");
			HttpURLConnection urlConnection =
					(HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			// gets the server json data
			BufferedReader bufferedReader =
					new BufferedReader(new InputStreamReader(
							urlConnection.getInputStream()));
			String next;
			while ((next = bufferedReader.readLine()) != null){
				JSONArray ja = new JSONArray(next);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = ja.getJSONObject(i);

					item.add(jo.getString("phno"));

					//System.out.println("Insert successful"+item);
				}

				//Log.w("Insert","Parent insert successfully");
				System.out.println("Insert successful "+item);

				Log.e("sample", "Insert successful");
			}
		}
		catch(Exception e)
		{
			Log.e("Fail 1", e.toString());

		}

		Iterator<String> tr=item.iterator();

		while(tr.hasNext()){

			if(nam.equalsIgnoreCase(tr.next().toString()))
			{
				f=true;
			}
		}
		return f;
	}



}
