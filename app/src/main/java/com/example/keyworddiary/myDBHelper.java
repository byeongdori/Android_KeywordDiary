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
        sqLiteDatabase.execSQL("create table diary (Diaryid integer primary key autoincrement, Userid integer references user, Score integer, Year integer, Month integer, Day integer);");
        sqLiteDatabase.execSQL("create table keyword (Keywordid integer primary key autoincrement, Keyword char(20))");
        sqLiteDatabase.execSQL("create table diarywithkeyword(diarywithkeywordid interger primary key autoincrement, Diaryid integer references diary, Keywordid references keyword)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists user;");
        sqLiteDatabase.execSQL("drop table if exists diary;");
        sqLiteDatabase.execSQL("drop table if exists keyword;");
        sqLiteDatabase.execSQL("drop table if exists diarywithkeyword;");
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

    public void insertUser(Context context, String username, Integer age) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("insert into user (Username, Age) values('"+username+"', '"+age+"')");
        sqlDB.close();
        Toast.makeText(context, "유저 저장 완료!", Toast.LENGTH_LONG).show();
    }

    public String getUser(Context context, String user_name) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select userid, username from user where username='"+user_name+"';", null);
        // 데이터 존재하지 않는 경우
        if (cursor.getCount() == 0) {
            return null;
        }

        String temp = "";
        while (cursor.moveToNext()) {
            temp = cursor.getString(1);
        }
        cursor.close();
        sqlDB.close();
        return temp;
    }

    public void insertKeyword(Context context, String keyword) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        // 먼저 넣으려는 키워드 있는지 체크
        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select Keyword from keyword where Keyword='"+keyword+"';",null);
        // 넣으려는 키워드 없는 경우에만 DB에 넣기!
        if (cursor.getCount() == 0) {
            SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("insert into keyword (Keyword) values ('"+keyword+"')");
            sqlDB.close();
        }
        // 있는 경우

        sqlreadDB.close();
        cursor.close();;
        Toast.makeText(context, "키워드 저장 완료!", Toast.LENGTH_LONG).show();
    }

    public void insertDiary(Context context, String userid, Integer year, Integer month, Integer day) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        // 이미 오늘 생성했는지 체크
        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select diaryid from Diary where Userid='"+userid+"' Year='"+year+"' Month='"+month+"' Day='"+day+"';", null);
        // 넣으려는 다이어리 없는 경우에만 DB에 넣기!
        if (cursor.getCount() == 0) {
            SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("insert into diary (Userid, Year, Month, Day) values ('"+userid+"', '"+year+"', '"+month+"','"+day+"')");
            sqlDB.close();
        }
        // 있는 경우

        sqlreadDB.close();
        cursor.close();;
        Toast.makeText(context, "다이어리 저장 완료!", Toast.LENGTH_LONG).show();
    }

    public void insertDiarywithKeyword(Context context, Integer diaryid, Integer keywordid) {

    }
}
