package com.example.shop_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText register_name,register_email,register_password_1,register_password_2;
    Button register_button;
    TextView swap_to_login_button,rule_login ;
    CheckBox accept_rule;
    ProgressBar register_progress_bar;
    FirebaseAuth authorization;
    FirebaseFirestore store;
    String userId;
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
        accept_rule = (CheckBox) findViewById(R.id.accept_reg_check);
        rule_login = findViewById(R.id.Rule_view);
        swap_to_login_button = findViewById(R.id.login_view);
        authorization = (FirebaseAuth) FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        if(authorization.getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
                if(!accept_rule.isChecked()){
                    accept_rule.setError("Aby stworzyć konto trzeba zaakceptować regulamin");
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
                            userId = authorization.getCurrentUser().getUid();
                            DocumentReference create_colection = store.collection("users").document(userId).collection("note").document("pierwsza_lista");
                            Map<String,Object> note = new HashMap<>();
                            note.put("1","cukier ");
                            create_colection.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Nie udało się stworzyć użytkownika", Toast.LENGTH_SHORT).show();
                            register_progress_bar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            }
        });
        swap_to_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
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