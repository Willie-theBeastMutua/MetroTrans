package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.logging.Logger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText usernameField;
    private  EditText passwordField;
    private CardView loginbtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView signup;
    DatabaseReference reference;

    private String rolecom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginbtn = findViewById(R.id.login);
        signup = findViewById(R.id.textSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });


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
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child(mAuth.getCurrentUser().getUid());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 rolecom = dataSnapshot.child("roles").getValue().toString();
                                if(rolecom.equals("Commuter")){
                                    usernameField.setText("");
                                    passwordField.setText("");
                                    startActivity(new Intent(MainActivity.this, userlocationactivity.class));
                                }
                                if(rolecom.equals("Driver")){
                                    usernameField.setText("");
                                    passwordField.setText("");
                                    startActivity(new Intent(MainActivity.this, driverlocation.class));

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                      }
}


            });

        }

    }
}
