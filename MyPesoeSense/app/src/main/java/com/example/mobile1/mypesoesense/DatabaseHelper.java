package com.example.mobile1.mypesoesense;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mobile1 on 7/7/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;
    private Context context;

    private DatabaseHelper(Context context) {
        super(context, "ps_db", null, 5);
    }

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(mContext);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_remittances (rem_id integer primary key, " +
                "rem_date varchar, rem_title varchar, rem_message varchar)");
        populateDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_remittances");
        onCreate(db);
    }

    public void populateDb(SQLiteDatabase db) {
        db.execSQL("INSERT INTO tbl_remittances (rem_id, rem_date, rem_title, rem_message) VALUES (1, '01-01-2015', 'SMART', 'This is a sample content') ");
    }
}
