package com.example.test.addapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test.R;
import com.example.test.Task.Tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskCalendarMode extends ArrayAdapter<Tasks> {

    private static final String TAG = "TasksListAdapter";

    private Context mContext;


    int mResource;
    ArrayList<Tasks> DataTasks;



    public TaskCalendarMode(@NonNull Context context, int resource, @NonNull ArrayList<Tasks> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        DataTasks = objects;
    }

    private String getTime(String DateTime)
    {
        String result = "00:00";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-DD HH:mm").parse(DateTime);
            result = String.format("%02d:%02d:%02d", date.getHours(), date.getMinutes(), date.getSeconds());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }



    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        String name = getItem(position).getNameTask();
        String dateEnd = getItem(position).getDateEndTask();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        return convertView;

    }


}
