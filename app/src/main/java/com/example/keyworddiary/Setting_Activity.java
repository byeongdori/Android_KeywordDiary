package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
        finish();
    }

    // 유저 정보 DB에 저장
    // DB 관련 함수 수정해야함, 유저를 처음 넣는경우, 유저가 있는데 수정하는 경우, 직접 DB 클라스에서 수정해야함
    public void saveuserinfo(View view) {

        if (binding.UsernameInput.length() != 0 && binding.Userageinput.length() != 0) {
            Username = binding.UsernameInput.getText().toString();
            Userage = Integer.parseInt(binding.Userageinput.getText().toString());

            String[] getUserresult = myDB.getUser(this, Username);

            // 유저 존재하지 않는 경우, 생성한 후 받아오기!
            if (getUserresult == null) {
                myDB.insertUser(this, Username, Userage);
                getUserresult = myDB.getUser(this, Username);
            }

            // 저장소에 값 저장
            SharedPreferences sharedPreferences = getSharedPreferences("SharedResource", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // 사용자가 입력한 저장할 데이터
            // key, value를 이용하여 저장하는 형태
            editor.putString("username", Username);
            editor.putInt("userage", Userage);
            editor.commit();

            // 저장 끝나면 메인 화면으로 이동
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            Log.i("TEST", "좀 떠라");
            Toast.makeText(this, "입력하지 않은 정보가 있어요!", Toast.LENGTH_LONG).show();
        }
    }

    // 데이터 초기화 함수!
    public void initializedata(View view) {

        myDB.initializeDB(this);
        SharedPreferences sharedPreferences = getSharedPreferences("SharedResource", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}