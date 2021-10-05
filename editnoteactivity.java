package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {
    Intent data ;
private EditText medittitlenote , meditcontenttext;
private FloatingActionButton floatingActionButton;

FirebaseUser firebaseUser;
FirebaseFirestore firebaseFirestore;
FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);
        data =  getIntent();
        medittitlenote=findViewById(R.id.edittitlenote);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        meditcontenttext=findViewById(R.id.editcontenttext);
        floatingActionButton=findViewById(R.id.saveeditnote);
        Toolbar toolbar = findViewById(R.id.toolbarodeditnote);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String notetitle=data.getStringExtra("title");
        String noteContent=data.getStringExtra("content");
        meditcontenttext.setText(noteContent);
        medittitlenote.setText(notetitle);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newtitle = medittitlenote.getText().toString();
                String newcontent=meditcontenttext.getText().toString();

                if (newtitle.isEmpty()||newcontent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Something is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    {
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String ,Object> note= new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void avoid) {
                            Toast.makeText(getApplicationContext(),"Note is Updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,notesactivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
//                Toast.makeText(getApplicationContext(), "savebutton click", Toast.LENGTH_SHORT).show();
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