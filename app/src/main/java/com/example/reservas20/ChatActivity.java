package com.example.reservas20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private FirebaseFirestore db;
    private String currentUserId, chatPartnerId;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Inicializar vistas
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        ImageButton sendButton = findViewById(R.id.sendButton);

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Verificar si el usuario está autenticado
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.e("ChatActivity", "El usuario no está autenticado.");
            finish(); // Cierra la actividad si no hay usuario autenticado
            return;
        }
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtener chatPartnerId desde el Intent
        chatPartnerId = getIntent().getStringExtra("chatPartnerId");
        if (chatPartnerId == null) {
            Log.e("ChatActivity", "No se ha proporcionado chatPartnerId.");
            finish(); // Cierra la actividad si no se ha pasado un chatPartnerId válido
            return;
        }

        // Inicializar adaptador y RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId);

        // Configurar LayoutManager
        linearLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(messageAdapter);

        // Cargar mensajes
        loadMessages();

        // Acción al hacer clic en el botón de enviar mensaje
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        db.collection("chats").document(getChatId())
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("ChatActivity", "Error al cargar los mensajes", e);
                        return;
                    }

                    // Limpiar la lista y agregar los nuevos mensajes
                    messageList.clear();
                    for (DocumentSnapshot snapshot : snapshots) {
                        Message message = snapshot.toObject(Message.class);
                        messageList.add(message);
                    }

                    // Notificar al adaptador que los datos han cambiado
                    messageAdapter.notifyDataSetChanged();

                    // Desplazar el RecyclerView al último mensaje
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            Message message = new Message(messageText, currentUserId, System.currentTimeMillis());

            db.collection("chats").document(getChatId())
                    .collection("messages")
                    .add(message)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("ChatActivity", "Mensaje enviado con éxito");

                        // Desplazar el RecyclerView al último mensaje cuando se envía uno nuevo
                        chatRecyclerView.scrollToPosition(messageList.size() - 1);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ChatActivity", "Error al enviar el mensaje", e);
                    });

            // Limpiar el campo de texto
            messageEditText.setText("");
        }
    }

    private String getChatId() {
        return currentUserId.compareTo(chatPartnerId) < 0 ?
                currentUserId + "" + chatPartnerId : chatPartnerId + "" + currentUserId;
    }
}
