package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText usernameField;
    private  EditText passwordField;
    private CardView loginbtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Startlogin();
            }

        });}
    private void Startlogin(){
        String email = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "PLEASE ENTER USERNAME OR PASSWORD", Toast.LENGTH_LONG).show();

        }
        else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "WRONG USERNAME OR PASSWORD", Toast.LENGTH_LONG).show();
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                    else{
                        usernameField.setText("");
                        passwordField.setText("");
                        startActivity(new Intent(MainActivity.this, userlocationactivity.class));

                    }

                }
            });

        }

    }
}
