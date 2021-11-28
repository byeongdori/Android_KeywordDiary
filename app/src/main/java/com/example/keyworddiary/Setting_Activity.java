package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.keyworddiary.databinding.ActivityMainBinding;
import com.example.keyworddiary.databinding.ActivitySettingBinding;

public class Setting_Activity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    // DB 객체
    public myDBHelper myDB = new myDBHelper(this);

    public String Username = null;
    public Integer Userage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    // 이전 버튼 눌렀을 때 동작하는 함수, 이전 액티비티 실행하도록 만들기
   @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }

    // 유저 정보 DB에 저장
    public void saveuserinfo(View view) {
        Username = binding.UsernameInput.getText().toString();
        Userage = Integer.parseInt(binding.Userageinput.getText().toString());
        myDB.insertUser(this, Username, Userage);
    }
}