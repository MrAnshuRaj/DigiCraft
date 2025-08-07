package com.anshu.digicraft;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> messageList;
    private static final String URL = "https://notification.digicraft.one/api/external/get-notifications";
    private static final String API_KEY = "414930a3b0d878b8c8b63a3de3368060a59e78a5344d409b1e090e396764dc82";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        messageList = new ArrayList<>();

        adapter = new NotificationAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchNotifications();
        IntentFilter filter = new IntentFilter("fcmmessage");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String m = intent.getStringExtra("message");

            }
        }, filter, Context.RECEIVER_NOT_EXPORTED);


        // Get FCM registration token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "FCM Token: " + token);

                });
       floatingActionButton =findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> copyFcmTokenToClipboard());
        // Display last message if received

    }
    public void copyFcmTokenToClipboard() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Failed to get FCM token", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String token = task.getResult();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("FCM Token", token);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(this, "FCM token copied to clipboard", Toast.LENGTH_LONG).show();
                    Log.d("FCM_TOKEN", "Copied Token: " + token);
                });
    }
    private void fetchNotifications() {
        String url = URL; // Replace with your real one

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (!success) {
                            Toast.makeText(this, "Server is down!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray notificationsArray = response.getJSONArray("notifications");
                        List<NotificationModel> notificationList = new ArrayList<>();

                        for (int i = 0; i < notificationsArray.length(); i++) {
                            JSONObject obj = notificationsArray.getJSONObject(i);

                            String title = obj.getString("title");
                            String body = obj.getString("body");
                            String sender = obj.optString("sender", "Unknown");
                            String createdAt = obj.optString("createdAt", "");
                            Map<String, String> data = new HashMap<>();
                            JSONObject dataObj = obj.getJSONObject("data");
                            Iterator<String> keys = dataObj.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = dataObj.getString(key);
                                data.put(key, value);
                            }

                            notificationList.add(new NotificationModel(title, body, sender, createdAt,data ));
                        }

                        adapter = new NotificationAdapter(notificationList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch notifications", Toast.LENGTH_SHORT).show()
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", API_KEY);
                return headers;
            }
        };

        queue.add(request);
    }





}