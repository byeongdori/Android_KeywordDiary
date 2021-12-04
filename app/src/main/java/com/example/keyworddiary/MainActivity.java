package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.keyworddiary.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // DB 객체
    public myDBHelper myDB = new myDBHelper(this);

    // 유저 이름 저장하는 변수
    public String Username = null;

    public int currentscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        // 앱 실행시 이전 정보 불러오기
        SharedPreferences pref = getSharedPreferences("SharedResource", Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("username"))) {
            Username = pref.getString("username", "");
            binding.Greetingtext.setText(Username + "님 반가워요! :)");
        }
        if (Username == null) {
            binding.Greetingtext.setText("유저 이름이 설정 되어있지 않아요!");
            binding.SetUsernameText.setText("설정 창에 가서 유저 이름을 설정해주세요!");
        }

        binding.todayScore.setProgress(0);
        // Seekbar에 리스너 등록, 점수대 별로 변화하는 텍스트 설정하기
        binding.todayScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentscore = seekBar.getProgress();
                binding.todayScoredisplay.setText(Integer.toString(currentscore) + "점");
                if (currentscore > 75) {
                    binding.todayScoretext.setText("최고의 하루를 보내셨네요!");
                } else if (currentscore < 30) {
                    binding.todayScoretext.setText("내일은 더 좋은 하루가 찾아올꺼에요 :)");
                } else {
                    binding.todayScoretext.setText("평범한 하루였어요");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                currentscore = seekBar.getProgress();
                Log.i("TEST", Integer.toString(currentscore));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentscore = seekBar.getProgress();
                Log.i("TEST", Integer.toString(currentscore));
            }
        });
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        // 앱 종료 시 정보 저장
        if (Username != null) {
            SharedPreferences pref = getSharedPreferences("username", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", Username);   // Username 저장
            //editor.commit();
            editor.apply();
        }
        super.onDestroy();
    }

    // 사용자가 정보 다 입력한 후, 다음 화면으로 가는 버튼 눌렀을 때 동작하는 함수
    public void mainactivityResult(View view) {

        // 사용자가 입력한 키워드 변수에 저장하는 작업
        String works = binding.todayWork.getText().toString();
        String feelings = binding.todayFeeling.getText().toString();
        ArrayList<String> todayworks = new ArrayList<>(Arrays.asList(works.replaceAll(" ","").split(",")));
        ArrayList<String> todayfeelings = new ArrayList<>(Arrays.asList(feelings.replaceAll(" ","").split(",")));

        // 변수 활용하기, DB에 저장하고 다음으로 넘어가나? 여기가 처리할 부분이 많겠네, 결과 페이지로 넘어갈 때 로딩?

        // 유저가 입력한 키워드 DB에 저장, 생성된 KeyWord ID도 받아와야 할듯?
        for (int i = 0; i < todayworks.size(); i++) {
            myDB.insertKeyword(this, todayworks.get(i));
        }
        for (int i = 0; i < todayfeelings.size(); i++) {
            myDB.insertKeyword(this, todayfeelings.get(i));
        }

        // 최종 다이어리 객체 DB에 생성
        // 1. 다이어리 객체 생성 위해, 현재 시간 받아오기
        long current = System.currentTimeMillis();
        SimpleDateFormat yeartransFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthtransFormat = new SimpleDateFormat("MM");
        SimpleDateFormat daytransFormat = new SimpleDateFormat("dd");
        Date currentDate = new Date(current);

        int current_year = Integer.parseInt(yeartransFormat.format(currentDate));
        int current_month = Integer.parseInt(monthtransFormat.format(currentDate));
        int current_day = Integer.parseInt(daytransFormat.format(currentDate));

        // 2. DB에 다이어리 객체 생성
        myDB.insertDiary(this, userid, current_year, current_month, current_day);

        // 3. 다이어리 - 키워드 일대다 관계 객체 생성

    }

    // 설정 액티비티로 가는 버튼 누른 경우 동작 수행하는 함수
    public void tosettingactivity(View view){
        startActivity(new Intent(this,Setting_Activity.class));
        // 현재 액티비티 종료
        finish();
    }
}