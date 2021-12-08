package com.example.keyworddiary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keyworddiary.databinding.FragmentCalendarinfoBinding;

import java.util.ArrayList;

public class Fragment_calendarinfo extends Fragment {

    private FragmentCalendarinfoBinding binding;
    public Calendar_Activity calendar_activity;

    public myDBHelper myDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        calendar_activity = (Calendar_Activity) getActivity();
        // DB 객체
        myDB = new myDBHelper(calendar_activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarinfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        calendar_activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setFragmentinfo(String username, int year, int month, int day) {
        ArrayList<String> dailyresult;

        binding.CurrentDate.setText(year + "년 " + month + "월 " + day + "일의 정보");
        int userid = Integer.parseInt(myDB.getUser(getActivity().getApplicationContext(), username)[0]);
        dailyresult = myDB.getDiaryScoreDaily(getActivity().getApplicationContext(), userid, year, month, day);

        Log.i("TEST", Integer.toString(dailyresult.size()));
        if (dailyresult.size() != 0) {
            int dailyScore_result = Integer.parseInt(dailyresult.get(0));
            binding.dailyScore.setText(Integer.toString(dailyScore_result) + "점");
        }
        else {
            binding.dailyScore.setText("데이터가 존재하지 않아요!");
        }

        ArrayList<String> dailyKeywordresult = myDB.getDiaryScoreDaily(getActivity().getApplicationContext(), userid, year, month, day);
        String currentdatekeyword_text = "- ";
        for (int i = 1; i < dailyKeywordresult.size(); i++) {
            currentdatekeyword_text += dailyKeywordresult.get(i);
            if (i != dailyKeywordresult.size() - 1) {
                currentdatekeyword_text += ", ";
            }
        }
        binding.CurrentDateKeyword.setText(currentdatekeyword_text);
    }
}