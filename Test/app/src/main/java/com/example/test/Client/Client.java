package com.example.test.Client;

import android.app.Activity;
import android.content.Intent;

import com.example.test.ActivityMain.Main;
import com.example.test.ActivitySignInUp.Registration;
import com.example.test.Task.Tasks;
import com.example.test.Verification;
import com.example.test.WaitConnectionServer;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.*;

/**
 *
 *
 * Этот класс был сделан как синглтон, но через enum, тип его проще так было сделать, но лучше я бы сделал его как обычным классом
 * Здесь создан внутрений класс как Web Socket Client.
 * Данный класс отвечает за доставку и отправку всего этого клиенту.
 *
 *
 */

public enum Client {

    UserAndroid  {

        //Веб-сокет
        private WebSocketClient mWs;

        private boolean connectionWebSocket = false;

        //Адрес нашего веб-сокета
        private final String ADDRESS_IP = "ws://192.168.1.38:9000";

        //JSON объект
        private JSONObject object = new JSONObject();

        //Результат сервера
        private String result_recieve = "";

        private String UidUser;

        //Рабочее активити на данный момент
        private Activity NowActivity;

        //Ссылка на адрес сервака
        private URI uri ;

        private String google_auth_code = "";

        private String codeVeryfication;


        public void Connection() throws URISyntaxException, InterruptedException {
            uri = new URI(ADDRESS_IP);
            mWs = new WebSocketClient(uri,new Draft_6455(),null,1000) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        System.out.println( "opened connection" );
                    }

                    @Override
                    public void onMessage(String message) {
                        try {
                            JSONObject recieve = new JSONObject(message);
                            if (EqualsJSONAction("ENT_ACC",recieve)  || EqualsJSONAction("REG_ACC",recieve) || EqualsJSONAction("GOOGLE_ENT_ACC",recieve))
                            {
                                result_recieve = recieve.getString("result");
                                UidUser = recieve.getString("uid");
                            }
                            else if ( EqualsJSONAction("GET_TASKS",recieve))
                            {
                                result_recieve = recieve.getString("result");
                            }
                            else if (EqualsJSONAction("DEL_TASK",recieve) ||
                                    EqualsJSONAction("CHANGE_TASK",recieve) ||
                                    EqualsJSONAction("CREATE_TASK",recieve) ||
                                    EqualsJSONAction("SET_TASKS_DONE",recieve) ||
                                    EqualsJSONAction("CHECK_VERYFICATION",recieve))
                            {
                                result_recieve = recieve.getString("result");
                            }
                            else if (EqualsJSONAction("SEND_VERYFICATION",recieve)){
                                codeVeryfication = recieve.getString("code_very");
                                UidUser = recieve.getString("uid");
                                changeActivityToVery();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        System.out.println( "closed connection" );
                        if (connectionWebSocket){
                            connectionWebSocket = false;
                            changeToReconnectActivity();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }

                };
        }


        private void changeActivityToVery(){
            Intent activity = new Intent(NowActivity, Verification.class);
            NowActivity.startActivity(activity);
        }

        public void checkResultConnection(){
            try {
                if (!resultConnection(mWs))
                {
                    closeApp(mWs);
                }
                else {
                    connectionWebSocket = true;
                }
            } catch (InterruptedException e ) {
                e.printStackTrace();
            }

        }

        public void initiateSocketConnection(){
            try {
                Connection();
                checkResultConnection();
            }
            catch (InterruptedException | URISyntaxException e) {
                e.printStackTrace();
            }

        }



        public Boolean codeGoogleIsEmpty(){
            return google_auth_code.isEmpty();
        }

        private void closeApp(WebSocketClient mWs) throws InterruptedException {
            int try_connect_server = 0;
            while (try_connect_server < 10)
            {
                if (resultReconnect(mWs))
                {
                    break;
                }
                else {
                    try_connect_server++;
                }
            }
            if (try_connect_server >= 10)
            {
                changeToReconnectActivity();

            }
        }

        private void changeToReconnectActivity(){
            Intent activity = new Intent(NowActivity, WaitConnectionServer.class);
            NowActivity.startActivity(activity);
        }

        private boolean resultConnection(WebSocketClient mWs) throws InterruptedException {
            if (mWs.connectBlocking(10, TimeUnit.SECONDS)){
                return true;
            }
            return false;
        }

        private boolean resultReconnect(WebSocketClient mWs) throws InterruptedException{
            if (null != mWs && !mWs.isOpen() )
            {
                if (mWs.reconnectBlocking()){
                    return true;
                }
            }
            return false;
        }

        public boolean reconnectToServer() throws InterruptedException {
            try {
                Connection();
            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
            if (!resultConnection(mWs)) {
                return false;
            }
            else {
                connectionWebSocket = true;
                return true;
            }
        }


        private boolean EqualsJSONAction(String Value,JSONObject object) {
            try {
                return Value.equals(object.getString("action"));
            }
            catch (JSONException ex)
            {
                return false;
            }
        }


        public ArrayList<Tasks>  getTasksResult(String result, ArrayList<Tasks> Tasks)
        {
            JsonFileWork jsonFile = new JsonFileWork(Tasks);
            return jsonFile.getArrayTasks(result,Tasks);
        }

        private void setGoogleAuthCode(String value){
            this.google_auth_code = value;
        }

        public String  getUIDUser()
        {
            return this.UidUser;
        }


        public String getGoogleAuthCode(){
            return this.google_auth_code;
        }

        public String getResultFromServer()
        {
            String result = this.result_recieve;
            this.result_recieve = "";
            return result;
        }


        private boolean ResultIsEmpty()
        {
            return this.result_recieve.isEmpty();
        }

        public void CreateAction(String Header,String Value)
        {
            try {
                object.put(Header, Value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void CreateActionUid()
        {
            try {
                object.put("uid", UidUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        public String SignIn(GoogleSignInResult result){
            CreateAction("action","GOOGLE_ENT_ACC");
            CreateAction("DisplayName",result.getSignInAccount().getDisplayName());
            CreateAction("email",result.getSignInAccount().getEmail());
            CreateAction("password",result.getSignInAccount().getId());
            CreateAction("auth_code",result.getSignInAccount().getServerAuthCode());
            setGoogleAuthCode(result.getSignInAccount().getServerAuthCode());
            SendJsonMessage();
            WaitMessage();
            return getResultFromServer();
        }


        public String SignIn(String log,String pass){
            CreateAction("action","ENT_ACC");
            CreateAction("login",log);
            CreateAction("password",pass);
            SendJsonMessage();
            WaitMessage();
            return getResultFromServer();
        }

        public String SyncServiceGoogle(){
            CreateAction("action","SYNC_CALENDAR");
            CreateAction("auth_code",google_auth_code);
            CreateActionUid();
            SendJsonMessage();
            return getResultFromServer();
        }


        private void ClearJsonFile()
        {
            Iterator keys = object.keys();
            while(keys.hasNext())
            {
                if (object.length() != 0)
                {
                    object.remove((String) object.keys().next());
                }
                else{
                    break;
                }
            }
        }

        public void SendJsonMessage()
        {
            if (object.length() != 0)
            {
                mWs.send(object.toString());
                ClearJsonFile();
            }
        }

        public void setActivityNow(Activity value)
        {
            NowActivity = value;
        }

        public Activity getActivityNow(){
            return this.NowActivity;
        }


        public String  getCodeVeryfaction(){
            return this.codeVeryfication;
        }

        public void WaitMessage()
        {
            while(true)
            {
                if (!ResultIsEmpty() || !String.valueOf(codeVeryfication).isEmpty())
                {
                    break;
                }
            }
        }


        public String SendVeryficationCode(int codeVeryfication){
            CreateAction("action","CHECK_VERYFICATION");
            CreateAction("code_veryfication",Integer.toString(codeVeryfication));
            CreateActionUid();
            SendJsonMessage();
            this.codeVeryfication = "";
            WaitMessage();
            return getResultFromServer();
        }


    };


    public abstract String SignIn(GoogleSignInResult result);
    public abstract String SignIn(String log,String pass);
    public abstract String SendVeryficationCode(int codeVeryfication);

    public abstract boolean reconnectToServer() throws InterruptedException;



    public abstract String  getCodeVeryfaction();
    public abstract void initiateSocketConnection();
    public abstract void checkResultConnection();
    public abstract Activity getActivityNow();
    public abstract String getGoogleAuthCode();
    public abstract Boolean codeGoogleIsEmpty();
    public abstract String SyncServiceGoogle();
    public abstract ArrayList<Tasks> getTasksResult(String result, ArrayList<Tasks> TasksUser);
    public abstract String getUIDUser();
    public abstract void WaitMessage();
    public abstract void Connection() throws URISyntaxException, InterruptedException;
    public abstract void CreateAction(String Header,String Value);
    public abstract String getResultFromServer();
    public abstract void SendJsonMessage();
    public abstract void setActivityNow(Activity value);
    public abstract void CreateActionUid();

}
