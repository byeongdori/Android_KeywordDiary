package com.example.keyworddiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

            }
        });
    }

}