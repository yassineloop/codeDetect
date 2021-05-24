package com.dvops.codedetect.ui.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.Map;

/**
 * Class SQLiteCRUD: CRUD database 'SQLite'
 * @author      G. Mizael Mtz Hdz
 * @version     1.0.0
 * @since       1.0.0
 */
public class SQLiteCRUD extends SQLiteConnection
{
    private SQLiteDatabase db_connection;
    /**
     * Function: SQLiteCRUD  Constructor
     * @param context
     * @param db_version
     * @param db_name
     * @param db_sql_create
     * @param db_sql_delete
     * @return void
     */
    public SQLiteCRUD(Context context, int db_version, String db_name, String db_sql_create, String db_sql_delete)
    {
        super(context, db_version, db_name, db_sql_create, db_sql_delete);
    }
    /**
     * Function:  select (CRUD)
     * @param table
     * @param map
     * @param where
     * @param limit
     * @return Cursor
     */
    @SuppressLint("LongLogTag")
    public Cursor select(String table, Map<String, String> map, String where, String limit)
    {
        StringBuilder query=new StringBuilder();
        query.append("SELECT * FROM "+table);
        if(map!=null)
        {
            if(map.size()>0)
            {
                query.append(" WHERE ");
                query.append("");
                int count=0;
                for(String key: map.keySet())
                {
                    if(count==0)
                        query.append(""+key+"='"+map.get(key)+"'");
                    else
                        query.append(" AND "+key+"='"+map.get(key)+"'");
                    count++;
                }
            }
        }
        else
        {
            if(where!=null)
                query.append(" "+where);
        }
        if(limit!=null)
            query.append(" "+limit);
        Log.i("[SQLiteCRUD] Query SELECT: ",query.toString());
        this.db_connection=this.getReadableDatabase();
        return this.db_connection.rawQuery(query.toString(),null);
    }
    /**
     * Función insert  (CRUD)
     * @param table
     * @param map
     * @return Boolean
     */
    @SuppressLint("LongLogTag")
    public boolean insert(String table, Map<String, String> map)
    {
        boolean result=false;
        if(map.size()>0)
        {
            StringBuilder query=new StringBuilder();
            StringBuilder valores=new StringBuilder();

            query.append("INSERT INTO "+table+"(");
            valores.append(" VALUES(");
            int count=0;
            for(String key: map.keySet())
            {
                if(count==map.size()-1)
                {
                    query.append("" + key + ")");
                    valores.append("'"+map.get(key)+"')");
                }
                else
                {
                    query.append("" + key + ",");
                    valores.append("'"+map.get(key)+"',");
                }
                count++;
            }
            query.append(valores.toString());
            Log.i("[SQLiteCRUD] Query INSERT: ",query.toString());
            this.db_connection=this.getWritableDatabase();
            this.db_connection.execSQL(query.toString());
            result=true;
        }
        return result;
    }
    /**
     * Función update  (CRUD)
     * @param table
     * @param map
     * @param where
     * @return Boolean
     */
    @SuppressLint("LongLogTag")
    public boolean update(String table, Map<String, String> map, Map<String, String> where)
    {
        boolean result=false;
        if(map!=null)
        {
            if(map.size()>0)
            {
                StringBuilder query=new StringBuilder();
                query.append("UPDATE "+table+" SET ");
                int count=0;
                for(String key: map.keySet())
                {
                    if(count==0)
                        query.append(""+key+"='"+map.get(key)+"'");
                    else
                        query.append(", "+key+"='"+map.get(key)+"'");
                    count++;
                }
                if(where!=null)
                {
                    if(where.size()>0)
                    {
                        count=0;
                        boolean is_number=false;
                        for(String key: where.keySet())
                        {
                            is_number=false;
                            if(count==0)
                                query.append(" WHERE ");
                            else
                                query.append(" AND ");
                            try
                            {
                                Integer.parseInt(where.get(key));
                                is_number=true;
                            }catch(NumberFormatException e)
                            {
                                is_number=false;
                            } catch(NullPointerException e)
                            {
                                is_number=false;
                            }
                            if(is_number)
                                query.append(key+"="+where.get(key)+"");
                            else
                                query.append(key+"='"+where.get(key)+"'");

                            count++;
                        }
                    }
                }
                Log.i("[SQLiteCRUD] Query UPDATE: ",query.toString());
                this.db_connection=this.getWritableDatabase();
                this.db_connection.execSQL(query.toString());
                result=true;
            }
        }
        return result;
    }
    /**
     * Función delete  (CRUD)
     * @param table
     * @param map (where)
     * @return Boolean
     */
    @SuppressLint("LongLogTag")
    public boolean delete(String table, Map<String, String> map)
    {
        boolean result=true;
        StringBuilder query=new StringBuilder();
        query.append("DELETE FROM "+table);
        if(map!=null)
        {
            int count=0;
            for(String key: map.keySet())
            {
                if(count==0)
                    query.append(" WHERE "+key+"='"+map.get(key)+"'");
                else
                    query.append(" AND "+key+"='"+map.get(key)+"'");
                count++;
            }
        }
        Log.i("[SQLiteCRUD] Query DELETE: ",query.toString());
        this.db_connection=this.getWritableDatabase();
        this.db_connection.execSQL(query.toString());
        return result;
    }
    /**
     * Función delete  (CRUD)
     * @param table
     * @param where
     * @return Boolean
     */
    @SuppressLint("LongLogTag")
    public boolean delete(String table, String where)
    {
        boolean result=true;
        StringBuilder query=new StringBuilder();
        query.append("DELETE FROM "+table);
        if(where!=null)
            query.append(" "+where);
        Log.i("[SQLiteCRUD] Query DELETE: ",query.toString());
        this.db_connection=this.getWritableDatabase();
        this.db_connection.execSQL(query.toString());
        return result;
    }
    /**
     * Función delete  (CRUD)
     * @param table
     * @return Boolean
     */
    @SuppressLint("LongLogTag")
    public boolean delete(String table)
    {
        boolean result=true;
        StringBuilder query=new StringBuilder();
        query.append("DELETE FROM "+table);
        Log.i("[SQLiteCRUD] Query DELETE: ",query.toString());
        this.db_connection=this.getWritableDatabase();
        this.db_connection.execSQL(query.toString());
        return result;
    }
}