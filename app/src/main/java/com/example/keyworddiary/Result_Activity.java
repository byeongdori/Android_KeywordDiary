package com.example.keyworddiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

            // 점수 받아오기
            Double[] userScore = new Double[3];
            Integer userid = Integer.parseInt(myDB.getUser(this, Username)[0]);
            userScore = myDB.getDiaryScore(this, userid);
        }
        if (Username == null) {
            binding.userinfoName.setText("유저 이름이 설정 되어있지 않아요!");
        }
        // 점수 슬라이드 바
//       binding.Scorerange.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
//            @Override
//            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
//
//            }
//        });
//
//        binding.Scorerange.setLabelFormatter(Float minimum, Float maximum) {
//
//        };
    }

    // 이전 버튼 눌렀을 때 동작하는 함수, 이전 액티비티 실행하도록 만들기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));

        // 현재 액티비티 종료
        finish();
    }

    public void tocalendaractivity(View view) {

        startActivity(new Intent(this,Calendar_Activity.class));

        // 현재 액티비티 종료
        finish();
    }
}