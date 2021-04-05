package ie.ul.ulapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Timetable_Save_Events {

    public static void saveicon(final HashMap<Integer, Timetable_icons> event_icon){

        //Connect to DB and get the current user's email
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }
        DocumentReference docIdRef = db.collection("timetable").document("18245137");
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    final int[] event_index = getSortedKeySet(event_icon);
                    if (task.isSuccessful()) {
                        final DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.get("index") != null) {
                                System.out.println("I am in first if");
                                addToDatabase(event_icon, event_index);

                            } else {
                                Map<String, Object> calendar_index = new HashMap<>();
                                calendar_index.put("index", 0);
                                db.collection("timetable").document("18245137")
                                        .set(calendar_index).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully written!");
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error writing document", e);
                                            }
                                        });
                                System.out.println("I am in first else");
                                addToDatabase(event_icon, event_index);

                            }
                        } else {
                            System.out.println("I am in second if");
                            Map<String, Object> calendar_index = new HashMap<>();
                            calendar_index.put("index", 0);
                            db.collection("timetable").document("18245137")
                                    .set(calendar_index).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully written!");

                                    addToDatabase(event_icon, event_index);
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });

                        }
                    }
                }
            });
    }

    public static void addToDatabase(final HashMap<Integer, Timetable_icons> final_event_icon, final int[] event_index ) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }
        final CollectionReference db2 = db.collection("timetable");
            Log.d("TAG", "your field exist");
            final DocumentReference docIdRef = db.collection("timetable").document("18245137");
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (int i : event_index) {
                            JsonObject obj2 = new JsonObject();
                            JsonArray arr2 = new JsonArray();//5
                            ArrayList<Timetable_Event> calendars = Objects.requireNonNull(final_event_icon.get(i)).getCalendars();
                            int index = -1;
                            DocumentSnapshot document = task.getResult();
                            Map<String, Object> calendar_index = new HashMap<>();

                            index = Integer.parseInt(document.get("index").toString()) + 1;
                            System.out.println(document.get("index"));
                            obj2.addProperty("idx", index);
                            calendar_index.put("index", index);
                            docIdRef.update(calendar_index);

                            JsonObject obj6 = new JsonObject();
                            obj6.addProperty("idx", index);

                            for (Timetable_Event calendar : calendars) {
                                JsonObject obj3 = new JsonObject();
                                obj3.addProperty("eventName", calendar.eventName);
                                obj3.addProperty("eventLocation", calendar.eventLocation);
                                obj3.addProperty("speakerName", calendar.speakerName);
                                obj3.addProperty("day", calendar.getDay());
                                JsonObject obj4 = new JsonObject();//startTime
                                obj4.addProperty("hour", calendar.getStartTime().getHour());
                                obj4.addProperty("minute", calendar.getStartTime().getMinute());
                                obj3.add("startTime", obj4);
                                JsonObject obj5 = new JsonObject();//endTime
                                obj5.addProperty("hour", calendar.getEndTime().getHour());
                                obj5.addProperty("minute", calendar.getEndTime().getMinute());
                                obj3.add("endTime", obj5);
                                arr2.add(obj3);

//
//                                System.out.println("Obj3: " + obj3.toString());
//                                System.out.println("Obj2: " + obj2.toString());
//                                System.out.println("Obj5: " + obj5.toString());

                                Map<String, Object> calendar_activity = new HashMap<>();
                                calendar_activity.put(obj6.toString(), obj3.toString());
                                docIdRef.update(calendar_activity);
                            }
                        }
                    }
            }
        });
    }

    public static HashMap<Integer, Timetable_icons> loadIcon(String json){
        HashMap<Integer, Timetable_icons> icon = new HashMap<Integer, Timetable_icons>();
        JsonParser parser = new JsonParser();
        JsonObject obj1 = (JsonObject)parser.parse(json);
        JsonArray arr1 = obj1.getAsJsonArray("icon");
        for(int i = 0 ; i < arr1.size(); i++){
            Timetable_icons icon1 = new Timetable_icons();
            JsonObject obj2 = (JsonObject)arr1.get(i);
            int idx = obj2.get("idx").getAsInt();
            JsonArray arr2 = (JsonArray)obj2.get("schedule");
            for(int k = 0 ; k < arr2.size(); k++){
                Timetable_Event schedule = new Timetable_Event();
                JsonObject obj3 = (JsonObject)arr2.get(k);
                schedule.setEventName(obj3.get("eventName").getAsString());
                schedule.setEventLocation(obj3.get("eventLocation").getAsString());
                schedule.setSpeakerName(obj3.get("speakerName").getAsString());
                schedule.setDay(obj3.get("day").getAsInt());
                Timetable_Time_Keeper startTime = new Timetable_Time_Keeper();
                JsonObject obj4 = (JsonObject)obj3.get("startTime");
                startTime.setHour(obj4.get("hour").getAsInt());
                startTime.setMinute(obj4.get("minute").getAsInt());
                Timetable_Time_Keeper endTime = new Timetable_Time_Keeper();
                JsonObject obj5 = (JsonObject)obj3.get("endTime");
                endTime.setHour(obj5.get("hour").getAsInt());
                endTime.setMinute(obj5.get("minute").getAsInt());
                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                icon1.addIcon(schedule);
            }
            icon.put(idx,icon1);
        }
        return icon;
    }


    static private int[] getSortedKeySet(HashMap<Integer, Timetable_icons> icon){
        int[] orders = new int[icon.size()];
        int i = 0;
        for(int key : icon.keySet()){
            orders[i++] = key;
        }
        Arrays.sort(orders);
        return orders;
    }
}
