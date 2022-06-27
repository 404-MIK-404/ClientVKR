package com.example.test.Client;

import com.example.test.Task.Tasks;
import java.util.ArrayList;


public class ActionTask {

        private final Client User = Client.UserAndroid;

        public ActionTask(){

        }

        public String GetAllTasks(){
            User.CreateAction("action","GET_TASKS");
            User.CreateActionUid();
            User.SendJsonMessage();
            User.WaitMessage();
            return User.getResultFromServer();
        }

        public String DeleteTaskResult(ArrayList<Tasks> TasksUser,int position)
        {
            User.CreateAction("action","DEL_TASK");
            User.CreateAction("uid_us",User.getUIDUser());
            User.CreateAction("uid_task",TasksUser.get(position).getUIDTask());
            if (!User.codeGoogleIsEmpty()){
                User.CreateAction("auth_code",User.getGoogleAuthCode());
            }
            User.SendJsonMessage();
            User.WaitMessage();
            return User.getResultFromServer();

        }


        public String EditTaskResult(Tasks UserTask)
        {
            User.CreateAction("action","CHANGE_TASK");
            User.CreateAction("uid",User.getUIDUser());
            if (!User.codeGoogleIsEmpty()){
                User.CreateAction("auth_code",User.getGoogleAuthCode());
            }
            User.CreateAction("uid_task",UserTask.getUIDTask());
            User.CreateAction("name_task", UserTask.getNameTask());
            User.CreateAction("description_task",UserTask.getDescription());
            User.CreateAction("date_start",UserTask.getDateBeginTask());
            User.CreateAction("date_end",UserTask.getDateEndTask());
            User.SendJsonMessage();
            User.WaitMessage();
            return User.getResultFromServer();
        }


        public String ChangeStatusTask(Tasks UserTask,String statusTask){

            User.CreateAction("action","SET_TASKS_DONE");
            User.CreateAction("uid",User.getUIDUser());
            User.CreateAction("uid_task",UserTask.getUIDTask());
            User.CreateAction("change_task",statusTask);
            User.SendJsonMessage();
            User.WaitMessage();
            return User.getResultFromServer();
        }


        public String AddTaskResult(String DateEnd,String NameTask,String DescriptionTask)
        {
            User.CreateAction("action","CREATE_TASK");
            User.CreateAction("DateEndTask",DateEnd);
            User.CreateAction("NameTask",NameTask);
            User.CreateAction("DiscrTask",DescriptionTask);
            if (!User.codeGoogleIsEmpty()){
                User.CreateAction("auth_code",User.getGoogleAuthCode());
            }
            User.CreateActionUid();
            User.SendJsonMessage();
            User.WaitMessage();
            return User.getResultFromServer();

        }

}
