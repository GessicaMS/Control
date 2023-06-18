package com.gessica.projetocontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;


public class Configuracoes extends AppCompatActivity {
    private EditText novaSenhaEditText, confirmarSenhaEditText;
    private Button bt_save, excluirContaButton;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        getSupportActionBar().hide();
        IniciarComponentes();

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String novaSenha = novaSenhaEditText.getText().toString();
                String confirmarSenha = confirmarSenhaEditText.getText().toString();

                if (verificarSenhaValida(novaSenha) && novaSenha.equals(confirmarSenha)) {
                    atualizarSenha(novaSenha);
                } else {
                    Toast.makeText(getApplicationContext(), "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        excluirContaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Configuracoes.this);
                builder.setTitle("Confirmar exclusão da conta");
                builder.setMessage("Tem certeza de que deseja excluir a conta? Esta ação não poderá ser desfeita.");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        excluirConta();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void excluirConta() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            excluirContaFirebaseAuthentication(user, userId);
            excluirContaFirestore(userId);
        }
    }

    private void excluirContaFirebaseAuthentication(FirebaseUser user, String userId) {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Conta excluída com sucesso", Toast.LENGTH_SHORT).show();
                            backLogin();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erro ao excluir a conta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void excluirContaFirestore(String userId) {
        database.collection("Usuarios").document(userId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Erro ao excluir a conta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean verificarSenhaValida(String senha) {
        return senha.length() == 6 && senha.matches("\\d+");
    }

    private void atualizarSenha(String novaSenha) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(novaSenha)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Senha atualizada com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Erro ao atualizar a senha", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void backLogin() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Configuracoes.this, FormLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes() {
        novaSenhaEditText = findViewById(R.id.novaSenhaEditText);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenhaEditText);
        bt_save = findViewById(R.id.bt_save);
        excluirContaButton = findViewById(R.id.excluirContaButton);
    }
}
