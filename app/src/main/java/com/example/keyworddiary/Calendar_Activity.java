package com.example.keyworddiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import com.example.keyworddiary.databinding.ActivityCalendarBinding;
import com.example.keyworddiary.databinding.ActivityResultBinding;

public class Calendar_Activity extends AppCompatActivity {

    private ActivityCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // 캘린더 뷰 관련 함수
        binding.resultCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            // 월이 0부터 시작하네..?
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Log.i("TEST", Integer.toString(i));
                Log.i("TEST", Integer.toString(i1));
                Log.i("TEST", Integer.toString(i2));
                // 이 날짜를 누르면 아래 프래그먼트에 정보 띄우기
            }


        });

        // 월간, 요일별 통계 띄우기
    }

}