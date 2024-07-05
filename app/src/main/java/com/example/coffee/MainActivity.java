package com.example.coffee;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TextView textCadastrar;
    private EditText editEmail, editSenha;
    private Button entrar;
    private ProgressBar progressbar;
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
        InicializaComponentes();

        textCadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormCadastro.class);
            startActivity(intent);
        });

            entrar.setOnClickListener(v -> {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else {
                    AutenticarUsuario();
                }

            });
    }
    private void AutenticarUsuario(){
        String senha = editSenha.getText().toString();
        String email = editEmail.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressbar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TelaPrincipal();
                    }
                },3000);
            }
        });

    }
    private void TelaPrincipal(){
        Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }
    private void InicializaComponentes(){
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        entrar = findViewById(R.id.buttonEntrar);
        progressbar = findViewById(R.id.progressBar);
        textCadastrar = findViewById(R.id.text_tela_cadastro);
    }
}