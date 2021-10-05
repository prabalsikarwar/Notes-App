package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesactivity extends AppCompatActivity {

FloatingActionButton mfab;
private FirebaseAuth firebaseAuth;
StaggeredGridLayoutManager staggeredGridLayoutManager;
FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;
FirebaseUser firebaseUser;
FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);
        mfab=findViewById(R.id.fab);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Notes");
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(notesactivity.this,Createnote.class));

            }
        });
        Query query= firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel>allnotes=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allnotes) {

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noteslayout,parent,false);
                return new NoteViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull firebasemodel model) {
                ImageView  popmenu= holder.itemView.findViewById(R.id.popmenu);
                int colorcode=getRandomcolor();

                holder.linearLayout.setBackgroundColor(holder.itemView.getResources().getColor(colorcode,null));
                holder.notetitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());
                String docId= noteAdapter.getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                  Intent intent = new Intent(view.getContext(),notedetail.class);
                  intent.putExtra("title",model.getTitle());
                  intent.putExtra("content",model.getContent());
                  intent.putExtra("noteId",docId);
                  view.getContext().startActivity(intent);
                  Toast.makeText(getApplicationContext(), "This is Clicked", Toast.LENGTH_SHORT).show();


                    }
                });
                popmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popmenu = new PopupMenu(view.getContext(),view);
                        popmenu.setGravity(Gravity.END);
                        popmenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                              Intent intent = new Intent(view.getContext(),editnoteactivity.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteId",docId);
                              view.getContext().startActivity(intent);
                                return false;
                            }
                        });
                        popmenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                               DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                               documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(getApplicationContext(), "This Note is Deleted", Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(getApplicationContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                   }
                               });
                                return false;
                            }
                        });
                        popmenu.show();
                    }
                });
            }
        };
        RecyclerView recyclerView= findViewById(R.id.rectangles);
        recyclerView.setHasFixedSize(true);
      staggeredGridLayoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
      recyclerView.setLayoutManager(staggeredGridLayoutManager);
      recyclerView.setAdapter(noteAdapter);


    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView notetitle;
        private TextView noteContent;
        LinearLayout linearLayout;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle=itemView.findViewById(R.id.notetitle);
            noteContent=itemView.findViewById(R.id.notescontent);
            linearLayout=itemView.findViewById(R.id.note);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menufile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();finish();
                startActivity(new Intent(notesactivity.this,MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter!=null){
            noteAdapter.startListening();
        }
    }
       private int  getRandomcolor(){
        List<Integer> colorcode= new ArrayList<>();
        colorcode.add(R.color.orange);
        colorcode.add(R.color.cyan);
    colorcode.add(R.color.IndianRed);
    colorcode.add(R.color.lightblue);
    colorcode.add(R.color.blue);
    colorcode.add(R.color.yellow);
    colorcode.add(R.color.pink);
    colorcode.add(R.color.grey);
 Random random = new Random();
 int number = random.nextInt(colorcode.size());
 return colorcode.get(number);




    }
}
