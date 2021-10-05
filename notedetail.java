package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class notedetail extends AppCompatActivity {
    private TextView mtitlenotedetail , mcontenttextofdetail;
    FloatingActionButton detailfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        mtitlenotedetail=findViewById(R.id.textView2);
        mcontenttextofdetail=findViewById(R.id.contenttextofdetail);
        detailfab=findViewById(R.id.gotoedit);
        Toolbar toolbar = findViewById(R.id.toolbarofnotedetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data =  new Intent();
        detailfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(view.getContext(),editnoteactivity.class);
               intent.putExtra("title",data.getStringExtra("title"));
               intent.putExtra("content",data.getStringExtra("content"));
               intent.putExtra("noteId",data.getStringExtra("noteId"));
               view.getContext().startActivity(intent);
            }
        });
        mcontenttextofdetail.setText(data.getStringExtra("content"));
        mtitlenotedetail.setText(data.getStringExtra("title"));

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}