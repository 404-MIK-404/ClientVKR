package com.example.test.ActivityTasks;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.test.ActivityMain.Main;
import com.example.test.Client.ActionTask;
import com.example.test.Client.Client;
import com.example.test.R;
import com.example.test.Task.Tasks;
import com.example.test.addapters.*;

import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 *
 * Это экран отвечает за то чтобы можно было посмотреть задачки через календарь который я создавал руками.
 *
 * @author MIK
 *
 */

public class ViewCalendar extends AppCompatActivity implements CalendarAdapter.onItemListener {

    private TextView monthYearText,TextDate;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Toolbar ToolbarCalendarMode;
    private ListView ListTasks;
    private Button NextMonthButton,PreviousMonthButton;

    private final Client User = Client.UserAndroid;

    private static ArrayList<Tasks> TasksUser;
    private ArrayList<Tasks> data = new ArrayList<>();

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_task);
        User.setActivityNow(this);
        selectedDate = LocalDate.now();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init(){
        java.util.Calendar dataNow = java.util.Calendar.getInstance();
        calendarRecyclerView = findViewById(R.id.CalendarRecyclerView);
        ToolbarCalendarMode = (Toolbar) findViewById(R.id.ToolbarCalendarMode);
        monthYearText = findViewById(R.id.MonthYearTV);
        NextMonthButton = findViewById(R.id.NextMonthButton);
        PreviousMonthButton = findViewById(R.id.PreviousMonthButton);
        TextDate = findViewById(R.id.TextDate);
        ListTasks = findViewById(R.id.ListTasks);

        NextMonthButton.setText(">");
        PreviousMonthButton.setText("<");
        TasksUser = getIntent().getExtras().getParcelableArrayList("com/example/test/Task");
        getSupportActionBar().hide();
        ToolbarCalendarMode.setNavigationIcon(R.drawable.back_to_main);
        eventOnMenuItemActionBar();
        setMonthView();
        setListCurrentTask(dataNow);
        eventClickListView();
    }


    private void eventOnMenuItemActionBar()
    {
        ToolbarCalendarMode.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewCalendar.this, Main.class));
            }
        });
    }

    private void eventClickListView(){
        AlertDialog.Builder alertDialogDel = new AlertDialog.Builder(ViewCalendar.this);
        alertDialogDel.setTitle("You want delete this task ?");
        ListTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewCalendar.this, SeeCurrentTask.class);
                intent.putExtra("CurrentTask",TasksUser.get(position));
                startActivity(intent);
            }
        });

        ListTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogDel.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteTask(position);
                    }
                });
                alertDialogDel.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogDel.show();
                return true;
            }
        });

    }
    public void DeleteTask(int position){
        ActionTask Task = new ActionTask();
        if ("succ".equals(Task.DeleteTaskResult(TasksUser,position)))
        {
            TasksUser.remove(position);
            data.remove(position);
            ListTasks.setAdapter(null);
            TasksListAdapter adapter = new TasksListAdapter(this,R.layout.list_item,data);
            ListTasks.setAdapter(adapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth,this,selectedDate,TasksUser);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private void setListCurrentTask(java.util.Calendar Date){
        java.util.Calendar dateTask = java.util.Calendar.getInstance();
        String textDate = String.format("%02d.%02d.%02d",Date.get(java.util.Calendar.DAY_OF_MONTH),(Date.get(java.util.Calendar.MONTH)+1),Date.get(java.util.Calendar.YEAR));
        TextDate.setText("События на " + textDate);
        if (data.size() != 0){
            ListTasks.setAdapter(null);
            data.clear();
        }
        for (int i = 0; i < TasksUser.size();i++){
            try {
                dateTask.setTime(sdf.parse(TasksUser.get(i).getDateEndTask()));
                if (dateTask.get(java.util.Calendar.YEAR) == Date.get(java.util.Calendar.YEAR)
                    && (dateTask.get(java.util.Calendar.MONTH) + 1) == (Date.get(java.util.Calendar.MONTH) + 1)
                    && dateTask.get(java.util.Calendar.DAY_OF_MONTH) == Date.get(java.util.Calendar.DAY_OF_MONTH)){
                    data.add(TasksUser.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        TasksListAdapter adapter = new TasksListAdapter(this,R.layout.list_item,data);
        ListTasks.setAdapter(adapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date){

        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 2; i <= 42; i++)
        {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    public String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";//или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("LLLL yyyy",new Locale("ru"));
        return firstUpperCase(date.format(formatter));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view){
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view){
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()){
            java.util.Calendar ClickDate = java.util.Calendar.getInstance();
            ClickDate.set(selectedDate.getYear(),selectedDate.getMonth().getValue() - 1,Integer.parseInt(dayText));
            setListCurrentTask(ClickDate);
        }
    }

}