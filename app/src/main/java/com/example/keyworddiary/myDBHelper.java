package com.example.keyworddiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class myDBHelper extends SQLiteOpenHelper {

    public myDBHelper(Context context) {
        super(context, "MyDiaryDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("TEST", "DB 생성");
        sqLiteDatabase.execSQL("create table user (Userid integer primary key autoincrement, Username char(20), Age integer);");
        sqLiteDatabase.execSQL("create table diary (Diaryid integer primary key autoincrement, Userid integer references user, Keywordid integer references Keyword, Year integer, Month integer, Day integer);");
        sqLiteDatabase.execSQL("create table keyword (Keywordid integer primary key autoincrement, Keyword char(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists user;");
        sqLiteDatabase.execSQL("drop table if exists diary;");
        sqLiteDatabase.execSQL("drop table if exists keyword;");
        onCreate(sqLiteDatabase);
    }

    public void initializeDB(Context context) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        myHelper.onUpgrade(sqlDB, 1, 2);
        sqlDB.close();
        Toast.makeText(context, "Initialized", Toast.LENGTH_LONG).show();
    }

    public void insertDB(Context context) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        //sqlDB.execSQL("insert into user values('Super', 1)");
        //sqlDB.execSQL("insert into diary values('Super', 2021, 11, 27,)");
        sqlDB.close();
        Toast.makeText(context, "Inserted", Toast.LENGTH_LONG).show();
    }

    public void searchDB(Context context) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select * from diary;", null);
        while (cursor.moveToNext()) {
            Toast.makeText(context, cursor.getString(0), Toast.LENGTH_LONG).show();
        }
        cursor.close();
        sqlDB.close();
    }
}
