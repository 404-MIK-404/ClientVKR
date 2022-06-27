package com.example.test.OtherClass;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.addapters.CalendarAdapter;


public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView dayOfMonth;
    private final CalendarAdapter.onItemListener onItemListener;


    public CalendarViewHolder(@NonNull View itemView,CalendarAdapter.onItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.CellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getAdapterPosition(),(String) dayOfMonth.getText());
    }
}
