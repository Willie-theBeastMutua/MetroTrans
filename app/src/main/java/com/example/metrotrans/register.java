package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class register extends AppCompatActivity {
    Spinner rolespinner;
    EditText Fname;
    EditText Lname;
    EditText Email;
    EditText pass;
    EditText Phone;
    CardView Signup;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rolespinner = findViewById(R.id.spinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(register.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roleSpinnerArray));
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rolespinner.setAdapter(roleAdapter);
        Fname = findViewById(R.id.fname);
        Lname = findViewById(R.id.lname);
        Email = findViewById(R.id.email);
        pass = findViewById(R.id.passwrd);
        Phone = findViewById(R.id.pnumber);
        Signup = findViewById(R.id.signUpBTn);
        mauth = FirebaseAuth.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //if (mauth.getCurrentUser() != null){
                //    Toast.makeText(register.this, "Email Address is registered",Toast.LENGTH_LONG).show();
                  //  Email.setText("");
                    //Email.setError("Enter new email");
                //}
                //else {
                    registerdetails();
                //}
            }
        });


    }



    private void registerdetails(){
       final String firstname = Fname.getText().toString().trim();
        final String lastname = Lname.getText().toString().trim();
        final String Emailname = Email.getText().toString().trim();
        final String phonenum = Phone.getText().toString().trim();
        final String password = pass.getText().toString().trim();
      final  String role = rolespinner.getSelectedItem().toString();
        if(firstname.isEmpty()){
            Fname.setError("First name is required");
            Fname.requestFocus();
            return;
        }
        if(lastname.isEmpty()){
            Lname.setError("Last name is required");
            Lname.requestFocus();
            return;
        }
        if(Emailname.isEmpty()){
            Email.setError("Email  is required");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Emailname).matches()){
            Email.setError("Enter a valid Email Address");
            Email.requestFocus();
            return;

        }
        if(password.isEmpty()){
            pass.setError("enter password");
            pass.requestFocus();
            return;
        }
        if(password.length() < 8){
            pass.setError("password should be atleast 8 characters long");
            pass.requestFocus();
            return;
        }
        if(phonenum.isEmpty()){
            Phone.setError("phone number is required");
            Phone.requestFocus();
            return;
        }
        if (phonenum.length() != 10){
            Phone.setError("Enter valid phone number");
            Phone.requestFocus();
            return;
        }

        mauth.createUserWithEmailAndPassword(Emailname, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){

                        if(role.equals("Commuter")){
                            commreg comm = new commreg(
                                    firstname,
                             lastname,
                             Emailname,
                             phonenum,
                            role
                            );
                            FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(comm)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(register.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(register.this, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(register.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(register.this, register.class));

                                    }
                                }
                            });

                        }
                       if(role.equals("Driver")){
                           driverreg driver = new driverreg(
                                   firstname,
                                   lastname,
                                   Emailname,
                                   phonenum,
                                   role
                           );
                           FirebaseDatabase.getInstance().getReference()
                                   .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(driver)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Toast.makeText(register.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                       startActivity(new Intent(register.this, MainActivity.class));
                                   }
                                   else{
                                       Toast.makeText(register.this, "Registration failed", Toast.LENGTH_LONG).show();
                                       startActivity(new Intent(register.this, register.class));

                                   }
                               }
                           });
                       }
                   }
                   else{
                       Toast.makeText(register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                   }
                }
            });



    }
}
