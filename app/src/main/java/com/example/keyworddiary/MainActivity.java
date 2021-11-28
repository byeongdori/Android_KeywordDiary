package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.keyworddiary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // DB 객체
    public myDBHelper myDB = new myDBHelper(this);

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

    // 점수바 동작 감지 함수
    public int getTodayScore(View view) {
        // binding.todayScore.
        return 0;
    }
}