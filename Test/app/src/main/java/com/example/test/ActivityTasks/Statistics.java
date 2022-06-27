package com.example.test.ActivityTasks;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.ActivityMain.Main;
import com.example.test.Client.Client;
import com.example.test.R;
import com.example.test.Task.Tasks;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Statistics extends AppCompatActivity {

    private final Client User = Client.UserAndroid;
    private PieChart PieTaskDoneNotDone;
    private TextView nameValueCountTaskView,nameValueCountDoneTaskView,nameValueCountLateDateTaskView,nameValueCountNotLateDateTaskView;
    private ArrayList<Tasks> Tasks;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getSupportActionBar().hide();
        User.setActivityNow(this);
        init();
    }

    private void init(){
        PieTaskDoneNotDone = findViewById(R.id.PieTaskDoneNotDone);
        nameValueCountTaskView = findViewById(R.id.nameValueCountTaskView);
        nameValueCountDoneTaskView = findViewById(R.id.nameValueCountDoneTaskView);
        nameValueCountLateDateTaskView = findViewById(R.id.nameValueCountLateDateTaskView);
        nameValueCountNotLateDateTaskView = findViewById(R.id.nameValueCountNotLateDateTaskView);
        setParcebleTask();
        setValueView();
    }

    private void setParcebleTask(){
        Tasks = getIntent().getExtras().getParcelableArrayList("Tasks");
    }

    private void setValueView(){
        nameValueCountTaskView.setText(String.valueOf(Tasks.size()));
        int coutTaskDone = getCountTaskDone();
        nameValueCountDoneTaskView.setText(String.valueOf(coutTaskDone));
        nameValueCountLateDateTaskView.setText(String.valueOf(getCountTaskLate()));
        nameValueCountNotLateDateTaskView.setText(String.valueOf(getCountTaskNotLate()));
        initPie(coutTaskDone,Tasks.size() - coutTaskDone);
    }

    private int getCountTaskLate(){
        int count = 0;
        try {
            for (int i = 0 ; i < Tasks.size();i++){
                long timeNow = System.currentTimeMillis();
                Date timeDateTask = sdf.parse(Tasks.get(i).getDateEndTask());
                long timeTask = timeDateTask.getTime();
                if (timeNow > timeTask)
                {
                    count++;
                }
            }
        }
        finally {
            return count;
        }
    }

    private int getCountTaskNotLate(){
        int count = 0;
        try {
            for (int i = 0 ; i < Tasks.size();i++){
                long timeNow = System.currentTimeMillis();
                Date timeDateTask = sdf.parse(Tasks.get(i).getDateEndTask());
                long timeTask = timeDateTask.getTime();
                if (timeNow < timeTask)
                {
                    count++;
                }
            }
        }
        finally {
            return count;
        }

    }


    private int getCountTaskDone(){
        int count = 0;
        for (int i=0;i < Tasks.size();i++)
        {
            if (Tasks.get(i).getStatus())
            {
                count++;
            }
        }
        return count;
    }

    private void initPie(int countTaskDone,int coutTaskNotDone){
        PieTaskDoneNotDone.setDrawHoleEnabled(false);
        PieTaskDoneNotDone.setRotationAngle(180);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Отмеченные задачи",countTaskDone);
        typeAmountMap.put("Не отмеченные задачи",coutTaskNotDone);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,"");
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        PieTaskDoneNotDone.setData(pieData);
        PieTaskDoneNotDone.invalidate();
    }

    public void BackToMenu(View view)
    {
        startActivity(new Intent(Statistics.this, Main.class));
    }

}