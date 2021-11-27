package com.example.keyworddiary;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.keyworddiary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    // DB 생성
    public void testfunction(View view) {
        myDBHelper myDB = new myDBHelper(getApplicationContext());
        myDB.initializeDB(getApplicationContext());
        Toast.makeText(getApplicationContext(), "클래스 분리완료", Toast.LENGTH_LONG).show();
    }

}