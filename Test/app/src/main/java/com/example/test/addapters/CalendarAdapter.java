package com.example.test.addapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.OtherClass.CalendarViewHolder;
import com.example.test.R;
import com.example.test.Task.Tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * Здесь идёт адаптер отображения календаря он был сделан с помощью RecyclerView как я помню
 *
 * @author MIK
 *
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final onItemListener onItemListener;
    private final LocalDate dateNow;
    private final Calendar TodayDate = Calendar.getInstance();
    private final ArrayList<Tasks> TasksUser;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public CalendarAdapter(ArrayList<String> daysOfMonth,onItemListener onItemListener,LocalDate dateNow,ArrayList<Tasks> TasksUser)
    {
        this.TasksUser = TasksUser;
        this.dateNow = dateNow;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }



    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view,onItemListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Calendar cal = Calendar.getInstance();

        if (daysOfMonth.get(position).equals(String.valueOf(dateNow.getDayOfMonth())) && TodayDate.get(Calendar.YEAR) == dateNow.getYear()
                && (TodayDate.get(Calendar.MONTH)+1) == dateNow.getMonth().getValue())
        {
            holder.dayOfMonth.setBackground( ContextCompat.getDrawable(context,R.drawable.today));
        }

        for (int i = 0; i < TasksUser.size();i++)
        {
            try {
                Date dateTask = sdf.parse(TasksUser.get(i).getDateEndTask());
                cal.setTime(dateTask);
                if (cal.get(Calendar.YEAR) == dateNow.getYear() &&
                        (cal.get(Calendar.MONTH)+1) == dateNow.getMonth().getValue())
                {
                    if (daysOfMonth.get(position).equals(String.valueOf(cal.get(Calendar.DAY_OF_MONTH))))
                    {
                        if ( daysOfMonth.get(position).equals(String.valueOf(dateNow.getDayOfMonth())) && TodayDate.get(Calendar.YEAR) == dateNow.getYear()
                                && (TodayDate.get(Calendar.MONTH)+1) == dateNow.getMonth().getValue() && daysOfMonth.get(position).equals(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)))){
                            holder.dayOfMonth.setBackground(ContextCompat.getDrawable(context,R.drawable.today_have_task));
                        }
                        else {
                            holder.dayOfMonth.setBackground( ContextCompat.getDrawable(context,R.drawable.have_task));
                        }
                        break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.dayOfMonth.setText(daysOfMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface onItemListener{

        void onItemClick(int position,String dayText);

    }

}
