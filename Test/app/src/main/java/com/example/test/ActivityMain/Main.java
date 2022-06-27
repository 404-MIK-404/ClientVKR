package com.example.test.ActivityMain;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.test.ActivityTasks.*;
import com.example.test.ActivityTasks.ViewCalendar;
import com.example.test.Client.*;
import com.example.test.R;
import com.example.test.Services.ServiceNotificationRemember;
import com.example.test.Services.SynchroCalendarClass;
import com.example.test.Task.Tasks;
import com.example.test.addapters.TasksListAdapter;


import java.text.*;
import java.time.format.DateTimeFormatter;
import java.util.*;



public class Main extends AppCompatActivity {

    private ListView ViewTasks;
    private Toolbar ToolbarMain;
    private SwipeRefreshLayout refreshUpdateTask;

    private static ArrayList<Tasks> TasksUser = new ArrayList<>();
    private final Client User = Client.UserAndroid;

    private ArrayList<Tasks> data = new ArrayList<>();

    private String command;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();
        init();
    }

    private boolean checkSort(@NonNull java.util.Calendar calendarNow, java.util.Calendar calendarEnd, int day, int month, int year){
        if (calendarEnd.get(java.util.Calendar.DAY_OF_MONTH) - calendarNow.get(java.util.Calendar.DAY_OF_MONTH)  == day
                &&calendarEnd.get(java.util.Calendar.MONTH) - calendarNow.get(java.util.Calendar.MONTH) == month &&
                calendarEnd.get(java.util.Calendar.YEAR) - calendarNow.get(java.util.Calendar.YEAR) == year){
            return true;
        }
        return false;
    }

    private boolean checkSortWeek(@NonNull java.util.Calendar calendarNow, java.util.Calendar calendarEnd, int day, int month, int year){
        if (calendarEnd.get(java.util.Calendar.DAY_OF_MONTH) - calendarNow.get(java.util.Calendar.DAY_OF_MONTH)  <= day
                && calendarEnd.get(java.util.Calendar.DAY_OF_MONTH) - calendarNow.get(java.util.Calendar.DAY_OF_MONTH) >= 0
                &&calendarEnd.get(java.util.Calendar.MONTH) - calendarNow.get(java.util.Calendar.MONTH) == month &&
                calendarEnd.get(java.util.Calendar.YEAR) - calendarNow.get(java.util.Calendar.YEAR) == year){
            return true;
        }
        return false;
    }

    private boolean checkSortMonth(@NonNull java.util.Calendar calendarNow, java.util.Calendar calendarEnd, int month, int year){
        if (calendarEnd.get(java.util.Calendar.MONTH) - calendarNow.get(java.util.Calendar.MONTH) == month &&
                calendarEnd.get(java.util.Calendar.YEAR) - calendarNow.get(java.util.Calendar.YEAR) == year){
            return true;
        }
        return false;
    }

    private void setData(int i){
        if (TasksUser.get(i).getStatus()){
            data.add(TasksUser.get(i));
        }
        else {
           data.add(TasksUser.get(i));
        }
    }

    private void sortTask() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        for (int i=0;i< TasksUser.size() ;i++){
            Date dateEndTask = formatter.parse(TasksUser.get(i).getDateEndTask());
            java.util.Calendar calendarEnd = java.util.Calendar.getInstance();
            calendarEnd.setTime(dateEndTask);
            switch (command){
                case "tommorowTasks":
                    if (checkSort(calendar,calendarEnd,1,0,0)){
                        setData(i);
                    }
                    break;

                case "todayTasks":
                    if (checkSort(calendar,calendarEnd,0,0,0)){
                        setData(i);
                    }
                    break;

                case "weekTasks":
                    if (checkSortWeek(calendar,calendarEnd,7,0,0)){
                        setData(i);
                    }
                    break;

                case "monthTasks":
                    if (checkSortMonth(calendar,calendarEnd,1,0)){
                        setData(i);
                    }
                    break;
                case "allTasks":
                    setData(i);
                    break;
            }
        }
    }


    private void initView()
    {
        try {
            data.clear();
            ViewTasks.setAdapter(null);
            sortTask();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            TasksListAdapter adapter = new TasksListAdapter(this,R.layout.list_item,data);
            ViewTasks.setAdapter(adapter);
        }
    }


    private void eventOnMenuItemActionBar()
    {
        ToolbarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.itemAddTask:
                        startActivity(new Intent(Main.this, CreateTask.class));
                        break;
                    case R.id.allTasks:
                        command = "allTasks";
                        initView();
                        break;
                    case R.id.TomorrowTasks:
                        command = "tommorowTasks";
                        initView();
                        break;
                    case R.id.TodayTasks:
                        command = "todayTasks";
                        initView();
                        break;
                    case R.id.WeekTasks:
                        command = "weekTasks";
                        initView();
                        break;
                    case R.id.MothTasks:
                        command = "monthTasks";
                        initView();
                        break;
                    case R.id.itemStaticSee:
                        mainToSeeStatistics();
                        break;
                }
                return false;
            }
        });
    }

    private void mainToSeeStatistics(){
        Intent changeMain = new Intent(Main.this,Statistics.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Tasks",TasksUser);
        changeMain.putExtras(bundle);
        startActivity(changeMain);
    }

    public void onClickCalendarMode(View view)
    {
        Intent intentCalendarMode = new Intent(Main.this, ViewCalendar.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("com/example/test/Task",TasksUser);
        intentCalendarMode.putExtras(bundle);
        startActivity(intentCalendarMode);
    }

    private void eventTaskListViewLongClick()
    {
        AlertDialog.Builder alertDialogDel = new AlertDialog.Builder(Main.this);
        alertDialogDel.setTitle("You want delete this task ?");
        ViewTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    private void init()
    {
        command = "allTasks";
        User.setActivityNow(this);
        ViewTasks = (ListView) findViewById(R.id.ViewTasks);
        ToolbarMain = findViewById(R.id.ToolbarMain);
        refreshUpdateTask = (SwipeRefreshLayout) findViewById(R.id.refreshUpdateTask);
        ToolbarMain.inflateMenu(R.menu.menu_main);
        initListTasks();
        setEventItemClick();
        setRefreshEvent();
        eventOnMenuItemActionBar();
        initView();
        eventTaskListViewLongClick();
        initServiceNoti();
        if (!User.codeGoogleIsEmpty()) {
            initThread();
        }
    }

    private void initServiceNoti(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Date endDateTask;
            Date nowDate = new Date();
            for (int i = 0; i < TasksUser.size();i++){
                try {
                    endDateTask = sdf.parse(TasksUser.get(i).getDateEndTask());
                    long longEndDate = endDateTask.getTime() - 14400000;
                    long longNowDate = System.currentTimeMillis();
                    endDateTask.setTime(longEndDate);
                    nowDate.setTime(longNowDate);
                    if ( longEndDate > longNowDate && !TasksUser.get(i).getStatus() ){
                        Intent intent = new Intent(this, ServiceNotificationRemember.class);
                        intent.putExtra("Timer",longEndDate - longNowDate);
                        intent.putExtra("CurrentTask",TasksUser.get(i));
                        intent.putExtra("Number_Task",i);
                        startService(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initThread(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SynchroCalendarClass CalendarClass = new SynchroCalendarClass();
                CalendarClass.callFuncSync();
                handler.postDelayed(this,120000);
            }
        },1);
    }


    private void initListTasks()
    {
        ActionTask Task = new ActionTask();
        if (TasksUser.size() != 0){
            data.clear();
            TasksUser.clear();
            ViewTasks.setAdapter(null);
        }
        TasksUser = User.getTasksResult(Task.GetAllTasks(),TasksUser);
    }

    private void refreshListTask(){
        try {
            ActionTask Task = new ActionTask();
            TasksUser.clear();
            ViewTasks.setAdapter(null);
            TasksUser = User.getTasksResult(Task.GetAllTasks(),TasksUser);
            data.clear();
            sortTask();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            TasksListAdapter adapter = new TasksListAdapter(this,R.layout.list_item,data);
            ViewTasks.setAdapter(adapter);
        }
    }

    private void setRefreshEvent()
    {
        refreshUpdateTask.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        refreshUpdateTask.setRefreshing(false);
                        refreshListTask();
                    }
                }, 2000); // Delay in millis
            }
        });
    }



    public void DeleteTask(int position){
        ActionTask Task = new ActionTask();
        if ("succ".equals(Task.DeleteTaskResult(TasksUser,position)))
        {
            TasksUser.remove(position);
            data.remove(position);
            ViewTasks.setAdapter(null);
            TasksListAdapter adapter = new TasksListAdapter(this,R.layout.list_item,data);
            ViewTasks.setAdapter(adapter);
        }
    }




    private void setEventItemClick()
    {
        ViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Main.this, SeeCurrentTask.class);
                    intent.putExtra("CurrentTask",TasksUser.get(position));
                    startActivity(intent);
            }
        });
    }





}