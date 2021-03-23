package ie.ul.timetable_viewer;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class saveEvents {
    public static String saveSticker(HashMap<Integer, icon> stickers){
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        int[] orders = getSortedKeySet(stickers);
        for(int i = 0 ; i < orders.length; i++){
            JsonObject obj2 = new JsonObject();
            int idx = orders[i];
            obj2.addProperty("idx",orders[i]);
            JsonArray arr2 = new JsonArray();//5
            ArrayList<calendar> schedules = stickers.get(idx).getSchedules();
            for(calendar schedule : schedules){
                JsonObject obj3 = new JsonObject();
                obj3.addProperty("classTitle",schedule.eventName);
                obj3.addProperty("classPlace",schedule.eventLocation);
                obj3.addProperty("professorName",schedule.getSpeakerName());
                obj3.addProperty("day",schedule.getDay());
                JsonObject obj4 = new JsonObject();//startTime
                obj4.addProperty("hour",schedule.getStartTime().getHour());
                obj4.addProperty("minute",schedule.getStartTime().getMinute());
                obj3.add("startTime",obj4);
                JsonObject obj5 = new JsonObject();//endtTime
                obj5.addProperty("hour",schedule.getEndTime().getHour());
                obj5.addProperty("minute",schedule.getEndTime().getMinute());
                obj3.add("endTime",obj5);
                arr2.add(obj3);
            }
            obj2.add("schedule",arr2);
            arr1.add(obj2);
        }
        obj1.add("sticker",arr1);
        return obj1.toString();
    }

    public static HashMap<Integer,icon> loadSticker(String json){
        HashMap<Integer, icon> stickers = new HashMap<Integer,icon>();
        JsonParser parser = new JsonParser();
        JsonObject obj1 = (JsonObject)parser.parse(json);
        JsonArray arr1 = obj1.getAsJsonArray("sticker");
        for(int i = 0 ; i < arr1.size(); i++){
            icon sticker = new icon();
            JsonObject obj2 = (JsonObject)arr1.get(i);
            int idx = obj2.get("idx").getAsInt();
            JsonArray arr2 = (JsonArray)obj2.get("schedule");
            for(int k = 0 ; k < arr2.size(); k++){
                calendar schedule = new calendar();
                JsonObject obj3 = (JsonObject)arr2.get(k);
                schedule.setEventName(obj3.get("classTitle").getAsString());
                schedule.setEventLocation(obj3.get("classPlace").getAsString());
                schedule.setSpeakerName(obj3.get("professorName").getAsString());
                schedule.setDay(obj3.get("day").getAsInt());
                time_keep startTime = new time_keep();
                JsonObject obj4 = (JsonObject)obj3.get("startTime");
                startTime.setHour(obj4.get("hour").getAsInt());
                startTime.setMinute(obj4.get("minute").getAsInt());
                time_keep endTime = new time_keep();
                JsonObject obj5 = (JsonObject)obj3.get("endTime");
                endTime.setHour(obj5.get("hour").getAsInt());
                endTime.setMinute(obj5.get("minute").getAsInt());
                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                sticker.addSchedule(schedule);
            }
            stickers.put(idx,sticker);
        }
        return stickers;
    }


    static private int[] getSortedKeySet(HashMap<Integer, icon> stickers){
        int[] orders = new int[stickers.size()];
        int i = 0;
        for(int key : stickers.keySet()){
            orders[i++] = key;
        }
        Arrays.sort(orders);
        return orders;
    }
}
