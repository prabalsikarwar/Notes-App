package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private EditText mloginmail, mpassword;
    private RelativeLayout mloginbutton;
    private RelativeLayout msignup;
    private TextView mforgot;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mloginmail = (EditText) findViewById(R.id.loginemail);
        mforgot = (TextView) findViewById(R.id.forgot);
        mpassword = (EditText) findViewById(R.id.editTextTextPassword);
        progressBar=findViewById(R.id.progressbarofmainactivity);
        mloginbutton = (RelativeLayout) findViewById(R.id.loginbutton);
        msignup = (RelativeLayout) findViewById(R.id.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,notesactivity.class));

        }

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });
        mforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, forgotpassword.class);
                startActivity(intent);
            }
        });


        mloginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mloginmail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All Fields Are Required ", Toast.LENGTH_LONG).show();

                } else if (password.length() < 7) {
                    Toast.makeText(getApplicationContext(), "Password must be Greater than 7 Digit", Toast.LENGTH_LONG).show();


                } else {

                 progressBar.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkmailverification();

                            } else {
                                Toast.makeText(getApplicationContext(), "Account Does Not Exist", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
                }
            }
        });


    }

private void  checkmailverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesactivity.class));
        }else{
           progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify Your Email First", Toast.LENGTH_LONG).show();
          firebaseAuth.signOut();
        }
}
}

