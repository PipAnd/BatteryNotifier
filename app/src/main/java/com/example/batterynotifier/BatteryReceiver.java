package com.example.batterynotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BatteryReceiver extends BroadcastReceiver {
    
    private static final String CHANNEL_ID = "battery_channel";
    private static int notificationId = 2;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;
            
            SharedPreferences prefs = context.getSharedPreferences("battery_settings", Context.MODE_PRIVATE);
            int maxLevel = prefs.getInt("max_level", 90);
            int minLevel = prefs.getInt("min_level", 20);
            
            // Проверяем статус зарядки
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                               status == BatteryManager.BATTERY_STATUS_FULL;
            
            if (isCharging && batteryPct >= maxLevel) {
                sendNotification(context, "Батарея заряжена до " + (int)batteryPct + "%");
            } else if (!isCharging && batteryPct <= minLevel) {
                sendNotification(context, "Батарея разряжена до " + (int)batteryPct + "%");
            }
        }
    }
    
    private void sendNotification(Context context, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Уровень заряда батареи")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(notificationId++, builder.build());
        } catch (SecurityException e) {
            // Обработка ошибки разрешений
        }
    }
}
