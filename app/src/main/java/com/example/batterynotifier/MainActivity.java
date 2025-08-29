package com.example.batterynotifier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    
    private EditText etMaxLevel, etMinLevel;
    private Button btnStart, btnStop;
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        initPreferences();
        loadSettings();
        setupClickListeners();
    }
    
    private void initViews() {
        etMaxLevel = findViewById(R.id.etMaxLevel);
        etMinLevel = findViewById(R.id.etMinLevel);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
    }
    
    private void initPreferences() {
        prefs = getSharedPreferences("battery_settings", MODE_PRIVATE);
    }
    
    private void loadSettings() {
        int maxLevel = prefs.getInt("max_level", 90);
        int minLevel = prefs.getInt("min_level", 20);
        etMaxLevel.setText(String.valueOf(maxLevel));
        etMinLevel.setText(String.valueOf(minLevel));
    }
    
    private void setupClickListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                startBatteryService();
                Toast.makeText(MainActivity.this, "Мониторинг запущен", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopBatteryService();
                Toast.makeText(MainActivity.this, "Мониторинг остановлен", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void saveSettings() {
        int maxLevel = Integer.parseInt(etMaxLevel.getText().toString().isEmpty() ? "90" : etMaxLevel.getText().toString());
        int minLevel = Integer.parseInt(etMinLevel.getText().toString().isEmpty() ? "20" : etMinLevel.getText().toString());
        
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("max_level", maxLevel);
        editor.putInt("min_level", minLevel);
        editor.apply();
    }
    
    private void startBatteryService() {
        Intent serviceIntent = new Intent(this, BatteryService.class);
        startService(serviceIntent);
    }
    
    private void stopBatteryService() {
        Intent serviceIntent = new Intent(this, BatteryService.class);
        stopService(serviceIntent);
    }
}
