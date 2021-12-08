package com.example.keyworddiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RenderNode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.keyworddiary.databinding.ActivityMainBinding;
import com.example.keyworddiary.databinding.ActivityResultBinding;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.Random;

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

            int userid = Integer.parseInt(myDB.getUser(this, Username)[0]);

            // 추천 문구 2개 띄우기
            // 1. 최근에 입력한 키워드
            ArrayList<String> recentappearkeyword = myDB.getKeywordinfo_recentappear(this, userid);
            if (recentappearkeyword.size() != 0) {
                Random rand = new Random();
                int randomindex = rand.nextInt(recentappearkeyword.size() - 1);
                binding.userrecommendtext1.setText(" - " + recentappearkeyword.get(randomindex) + "를(을) 최근에 자주 키워드로 입력하셨네요!");
            }

            // 2. 낮은 점수 받은 키워드
            ArrayList<String> lowerscorekeyword = myDB.getKeywordinfo_lowerscore(this, userid);
            if (lowerscorekeyword.size() != 0) {
                Random rand = new Random();
                int randomindex = rand.nextInt(lowerscorekeyword.size() - 1);
                binding.userrecommendtext2.setText(" - " + lowerscorekeyword.get(randomindex) + "를(을) 입력한 날, 점수가 좋지 않았어요 ㅜㅜ");
            }
            // 점수 받아와서 통계바에 세팅
            double[] userScore = new double[3];
            userScore = myDB.getDiaryScore(this, userid);

            binding.Scoreresult.setText("최저 점수 : " + userScore[0] + "   최고점수 : " + userScore[1] + "   평균점수 : " + userScore[2]);
            binding.Scorerange.setEnabled(false);
            binding.Scorerange.setValues((float)userScore[0], (float)userScore[1]);

            // 키워드 분석 결과 세팅
            // 1. 가장 많이 나타난 키워드들
            String mostappearkeyword = myDB.getKeywordinfo_mostappear(this, userid);
            binding.mostappearkeyword.setText(mostappearkeyword);

            // 2. 기분 좋은 날 등장한 키워드들
            ArrayList<String> higherscorekeyword = myDB.getKeywordinfo_higherscore(this, userid);
            // 등장한 키워드 중, 3개 추려서 보여주기
            binding.higherScorekeyword.setText(higherscorekeyword.get(0) + ", " + higherscorekeyword.get(1));

        }
        if (Username == null) {
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