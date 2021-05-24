package com.dvops.codedetect.ui;

import android.content.Context;
import android.database.Cursor;


import com.dvops.codedetect.ui.data.SQLiteCRUD;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JavaDoc
 * @author      G. Mizael Mtz Hdz
 * @version     1.0.0   (current version number of program)
 * @since       1.0.0     (the version of the package this class was first added to)
 */
public class Main  {
    SQLiteCRUD sqLiteCRUD;

    public Main(Context context){
        example(context);
    }

    public void example(Context context)
    {
        //CREATE DATABASE: db & TABLE: employee
        int db_version=1;
        String db_name="db";
        String db_sql_create="CREATE TABLE IF NOT EXISTS codebar(\n" +
                "                id INTEGER NOT NULL PRIMARY KEY,\n" +
                "                name VARCHAR(30) NOT NULL,\n" +
                "                date DATE NOT NULL  ); ";
       String db_sql_delete="DROP TABLE IF EXISTS codebar;";
       sqLiteCRUD=new SQLiteCRUD(context,db_version,db_name,db_sql_create,db_sql_delete);

  }
    public void select(){
        //SELECT
      int  count=0;
        Cursor cursor= sqLiteCRUD.select("codebar",new HashMap<String, String>(),null,null);

        if(cursor.moveToFirst())
        {
          do {
              String id = cursor.getString(cursor.getColumnIndex("id"));
              String name = cursor.getString(cursor.getColumnIndex("name"));
              String date = cursor.getString(cursor.getColumnIndex("date"));
              //  String phone = cursor.getString(cursor.getColumnIndex("phone"));

              System.out.println("ID: " + id);
              System.out.println("NAME: " + name);
              System.out.println("date: " + date);
              count++;
          }while(cursor.moveToNext());
            cursor.close();
        }else{
            System.out.println("non");
        }

    }
    public boolean select(String where){
        //SELECT
        Cursor cursor= sqLiteCRUD.select("codebar",new HashMap<String, String>(),where,null);

        if( cursor.getColumnCount()>0 || cursor.moveToFirst())
        {

                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
              //  String phone = cursor.getString(cursor.getColumnIndex("phone"));

                System.out.println("ID: "+id);
                System.out.println("NAME: "+name);
                System.out.println("date: "+date);

                cursor.close();
              return true;
        }else{
            System.out.println("non");
            return false;
        }

    }
    public void insert(String name) {
        Date date = new Date();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("date", String.valueOf(date));

        sqLiteCRUD.insert("codebar",map);

    }
    public void delete(){
        //DELETE
      //  sqLiteCRUD.delete("employee","WHERE idemployee >1");
        sqLiteCRUD.delete("codebar");
    }
    void update(){
        //UPDATE
        Map<String, String> mapUpdate = new HashMap<String, String>();
        mapUpdate.put("phone","+2222222222");
        Map<String, String> mapWhere = new HashMap<String, String>();
        mapWhere.put("idemployee","1");
        sqLiteCRUD.update("employee",mapUpdate,mapWhere);

    }
}