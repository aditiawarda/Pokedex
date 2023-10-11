package com.gelora.pokemonapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "pokemon";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREAT_TABLE = "CREATE TABLE items (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, url TEXT NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS items");
        onCreate(sqLiteDatabase);
    }

    public ArrayList<HashMap<String, String>> getAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM items";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);
        if(cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("url", cursor.getString(2));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void insert(String name, String url){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO items (name, url) VALUES('"+name+"','"+url+"')";
        database.execSQL(QUERY);
    }

    public void update(int id, String name, String url){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "UPDATE items SET name = '"+name+"', url = '"+url+"' WHERE id = "+id;
        database.execSQL(QUERY);
    }

    public void delete(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "DELETE FROM items WHERE id = "+id;
        database.execSQL(QUERY);
    }

    public void truncateTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName);
        db.close();
    }

    public ArrayList<HashMap<String, String>> search(String name) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * FROM items WHERE name LIKE '%"+name+"%'";
        Cursor cursor = db.rawQuery(QUERY, null);
        if(cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("url", cursor.getString(2));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


}
