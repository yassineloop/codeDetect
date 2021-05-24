package com.dvops.codedetect.ui.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class SQLiteConnection: connection 'SQLite'
 * @author      G. Mizael Mtz Hdz
 * @version     1.0.0
 * @since       1.0.0
 */
public class SQLiteConnection extends SQLiteOpenHelper
{
    private String db_name;
    private int db_version;
    private String db_sql_create;
    private String db_sql_delete;

    /**
     * Función SQLiteConnection  Constructor
     * @param context
     * @param db_version
     * @param db_name
     * @param db_sql_create
     * @param db_sql_delete
     * @return void
     */
    public SQLiteConnection(Context context, int db_version, String db_name, String db_sql_create, String db_sql_delete)
    {
        super(context,db_name,null,db_version);
        this.db_name=db_name;
        this.db_version=db_version;
        this.db_sql_create=db_sql_create;
        this.db_sql_delete=db_sql_delete;
    }
    /**
     * Function onCreate
     * @param db
     * @return void
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i("[SQLiteCRUD] this.sql: ",this.db_sql_create);
        db.execSQL(this.db_sql_create);
    }
    /**
     * Function onUpgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     * @return void
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(this.db_sql_delete); //DELETE OLD TABLE(s)
        db.execSQL(this.db_sql_create); //CREATE NEW TABLE(s)
    }
    /**
     * Function deleteDatabase
     * @param context
     * @return void
     */
    public boolean deleteDatabase(Context context)
    {
        return context.deleteDatabase(this.db_name);
    }
}
