package com.example.test.ActivityTasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.example.test.Client.ActionTask;
import com.example.test.Client.Client;
import com.example.test.ActivityMain.Main;
import com.example.test.R;
import com.example.test.Task.Tasks;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class CreateTask extends AppCompatActivity{

    private Button BackButton,CreateBt,ButtonAddTask;

    private EditText CreateNameView,CreateTaskDecription;

    private TextView ViewCreateTimeTask,ViewCreateDateTask;

    private final Client User = Client.UserAndroid;
    private Tasks UserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        User.setActivityNow(this);
        getSupportActionBar().hide();
        init();
    }

    private void init()
    {
        CreateBt = (Button) findViewById(R.id.ButtonBackActivityAdd);
        ButtonAddTask = (Button) findViewById(R.id.ButtonAddTask);
        CreateNameView = (EditText) findViewById(R.id.CreateNameView);
        CreateTaskDecription = (EditText) findViewById(R.id.CreateTaskDecription);

        ViewCreateDateTask = (TextView) findViewById(R.id.ViewCreateDateTask);
        ViewCreateTimeTask = (TextView) findViewById(R.id.ViewCreateTimeTask);
    }


    public void setTimeDateToTask(View view){
        callTimerPicker();
        callDatePicker();
    }

    public void applyAddTask(View view){
        if (!TextUtils.isEmpty(CreateNameView.getText().toString()) && !TextUtils.isEmpty(CreateTaskDecription.getText().toString())
            && !"Дата".equals(ViewCreateDateTask.getText().toString()) && !"Время".equals(ViewCreateTimeTask.getText().toString()) ){
            ActionTask Task = new ActionTask();
            if ("succ".equals(Task.AddTaskResult(ViewCreateDateTask.getText().toString() + " " + ViewCreateTimeTask.getText().toString()
                    ,CreateNameView.getText().toString(),CreateTaskDecription.getText().toString())))
            {
                startActivity(new Intent(CreateTask.this, Main.class));
            }
        }
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
                        ViewCreateTimeTask.setText(resultTime);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();

    }

    public void BackToAcitvityAddMenu(View view){
        startActivity(new Intent(CreateTask.this, Main.class));
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
                        ViewCreateDateTask.setText(resultDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

}