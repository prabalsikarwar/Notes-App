package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Createnote extends AppCompatActivity {
   private  EditText mtitlenote , mcontenttext;
    private FloatingActionButton msave;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);
        mtitlenote = findViewById(R.id.titlenote);
        mcontenttext = findViewById(R.id.contenttext);
        msave = findViewById(R.id.save);
        Toolbar toolbar = findViewById(R.id.atoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mtitlenote.getText().toString();
                String content = mcontenttext.getText().toString();
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Both Field are Required", Toast.LENGTH_SHORT).show();

                } else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> mnote = new HashMap<>();
                    mnote.put("title", title);
                    mnote.put("content", content);
                    documentReference.set(mnote).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Note Created Succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Createnote.this, notesactivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to Create Note ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

}