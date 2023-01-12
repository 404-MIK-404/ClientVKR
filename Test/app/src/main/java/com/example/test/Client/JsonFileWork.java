package com.example.test.Client;

import android.util.Log;

import com.example.test.Task.Tasks;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class JsonFileWork {

    private JSONObject jsonData;
    private ArrayList<Tasks> Tasks;

    public JsonFileWork(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public JsonFileWork(ArrayList<Tasks> Tasks){
        this.Tasks = Tasks;
    }

    public ArrayList<Tasks> getArrayTasks(String result, ArrayList<Tasks> Tasks){
        try {
            JSONObject jsonObject = new JSONObject(result.trim());
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext())
            {
                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONObject)
                {
                    JSONObject jsonObject1 = jsonObject.getJSONObject(key);
                    Tasks.add(new Tasks(key, jsonObject1.getString("Name"),
                            jsonObject1.getString("Description"), jsonObject1.getString("Start_Date"),
                            jsonObject1.getString("End_Date"), jsonObject1.getBoolean("Status"))
                    );
                }
            }

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
        return Tasks;
    }

    public JSONObject convertArrayListToJson(ArrayList<Tasks> Tasks){
        JSONObject jsonFileResult = new JSONObject();
        Gson gson = new Gson();
        try {
            for (int i = 0; i < Tasks.size();i++){
                String result = gson.toJson(Tasks.get(i));
                JSONObject array = new JSONObject(result);
                jsonFileResult.put(Tasks.get(i).getUIDTask(),array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonFileResult;
    }



    public JSONObject getJsonData() {
        return jsonData;
    }


}
