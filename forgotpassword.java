package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
   private EditText mloginemail;
    private Button mrecover;
    private TextView mbackloginpage;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mloginemail=(EditText) findViewById(R.id.loginemail);
        mrecover =(Button) findViewById(R.id.recover);
        mbackloginpage=(TextView) findViewById(R.id.backloginpage);
        firebaseAuth=FirebaseAuth.getInstance();
        mbackloginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotpassword.this,MainActivity.class);
                startActivity(intent);
            }
        });
        mrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mloginemail.getText().toString().trim();
                if (email.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Your Registered Email",Toast.LENGTH_LONG).show();
                }else{
//                    //connect to firebase
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Mail Sent You can Recover Your Password using mail",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(forgotpassword.this,MainActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email is Wrong and Account Not Exist ",Toast.LENGTH_LONG).show();

                        }
                        }
                    });
                }
            }
        });
    }
}