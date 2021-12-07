package com.example.keyworddiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class myDBHelper extends SQLiteOpenHelper {

    public myDBHelper(Context context) {
        super(context, "MyDiaryDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("TEST", "DB 생성");
        sqLiteDatabase.execSQL("create table user (Userid integer primary key autoincrement, Username char(20), Age integer);");
        sqLiteDatabase.execSQL("create table diary (Diaryid integer primary key autoincrement, Userid integer references user, Score integer, Year integer, Month integer, Day integer);");
        sqLiteDatabase.execSQL("create table keyword (Keywordid integer primary key autoincrement, Keyword char(20));");
        sqLiteDatabase.execSQL("create table diarywithkeyword(Diarywithkeywordid integer primary key autoincrement, Diaryid integer references diary, Keywordid references keyword);");
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

    public int insertDiary(Context context, String userid, int score, int year, int month, int day) {
        int diary_id = 0;
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        // 이미 오늘 생성했는지 체크
        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select diaryid from diary where Userid="+userid+" and Year="+year+" and Month="+month+" and Day="+day+";", null);
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

    public double[] getDiaryScore(Context context, int userid) {
        double[] resultscore = new double[3];

        double minimumscore = 101.0;
        double maximumscore = 0.0;
        double averagescore = 0.0;

        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select Score from Diary where Userid="+userid+";", null);

        int num = cursor.getCount();

        while (cursor.moveToNext()) {
            averagescore += cursor.getInt(0);
            if (cursor.getInt(0) < minimumscore) {
                minimumscore = (double) cursor.getInt(0);
            }
            if (cursor.getInt(0) > maximumscore) {
                maximumscore = (double) cursor.getInt(0);
            }
        }
        if (num != 0) {
            averagescore = averagescore / num;
        }

        resultscore[0] = minimumscore;
        resultscore[1] = maximumscore;
        resultscore[2] = averagescore;

        return resultscore;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public double[] getDiaryScoreDetail(Context context, int userid, int year, int month, int day) {
        double[] resultscore = new double[3];
        Arrays.fill(resultscore, 0);
        double[] resultweeklyscore = new double[8];
        Arrays.fill(resultweeklyscore, 0);
        int[] resultweeklycount = new int[8];
        Arrays.fill(resultweeklycount, 0);

        double minimumscore = 101.0;
        double maximumscore = 0.0;
        double averagescore = 0.0;

        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();

        Cursor cursor;
        // 월간 통계
        if (day == 0) {
            cursor = sqlreadDB.rawQuery("select Score from Diary where Userid=" + userid + " and Month=" + month + ";", null);

            int num = cursor.getCount();

            while (cursor.moveToNext()) {
                averagescore += cursor.getInt(0);
                if (cursor.getInt(0) < minimumscore) {
                    minimumscore = (double) cursor.getInt(0);
                }
                if (cursor.getInt(0) > maximumscore) {
                    maximumscore = (double) cursor.getInt(0);
                }
            }
            if (num != 0) {
                averagescore = averagescore / num;
            }

            resultscore[0] = minimumscore;
            resultscore[1] = maximumscore;
            resultscore[2] = averagescore;

            return resultscore;
        }
        // 요일별 통계
        else if (day == 999) {
            cursor = sqlreadDB.rawQuery("select Score, Year, Month, Day from Diary where Userid=" + userid + ";", null);

            while (cursor.moveToNext()) {
                // 요일 알아내는 과정
                int current_year = cursor.getInt(1);
                int current_month = cursor.getInt(2);
                int current_day = cursor.getInt(3);
                LocalDate date = LocalDate.of(current_year, current_month, current_day);
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                int dayOfWeekNumber = dayOfWeek.getValue();

                resultweeklyscore[dayOfWeekNumber] += cursor.getInt(0);
                resultweeklycount[dayOfWeekNumber] += 1;
            }

            for (int i = 0; i < 7; i++) {
                if (resultweeklycount[i] != 0) {
                    resultweeklyscore[i] = resultweeklyscore[i] / resultweeklycount[i];
                }
            }
            return resultweeklyscore;
        }
        return null;
    }

    public void insertDiarywithKeyword(Context context, int diaryid, int keywordid) {
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

    public String getKeywordinfo_mostappear(Context context, int userid) {
        // 가장 빈번한 키워드 분석 함수
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlreadDB.rawQuery("select diaryid from diary where Userid="+userid+";", null);

        ArrayList<Integer> user_diaryids = new ArrayList<>();
        while (cursor.moveToNext()) {
            user_diaryids.add(cursor.getInt(0));
            Log.i("TEST", "diaryids" + cursor.getInt(0));
        }

        // 가장 빈번한 키워드 찾기
        int[] keywordcount = new int[100];
        Arrays.fill(keywordcount, 0);

        for (int i = 0; i < user_diaryids.size(); i++) {
            cursor = sqlreadDB.rawQuery("select keywordid from diarywithkeyword where Diaryid=" + user_diaryids.get(i) + ";", null);
            while (cursor.moveToNext()) {
                keywordcount[cursor.getInt(0)]++;
            }
        }

        int maximumkeywordindex = 0;
        int maximum = 0;
        for (int i = 0; i < 100; i++) {
            if (keywordcount[i] > maximum) {
                maximumkeywordindex = i;
                maximum = keywordcount[i];
            }
        }
        Log.i("TEST", "maximumkeywordindex" + maximumkeywordindex);

        String returnkeyword = "";
        cursor = sqlreadDB.rawQuery("select Keyword from keyword where Keywordid="+maximumkeywordindex+";",null);
        while (cursor.moveToNext()) {
            returnkeyword = cursor.getString(0);
        }
        cursor.close();

        return returnkeyword;
    }

    public String getKeywordinfo_higherscore(Context context, int userid) {
        // 가장 높은 점수 얻은 키워드 분석 함수
        myDBHelper myHelper = new myDBHelper(context);
        SQLiteDatabase sqlreadDB = myHelper.getReadableDatabase();
        return "";
//        Cursor cursor;
//        cursor = sqlreadDB.rawQuery("select diaryid from diary where Userid="+userid+";", null);
//
//        ArrayList<Integer> user_diaryids = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            user_diaryids.add(cursor.getInt(0));
//        }
//
//        // 가장 빈번한 키워드 찾기
//        int[] keywordcount = new int[100];
//        Arrays.fill(keywordcount, 0);
//
//        for (int i = 0; i < user_diaryids.size(); i++) {
//            cursor = sqlreadDB.rawQuery("select keywordid from diarywithkeyword where Diaryid=" + user_diaryids.get(i) + ";", null);
//            while (cursor.moveToNext()) {
//                keywordcount[cursor.getInt(0)]++;
//            }
//        }
//
//        int maximumkeywordindex = 0;
//        int maximum = 0;
//        for (int i = 0; i < 100; i++) {
//            if (keywordcount[i] > maximum) {
//                maximumkeywordindex = i;
//                maximum = keywordcount[i];
//            }
//        }
//
//        String returnkeyword = "";
//        cursor = sqlreadDB.rawQuery("select Keyword from keyword where Keywordid="+maximumkeywordindex+";",null);
//        while (cursor.moveToNext()) {
//            returnkeyword = cursor.getString(0);
//        }
//        cursor.close();
//
//        return returnkeyword;
    }
}
