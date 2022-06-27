package com.example.test.Task;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Tasks implements Parcelable {

    private String UIDTask;
    private String NameTask;
    private String Description;
    private String DateBeginTask;
    private String DateEndTask;
    private boolean Status;
    private int StatusCode;

    public Tasks(String UIDTask,String NameTask,String Description,String DateBeginTask, String DateEndTask,boolean Status)
    {
        this.UIDTask = UIDTask;
        this.NameTask = NameTask;
        this.Description = Description;
        this.DateBeginTask = DateBeginTask;
        this.DateEndTask = DateEndTask;
        this.Status = Status;
        if (this.Status){
            this.StatusCode = 1;
        }else{
            this.StatusCode = 0;
        }

    }

    protected Tasks(Parcel in) {
        UIDTask = in.readString();
        NameTask = in.readString();
        Description = in.readString();
        DateBeginTask = in.readString();
        DateEndTask = in.readString();
        StatusCode = in.readInt();
    }

    public static final Creator<Tasks> CREATOR = new Creator<Tasks>() {
        @Override
        public Tasks createFromParcel(Parcel in) {
            return new Tasks(in);
        }

        @Override
        public Tasks[] newArray(int size) {
            return new Tasks[size];
        }
    };

    public String getUIDTask()
    {
        return this.UIDTask;
    }


    public String getNameTask()
    {
        return this.NameTask;
    }

    public void setNameTask(String Value)
    {
        this.NameTask = Value;
    }

    public String getDescription()
    {
        return this.Description;
    }

    public Boolean getStatus(){
        return this.Status;
    }

    public void setStatus(Boolean Value){
        this.Status = Value;
        if (this.Status){
            this.StatusCode = 1;
        }
        else {
            this.StatusCode = 0;
        }
    }

    public int getStatusCode(){
        return this.StatusCode;
    }

    public void setStatusCode(int Value){
        this.StatusCode = Value;
        if (Value == 1){
            this.Status = true;
        }
        else {
            this.Status = false;
        }
    }

    public void setDescription(String Value)
    {
        this.Description = Value;
    }

    public String getDateBeginTask()
    {
        return this.DateBeginTask;
    }

    public void setDateBeginTask(String DateBeginTask) { this.DateBeginTask = DateBeginTask; }

    public String getDateEndTask(){
        return this.DateEndTask;
    }

    public void setDateEndTask(String DateEndTask)
    {
        this.DateEndTask = DateEndTask;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(UIDTask);
            dest.writeString(NameTask);
            dest.writeString(Description);
            dest.writeString(DateBeginTask);
            dest.writeString(DateEndTask);
            dest.writeInt(StatusCode);
    }
}
