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

public class Register extends AppCompatActivity {
    EditText register_name,register_email,register_password_1,register_password_2;
    Button register_button;
    TextView swap_to_login_button;
    ProgressBar register_progress_bar;
    FirebaseAuth authorization;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_name = (EditText) findViewById(R.id.Name_surname_view);
        register_email = (EditText) findViewById(R.id.email_view);
        register_password_1 = (EditText) findViewById((R.id.password_first_view));
        register_password_2 = (EditText) findViewById((R.id.password_secound_view));
        register_button = (Button) findViewById(R.id.button);
        register_progress_bar = (ProgressBar) findViewById(R.id.progressBar_register);
        authorization = (FirebaseAuth) FirebaseAuth.getInstance();
        if(authorization.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = register_email.getText().toString().trim();
                String name = register_name.getText().toString().trim();
                String password_first = register_password_1.getText().toString().trim();
                String password_s = register_password_2.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    register_email.setError("musisz wpisać email");
                    return;
                }
                if(TextUtils.isEmpty(password_first)){
                    register_password_1.setError("musisz wpisać hasło");
                    return;
                }
                if(TextUtils.isEmpty(password_s)){
                    register_password_2.setError("musisz wpisać hasło");
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    register_name.setError("wpisz swoje imie i nazwisko");
                    return;
                }
                if(password_first.length() < 6){
                    register_password_1.setError("haslo musi miec iwecej niz 5 znaków");
                    return;
                }
                if(!password_first.equals(password_s)){
                    register_password_1.setError("haslo sa różne");
                    return;
                }
                register_progress_bar.setVisibility(View.VISIBLE);

                authorization.createUserWithEmailAndPassword(email,password_first).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Register.this, "Stworzono uzytkownika", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Nie udało się stworzyć użytkownika", Toast.LENGTH_SHORT).show();
                            register_progress_bar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });

    }
}