package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.keyworddiary.databinding.ActivityMainBinding;
import com.example.keyworddiary.databinding.ActivityResultBinding;

public class Result_Activity extends AppCompatActivity {

    private ActivityResultBinding binding;

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
        }
        if (Username == null) {
            binding.userinfoName.setText("유저 이름이 설정 되어있지 않아요!");
        }
    }


}