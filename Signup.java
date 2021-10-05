package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Signup extends AppCompatActivity {
   private  EditText msignupemail;
   private EditText msignuppassword;
   private RelativeLayout msignupButton;
   private TextView mloginAgain;
private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        msignupemail=(EditText) findViewById(R.id.signupemail);
        msignuppassword=(EditText) findViewById(R.id.signuppassword);
      msignupButton= (RelativeLayout) findViewById(R.id.signupButton);
      mloginAgain=(TextView) findViewById(R.id.loginAgain);
      firebaseAuth=FirebaseAuth.getInstance();
      mloginAgain.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent (Signup.this,MainActivity.class);
              startActivity(intent);
          }
      });
      msignupButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String email=msignupemail.getText().toString();
              String password=msignuppassword.getText().toString();
              if (email.isEmpty()||password.isEmpty()){
                  Toast.makeText(getApplicationContext(),"Fill all Details Correctly",Toast.LENGTH_LONG).show();
              }else if(password.length()<7){
                  Toast.makeText(getApplicationContext(),"Password must be Greater than 7 Digits",Toast.LENGTH_LONG).show();
              }else{
                  firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         Toast.makeText(getApplicationContext(),"Registration is Successfully Done ",Toast.LENGTH_LONG).show();
                         sendEmailVerification();
                     }else
                     {
                         Toast.makeText(getApplicationContext(),"Failed To Register ",Toast.LENGTH_LONG).show();

                     }
                      }
                  });
              }
          }
      });


    }
// email verificaton
    private void  sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verifiaction Email Send Succesfully and Log In Again  ",Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Signup.this,MainActivity.class));


                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Failed To Send Verification ",Toast.LENGTH_LONG).show();
        }
    }
}