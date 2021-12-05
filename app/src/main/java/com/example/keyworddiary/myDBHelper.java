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
        sqLiteDatabase.execSQL("create table diarywithkeyword(Diarywithkeywordid integer primary key autoincrement, Diaryid integer references diary, Keywordid references keyword)");
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

    public void closeDB(Context context) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        sqlreadDB.close();

        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.close();
    }
    public void insertUser(Context context, String username, Integer age) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
        sqlDB.execSQL("insert into user (Username, Age) values('"+username+"', '"+age+"')");
        //sqlDB.close();
        Toast.makeText(context, "유저 저장 완료!", Toast.LENGTH_LONG).show();
    }

    public String[] getUser(Context context, String user_name) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select userid, username from user where username='"+user_name+"';", null);
        // 데이터 존재하지 않는 경우
        if (cursor.getCount() == 0) {
            return null;
        }

        String[] temp = new String[2];
        while (cursor.moveToNext()) {
            temp[0] = cursor.getString(0);
            temp[1] = cursor.getString(1);
        }
        cursor.close();
        //sqlDB.close();
        return temp;
    }

    public int insertKeyword(Context context, String keyword) {
        int keyword_id = 0;

        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        // 먼저 넣으려는 키워드 있는지 체크
        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select Keywordid from keyword where Keyword='"+keyword+"';",null);
        Log.i("TEST", Integer.toString(cursor.getCount()));
        // 넣으려는 키워드 없는 경우에만 DB에 넣기!
        if (cursor.getCount() == 0) {
            SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("insert into keyword (Keyword) values ('"+keyword+"')");
            //sqlDB.close();
            cursor = sqlreadDB.rawQuery("select Keywordid from keyword where Keyword='"+keyword+"';",null);
        }
        // 있는 경우, id 리턴
        while (cursor.moveToNext()) {
            keyword_id = cursor.getInt(0);
        }

        //sqlreadDB.close();
        cursor.close();
        Toast.makeText(context, "키워드 저장 완료! ", Toast.LENGTH_LONG).show();

        Log.i("TEST", "키워드 id 리턴 : " + keyword_id);
        return keyword_id;
    }

    public int insertDiary(Context context, String userid, Integer score, Integer year, Integer month, Integer day) {
        int diary_id = 0;
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        // 이미 오늘 생성했는지 체크
        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select diaryid from diary where Userid="+userid+" and Score="+score+" and Year="+year+" and Month="+month+" and Day="+day+";", null);
        // 넣으려는 다이어리 없는 경우에만 DB에 넣기!
        if (cursor.getCount() == 0) {
            SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("insert into diary (Userid, Score, Year, Month, Day) values ("+userid+", "+score+", "+year+", "+month+","+day+")");
            //sqlDB.close();
            cursor = sqlreadDB.rawQuery("select diaryid from diary where Userid="+userid+" and Score="+score+" and Year="+year+" and Month="+month+" and Day="+day+";", null);
        }
        // 있는 경우, id 리턴
        while (cursor.moveToNext()) {
            diary_id = cursor.getInt(0);
        }
        //sqlreadDB.close();
        cursor.close();
        Toast.makeText(context, "다이어리 저장 완료!", Toast.LENGTH_LONG).show();

        return diary_id;
    }

    public Double[] getDiaryScore(Context context, Integer userid) {
        Double[] resultscore = new Double[3];

        Double minimumscore = 101.0;
        Double maximumscore = 0.0;
        Double averagescore = 0.0;

        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select Score from Diary where Userid="+userid+";", null);

        Integer num = cursor.getCount();

        while (cursor.moveToNext()) {
            averagescore += cursor.getInt(0);
            if (cursor.getInt(0) < minimumscore) {
                minimumscore = Double.valueOf(cursor.getInt(0));
            }
            if (cursor.getInt(0) > maximumscore) {
                maximumscore = Double.valueOf(cursor.getInt(0));
            }
        }
        if (num != 0) {
            averagescore = averagescore / num;
        }

        return resultscore;
    }

    public void insertDiarywithKeyword(Context context, Integer diaryid, Integer keywordid) {
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        // 먼저 넣으려는 다이어리-키워드 객체 있는지 체크
        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select * from diarywithkeyword where Diaryid="+diaryid+" and Keywordid="+keywordid+";",null);
        // 넣으려는 다이어리-키워드 없는 경우에만 DB에 넣기!
        if (cursor.getCount() == 0) {
            SQLiteDatabase sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("insert into diarywithkeyword (Diaryid, Keywordid) values ("+diaryid+", "+keywordid+")");
            //sqlDB.close();
        }
        // 있는 경우

        //sqlreadDB.close();
        cursor.close();
        Toast.makeText(context, "키워드 저장 완료!", Toast.LENGTH_LONG).show();
    }

    public void getKeywordinfo(Context context, Integer userid) {
        // 키워드 분석 함수
    }
}
