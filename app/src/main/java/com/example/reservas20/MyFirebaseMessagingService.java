package com.example.reservas20;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.reservas20.ChatActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Verificar si el mensaje tiene una notificación asociada
        if (remoteMessage.getNotification() != null) {
            // Llamar al método para enviar la notificación
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        // Crear un intent para abrir la actividad del chat cuando se toque la notificación
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // PendingIntent para lanzar la actividad de Chat
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_IMMUTABLE);

        // Crear un canal de notificación si la versión de Android es 8.0 o superior
        String channelId = "chat_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Chat Notifications";
            String channelDescription = "Notifications for new chat messages";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            // Crear el canal de notificación
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Construcción de la notificación
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Icono predeterminado de Android
                .setContentTitle("Nuevo mensaje")                 // Título de la notificación
                .setContentText(messageBody)                      // Texto del cuerpo de la notificación
                .setAutoCancel(true)                              // Cerrar notificación al hacer clic
                .setPriority(NotificationCompat.PRIORITY_HIGH)    // Prioridad alta para asegurar que la notificación sea visible inmediatamente
                .setContentIntent(pendingIntent);                 // Intent para abrir la actividad de chat

        // Obtener el servicio de notificación del sistema
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Publicar la notificación con un ID único
        notificationManager.notify(0, notificationBuilder.build());
    }
}