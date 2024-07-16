package com.example.coffee;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {
    private EditText EditTextNome, EditTextSenha, EditTextEmail;

    private Button buttonCadastrar;
    String[] mensagens= {"Preencha todos os campos!", "Cadastro efetuado com sucesso"};
    String usuarioID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();
        buttonCadastrar.setOnClickListener(v -> {
            String nome = EditTextNome.getText().toString().trim();
            String email = EditTextEmail.getText().toString().trim();
            String senha = EditTextSenha.getText().toString().trim();

            if (nome.isEmpty() || senha.isEmpty() || email.isEmpty()){
                Toast.makeText(this,mensagens[0], Toast.LENGTH_SHORT).show();
            }else {
                CadastroUsuario();
            }
        });
    }
    private void CadastroUsuario(){
        String email = EditTextEmail.getText().toString().trim();
        String senha = EditTextSenha.getText().toString().trim();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    SalvarDadosUsuario();
                    Toast.makeText(FormCadastro.this, mensagens[1], Toast.LENGTH_SHORT).show();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha com no minimo 6 numeros";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Esse email já foi cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail Inválido";
                    }
                    catch (Exception e){
                        erro = "Erro ao cadastrar";
                    }
                    Toast.makeText(FormCadastro.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void SalvarDadosUsuario(){
        String nome = EditTextNome.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os Dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error","Erro ao salvar os Dados " + e.toString());
            }
        });
    }
    private void IniciarComponentes(){
        EditTextEmail = findViewById(R.id.EditTextEmail);
        EditTextSenha = findViewById(R.id.EditTextSenha);
        EditTextNome = findViewById(R.id.editTextNome);
        buttonCadastrar = findViewById(R.id.btCadastrar);

    }
}