package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText login_email,login_passowrd;
    Button login_button;
    TextView login_to_register,rule_login;
    FirebaseAuth authorization_login;
    ProgressBar progressBar_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_email = findViewById(R.id.email_login);
        login_passowrd = findViewById(R.id.password_login);
        login_button = findViewById(R.id.login_to_app);
        login_to_register = findViewById(R.id.to_register);
        rule_login = findViewById(R.id.accept_reg_check);
        progressBar_login = findViewById(R.id.progressBar);

        authorization_login = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String password_login = login_passowrd.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    login_email.setError("musisz wpisać email");
                    return;
                }
                if(TextUtils.isEmpty(password_login)){
                    login_passowrd.setError("musisz wpisać hasło");
                    return;
                }
                progressBar_login.setVisibility(View.VISIBLE);

                authorization_login.signInWithEmailAndPassword(email,password_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Zalogowano się poprawnie", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(Login.this, "złe hasło lub login", Toast.LENGTH_SHORT).show();
                            progressBar_login.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });

        login_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        rule_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Rule.class));
            }
        });
    }
}