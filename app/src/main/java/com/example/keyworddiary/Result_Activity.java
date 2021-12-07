package com.example.keyworddiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.keyworddiary.databinding.ActivityMainBinding;
import com.example.keyworddiary.databinding.ActivityResultBinding;
import com.google.android.material.slider.RangeSlider;

public class Result_Activity extends AppCompatActivity {

    private ActivityResultBinding binding;

    // DB 객체
    public myDBHelper myDB = new myDBHelper(this);

    public String Username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences pref = getSharedPreferences("username", Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("username"))) {
            Username = pref.getString("username", "");
            binding.userinfoName.setText(Username);

            // 추천 문구 2개 띄우기


            // 점수 받아와서 통계바에 세팅
            double[] userScore = new double[3];
            int userid = Integer.parseInt(myDB.getUser(this, Username)[0]);
            userScore = myDB.getDiaryScore(this, userid);

            binding.Scoreresult.setText("최저 점수 : " + userScore[0] + " 최고점수 : " + userScore[1] + " 평균점수 : " + userScore[2]);
            binding.Scorerange.setEnabled(false);
            binding.Scorerange.setValues((float)userScore[0], (float)userScore[1]);

            // 키워드 분석 결과 세팅
            // 1. 가장 많이 나타난 키워드
            Log.i("TEST", "Mostappear" + userid);
            String mostappearkeyword = myDB.getKeywordinfo_mostappear(this, userid);
            binding.mostappearkeyword.setText(mostappearkeyword);

            // 2. 기분 좋은 날 등장한 키워드

            // 3. 주말에만 등장한 키워드드

        }        if (Username == null) {
            binding.userinfoName.setText("유저 이름이 설정 되어있지 않아요!");
        }
    }

    // 이전 버튼 눌렀을 때 동작하는 함수, 이전 액티비티 실행하도록 만들기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    // 달력 통계로 넘어가는 버튼
    public void tocalendaractivity(View view) {

        startActivity(new Intent(this,Calendar_Activity.class));
        finish();
    }
}