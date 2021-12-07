package com.example.keyworddiary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.example.keyworddiary.databinding.ActivityCalendarBinding;
import com.example.keyworddiary.databinding.ActivityResultBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar_Activity extends AppCompatActivity {

    private ActivityCalendarBinding binding;

    // DB 객체
    public myDBHelper myDB = new myDBHelper(this);

    public String Username = null;

    public int click_year = 0;
    public int click_month = 0;
    public int click_day = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // 유저 이름 받기
        SharedPreferences pref = getSharedPreferences("username", Activity.MODE_PRIVATE);
        Username = pref.getString("username", "");

        long current = System.currentTimeMillis();
        //SimpleDateFormat yeartransFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthtransFormat = new SimpleDateFormat("MM");
        //SimpleDateFormat daytransFormat = new SimpleDateFormat("dd");
        Date currentDate = new Date(current);

        //int current_year = Integer.parseInt(yeartransFormat.format(currentDate));
        click_month = Integer.parseInt(monthtransFormat.format(currentDate));
        //int current_day = Integer.parseInt(daytransFormat.format(currentDate));

        // 캘린더 뷰 관련 함수
        binding.resultCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // 이 날짜를 누르면 아래 프래그먼트에 정보 띄우기
                click_year = i;
                click_month = i1 + 1;
                click_day = i2;
            }
        });

        // 월간, 요일별 통계 띄우기
        int userid = Integer.parseInt(myDB.getUser(this, Username)[0]);
        // 1. 월간 통계
        double[] monthlyScoreresult = myDB.getDiaryScoreDetail(this, userid, 0, click_month, 0);
        binding.Monthlyresult.setText("월간 최저 점수 : "+ monthlyScoreresult[0] + " 월간 평균 점수 : " + monthlyScoreresult[1] + " 월간 최고 점수 : " + monthlyScoreresult[2]);

        // 2. 요일별 통계
        double[] DaysofweekScoreresult = myDB.getDiaryScoreDetail(this, userid, 0, 0, 999);
        binding.Daysofweekresult.setText("월 : " + DaysofweekScoreresult[1] + " 화 : " + DaysofweekScoreresult[2] + " 수 : " + DaysofweekScoreresult[3] +
                " 목 : " + DaysofweekScoreresult[4] + " 금 : " + DaysofweekScoreresult[5] + " 토 : " + DaysofweekScoreresult[6] + " 일 : " + DaysofweekScoreresult[7]);
    }

    // 이전 버튼 눌렀을 때 동작하는 함수, 이전 액티비티 실행하도록 만들기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Result_Activity.class));
        finish();
    }
}