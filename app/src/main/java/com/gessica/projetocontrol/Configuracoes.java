package com.gessica.projetocontrol;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Configuracoes extends AppCompatActivity {

    private EditText novaSenhaEditText, confirmarSenhaEditText;
    private Button bt_save;

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

            }
        });

        }
    }

    private void IniciarComponentes() {
        novaSenhaEditText = findViewById(R.id.novaSenhaEditText);
        confirmarSenhaEditText = findViewById(R.id.confirmarSenhaEditText);
        bt_save = findViewById(R.id.bt_save);
    }
}