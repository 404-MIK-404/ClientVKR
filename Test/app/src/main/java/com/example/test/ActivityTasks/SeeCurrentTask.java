package com.example.test.ActivityTasks;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import com.example.test.Client.ActionTask;
import com.example.test.Client.Client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.test.ActivityMain.Main;
import com.example.test.R;
import com.example.test.Task.Tasks;


/**
 *
 * Данный экран отвечает за просмотр данного таска который пользователь выбрал
 *
 * @author MIK
 *
 */

public class SeeCurrentTask extends AppCompatActivity {


    private TextView DateEnd,ViewTimeTask,ViewDateTask;
    private EditText NameTask,Description;
    private Button BackBt;
    private final Client User = Client.UserAndroid;
    private Tasks UserTask;
    private final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_task);
        getSupportActionBar().hide();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init()
    {
        NameTask = (EditText) findViewById(R.id.NameView);
        Description = (EditText) findViewById(R.id.TaskDecription);
        DateEnd = (TextView) findViewById(R.id.DateEnd);
        BackBt = (Button) findViewById(R.id.ButtonBack);
        ViewDateTask = (TextView) findViewById(R.id.ViewDateTask);
        ViewTimeTask = (TextView) findViewById(R.id.ViewTimeTask);
        setTaskActivity();
        getParcebaleValue();
        setTextEditView();
        setDateToTextView(UserTask.getDateEndTask());
    }

    private void setTaskActivity()
    {
        User.setActivityNow(this);
    }

    private void setTextEditView(){
        NameTask.setText(UserTask.getNameTask());
        Description.setText(UserTask.getDescription());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDateToTextView(String DateText){
        try {
            Date date;
            date = sdf.parse(DateText);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String resultTime = String.format("%02d:%02d",date.getHours(),date.getMinutes());
            String resultDate = String.format("%02d-%02d-%02d",localDate.getYear(),localDate.getMonth().getValue(),localDate.getDayOfMonth());
            ViewTimeTask.setText(resultTime);
            ViewDateTask.setText(resultDate);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void getParcebaleValue(){
        UserTask = getIntent().getParcelableExtra("CurrentTask");
    }


    public void setTimeDateToTask(View view){
        callTimerPicker();
        callDatePicker();
    }


    private void callTimerPicker(){
        final Calendar cal = Calendar.getInstance();
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String resultTime = String.format("%02d:%02d",hourOfDay,minute);
                        ViewTimeTask.setText(resultTime);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();

    }

    private void callDatePicker(){
        final Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String resultDate = String.format("%02d-%02d-%02d",year,(monthOfYear + 1),dayOfMonth);
                        ViewDateTask.setText(resultDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }


    public void applyEditTask(View view){
        if (!TextUtils.isEmpty(NameTask.getText().toString()) && !TextUtils.isEmpty(Description.getText().toString()))
        {
            ActionTask Task = new ActionTask();
            UserTask.setNameTask(NameTask.getText().toString());
            UserTask.setDescription(Description.getText().toString());
            UserTask.setDateEndTask(ViewDateTask.getText().toString() + " " + ViewTimeTask.getText().toString());
            if ("succ".equals(Task.EditTaskResult(UserTask)))
            {
                startActivity(new Intent(SeeCurrentTask.this, Main.class));
            }
        }
    }

    public void BackToMenu(View view)
    {
            startActivity(new Intent(SeeCurrentTask.this, Main.class));
    }





}