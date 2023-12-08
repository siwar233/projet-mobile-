package com.example.appel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class plat_ens extends AppCompatActivity {
    private TextView listmat,nommatiere;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plat_ens);
        listmat=findViewById(R.id.listmat);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String teacherId = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Teacher");
            DatabaseReference teacherNodeRef = databaseReference.child(teacherId);
            teacherNodeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        Boolean integrationWeb = dataSnapshot.hasChild("integrationWeb") ?
                                dataSnapshot.child("integrationWeb").getValue(Boolean.class) : false;
                        Boolean ia = dataSnapshot.hasChild("ia") ?
                                dataSnapshot.child("ia").getValue(Boolean.class) : false;
                        Boolean developmentMobile = dataSnapshot.hasChild("developmentMobile") ?
                                dataSnapshot.child("developmentMobile").getValue(Boolean.class) : false;
                        StringBuilder subjectsText = new StringBuilder();
                        if (integrationWeb!=null && integrationWeb) {
                            subjectsText.append("Integration Web\n");
                            listmat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Get the text value of the clicked TextView
                                    String clickedSubject = listmat.getText().toString();
                                    Log.d("clk", "onClick: "+clickedSubject);
                                    // Create an Intent
                                    Intent nextIntent;
                                    nextIntent = new Intent(plat_ens.this, tableau_integrationweb.class);
                                    // Add the clicked subject as an extra to the Intent
                                    nextIntent.putExtra("KEY_SUBJECT", clickedSubject);

                                    // Start the next activity
                                    startActivity(nextIntent);
                                    finish();
                                }
                            });

                        }
                        if (ia!=null && ia) {
                            subjectsText.append("IA\n");
                            listmat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String clickedSubject = listmat.getText().toString();
                                    Log.d("clk", "onClick: "+clickedSubject);
                                    // Create an Intent
                                    Intent nextIntent;
                                    nextIntent = new Intent(plat_ens.this, tableau_ia.class);
                                    // Add the clicked subject as an extra to the Intent
                                    nextIntent.putExtra("KEY_SUBJECT", clickedSubject);
                                    startActivity(nextIntent);
                                    finish();
                                }
                            });
                        }
                        if (developmentMobile!=null && developmentMobile) {
                            subjectsText.append("Development Mobile\n");
                            listmat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String clickedSubject = listmat.getText().toString();
                                    Log.d("clk", "onClick: "+clickedSubject);
                                    // Create an Intent
                                    Intent nextIntent;
                                    nextIntent = new Intent(plat_ens.this, tableau_devmob.class);
                                    // Add the clicked subject as an extra to the Intent
                                    nextIntent.putExtra("KEY_SUBJECT", clickedSubject);
                                    startActivity(nextIntent);
                                    finish();
                                }
                            });
                        }
                        listmat.setText(subjectsText.toString());
                    }
                    else
                    {
                        Log.d("TeacherData", "Le nœud Teacher avec l'ID spécifié n'existe pas.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}