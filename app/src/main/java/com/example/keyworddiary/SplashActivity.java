package com.example.keyworddiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

// 앱 초기 시작화면 나오기 전 뜨는 스플래쉬형 액티비티
// https://yongtech.tistory.com/100 - 여기 링크 참고하여 초기 화면 추가하기
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
