package com.example.coffee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPrincipal extends AppCompatActivity {
    private TextView textNameUser, textEmailUser;
    private Button buttonDeslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();
        buttonDeslogar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TelaPrincipal.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        super.onStart();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null){
                    textNameUser.setText(documentSnapshot.getString("nome"));
                    textEmailUser.setText(email);
                }
            }
        });
    }

    private void IniciarComponentes(){
        textEmailUser = findViewById(R.id.textEmailUser);
        textNameUser = findViewById(R.id.textNameUser);
        buttonDeslogar = findViewById(R.id.buttonDeslogar);
    }
}