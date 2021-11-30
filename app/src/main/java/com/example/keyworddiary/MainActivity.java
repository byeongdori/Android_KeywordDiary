package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.keyworddiary.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // DB 객체
    public myDBHelper myDB = new myDBHelper(this);

    // 유저 이름 저장하는 변수
    public String Username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        // 앱 실행시 이전 정보 불러오기
        SharedPreferences pref = getSharedPreferences("username", Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("username"))) {
            Username = pref.getString("username", "");
            binding.Greetingtext.setText(Username);
        }
        if (Username == null) {
            binding.Greetingtext.setText("유저 이름이 설정 되어있지 않아요!");
            binding.SetUsernameText.setText("설정 창에 가서 유저 이름을 설정해주세요!");
        }

        // Seekbar에 리스너 등록, 점수대 별로 변화하는 텍스트 설정하기
        binding.todayScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int currentscore;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        int todayScore = binding.todayScore.getProgress();

        // 변수 활용하기, DB에 저장하고 다음으로 넘어가나? 여기가 처리할 부분이 많겠네, 결과 페이지로 넘어갈 때 로딩?
    }
}