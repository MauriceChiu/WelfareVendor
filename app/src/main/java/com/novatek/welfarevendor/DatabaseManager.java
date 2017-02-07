package com.novatek.welfarevendor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseManager {

	DatabaseHelper mDbHelper = null;
	static SQLiteDatabase mDb = null;
    Context mContext = null; 
    
    public DatabaseManager(Context context) {
        mContext = context;  
        mDbHelper = DatabaseHelper.getInstance(mContext);  
        mDb = mDbHelper.getReadableDatabase(); 
    }
    
    public boolean executeSql(String sql){
        try {
            mDb.execSQL(sql);
            return true;
        }
        catch(SQLiteException e){
           return false;
        }
    }
   
    public Cursor executeSql(String sql, String[]args){
        try {
            return mDb.rawQuery(sql, args);
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
        return null;
    }
	
    public void closeDBhelper() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
}
