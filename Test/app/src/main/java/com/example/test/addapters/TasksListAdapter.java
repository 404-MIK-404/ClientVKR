package com.example.test.addapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.ActivitySignInUp.SignIn;
import com.example.test.Client.ActionTask;
import com.example.test.R;
import com.example.test.Task.Tasks;
import com.google.android.gms.tasks.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksListAdapter extends ArrayAdapter<Tasks> {

    private Context mContext;
    private int mResource;
    private ArrayList<Tasks> DataTasks;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");


    public TasksListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Tasks> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        DataTasks = objects;
    }


    private int year(Calendar calendarNow,Calendar calendarTask){
        return  calendarTask.get(Calendar.YEAR) - calendarNow.get(Calendar.YEAR);
    }

    private int month(Calendar calendarNow,Calendar calendarTask){
        return calendarTask.get(Calendar.MONTH) - calendarNow.get(Calendar.MONTH);
    }

    private int day(Calendar calendarNow,Calendar calendarTask){
        return calendarTask.get(Calendar.DAY_OF_MONTH) - calendarNow.get(Calendar.DAY_OF_MONTH);
    }


    private void setColorView(View convertView,Calendar calendar,Date dateTask,int position) throws ParseException{
        Calendar calendarTask = Calendar.getInstance();
        calendarTask.setTime(dateTask);
        if (year(calendar,calendarTask) >= 0 && month(calendar,calendarTask) >= 1)
        {
            convertView.setBackground(getContext().getDrawable(R.drawable.border_item_task_is_done));
        }
        else if (year(calendar,calendarTask) == 0 && month(calendar,calendarTask) == 0
        && day(calendar,calendarTask) == 1)
        {
            convertView.setBackground(getContext().getDrawable(R.drawable.border_item_task_time_one_day));
        }
        else
        {
            convertView.setBackground(getContext().getDrawable(R.drawable.border_item_task_is_not_done));
        }
    }

    private void setCheckedStatusView(View convertView, int position,CheckBox tvTaskIsDone)
    {
        try {
        Calendar calendar = Calendar.getInstance();
        Date dateTask = sdf.parse(DataTasks.get(position).getDateEndTask());
            if (DataTasks.get(position).getStatus() || DataTasks.get(position).getStatusCode() == 1){
                tvTaskIsDone.setChecked(true);
                convertView.setBackground(getContext().getDrawable(R.drawable.border_item_task_is_done));
            }
            else{
                tvTaskIsDone.setChecked(false);
                setColorView(convertView,calendar,dateTask,position);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setEventCheckBox(View convertView, CheckBox tvTaskIsDone,int position){
        ActionTask actionTask = new ActionTask();
        tvTaskIsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox taskCheckBox = (CheckBox) v;
                Tasks tasks = (Tasks) DataTasks.get(position);
                if (taskCheckBox.isChecked()){
                    tasks.setStatus(true);
                    if ("succ".equals(actionTask.ChangeStatusTask(tasks,"True"))){
                        convertView.setBackground(getContext().getDrawable(R.drawable.border_item_task_is_done));
                        Toast.makeText(convertView.getContext(),"Статус задачи изменён на выполненный",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    tasks.setStatus(false);
                    setCheckedStatusView(convertView,position,tvTaskIsDone);
                    if ("succ".equals(actionTask.ChangeStatusTask(tasks,"False"))){
                        Toast.makeText(convertView.getContext(),"Статус задачи изменён на невыполненный",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        String name = getItem(position).getNameTask();
        String dateBegin = getItem(position).getDateBeginTask();
        String dateEnd = getItem(position).getDateEndTask();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvName = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvdateEnd = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvdateBegin = (TextView) convertView.findViewById(R.id.textView3);
        CheckBox tvTaskIsDone = (CheckBox) convertView.findViewById(R.id.TaskIsDone);

        tvName.setText(name);
        tvdateEnd.setText("Начало: "+ dateBegin);
        tvdateBegin.setText("Конец: "+ dateEnd );
        setEventCheckBox(convertView,tvTaskIsDone,position);
        setCheckedStatusView(convertView,position,tvTaskIsDone);
        return convertView;

    }






}
