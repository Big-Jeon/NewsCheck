package com.bigjeon.newscheck;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "bigjeon.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Category (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, writeDate TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public ArrayList<Category_Item> getCategory(){
        ArrayList<Category_Item> category_items = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Category ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                Category_Item category_item = new Category_Item();
                category_item.setId(id);
                category_item.setTitle(title);
                category_item.setWriteDate(writeDate);
                category_items.add(category_item);
            }
        }
        cursor.close();

        return category_items;
    }

    public void InsertCategory(String _title, String _writeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO Category (title, writedate) VALUES('" + _title + "', '" + _writeDate + "');");
    }

    public void UpdateCategory(String _title, String _writeDate, String _beforDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE Category SET title = '" + _title + "', writeDate = '" + _writeDate + "' WHERE writeDate = '" + _beforDate + "'");
    }

    public void DeleteCategory(String _beforeTime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Category WHERE writeDate = '" + _beforeTime + "'");
    }
}
