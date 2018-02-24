package com.example.administrator.iteam_fragment.change_head;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class IdDBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "Id";
    private static final int DB_VERSION = 1;

    public IdDBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一个数据库
        db.execSQL("CREATE TABLE ID_PASSWORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"id TEXT, "
                +"password TEXT, "
                +"sex TEXT, "
                +"num TEXT, "
                +"img BLOB);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

}
