package com.example.test.Services;

import com.example.test.Client.Client;


public class SynchroCalendarClass {

    private final Client User = Client.UserAndroid;

    private void getReadySynchroCalendar(){
        User.SyncServiceGoogle();
    }

    public void callFuncSync(){
        getReadySynchroCalendar();
    }

}
