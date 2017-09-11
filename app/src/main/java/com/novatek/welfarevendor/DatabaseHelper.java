package com.novatek.welfarevendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper  extends SQLiteOpenHelper {
	
	private static DatabaseHelper mInstance = null;
    // Update Database Version number before release APK
    private static final int DATABASE_VERSION = 5;
    private static final String SP_KEY_DB_VER = "db_ver";
    private final Context mContext;
 
    public DatabaseHelper(Context context) {
        super(context, ValDef.DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        initialize();
    }
    
    public DatabaseHelper(Context context, String name, int version) {
 	    super(context, name, null, version);
        mContext = context;
        initialize();
    }
    
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    /**
     * Initializes database. Creates database if doesn't exist.
     */
    private void initialize() {
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DATABASE_VERSION != dbVersion) {
                File dbFile = mContext.getDatabasePath(ValDef.DATABASE_NAME);
                if (!dbFile.delete()) {
                    Log.d(ValDef.Tag, "Unable to update database");
                }
            }
        }
        if (!databaseExists()) {
            createDatabase();
        }
    }

    /**
        * Returns true if database file exists, false otherwise.
        *  @return
        */
    private boolean databaseExists() {
        File dbFile = mContext.getDatabasePath(ValDef.DATABASE_NAME);
        return dbFile.exists();
    }

    /**
        * Creates database by copying it from assets directory.
        */
    private void createDatabase()
    {
        String OutFileName = ValDef.DATABASE_PATH + ValDef.DATABASE_NAME;

        File file = new File(ValDef.DATABASE_PATH);
        if (!file.mkdirs()) {
            file.mkdirs();
        }

        // 數據庫已經存在，無需複製
        if (new File(OutFileName).exists()) {
            Log.d(ValDef.Tag, "資料庫已存在");
            return;
        }

        InputStream is = null;
        OutputStream os = null;

        try {
            is = mContext.getAssets().open(ValDef.DATABASE_NAME);
            os = new FileOutputStream(OutFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(ValDef.Tag, "資料庫已複製");
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	//�R���ƾڮw
    public boolean deleteDatabase(Context context , String databaseName) {
        return context.deleteDatabase(databaseName);
    }

}
