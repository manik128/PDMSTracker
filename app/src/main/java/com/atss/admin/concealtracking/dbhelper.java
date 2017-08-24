package com.atss.admin.concealtracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbhelper extends SQLiteOpenHelper
{

	
   public dbhelper(Context context,String name,CursorFactory factory,int version)
   {
	   super(context, name, factory, version);
   }
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		
		db.execSQL(localdb.CREATE_TABLE1);
		//db.execSQL(localdb.CREATE_TABLE2);
		//db.execSQL(localdb.CREATE_TABLE3);
		System.out.println("table created");
		//db.execSQL(dbadapter.CREATE_TABLE1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		
		Log.w("dbhelper", "Upgrading from version " +oldVersion + " to " +newVersion);
		db.execSQL("DROP TABLE IF EXISTS "+localdb.EMPLOYEES_TABLE);
		//db.execSQL("DROP TABLE IF EXISTS"+dbadapter.Tab_name1);
		onCreate(db);
	}
	
}
