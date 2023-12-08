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
    private TextView listmat, nommatiere;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plat_ens);
        listmat = findViewById(R.id.listmat);
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
                        StringBuilder subjectsText = new StringBuilder();

                        if (dataSnapshot.hasChild("integrationWeb") && dataSnapshot.child("integrationWeb").getValue(Boolean.class)) {
                            subjectsText.append("Integration Web\n");
                            setListMatOnClickListener("Integration Web", tableau_integrationweb.class);
                        }

                        if (dataSnapshot.hasChild("ia") && dataSnapshot.child("ia").getValue(Boolean.class)) {
                            subjectsText.append("IA\n");
                            setListMatOnClickListener("IA", tableau_ia.class);
                        }

                        if (dataSnapshot.hasChild("developmentMobile") && dataSnapshot.child("developmentMobile").getValue(Boolean.class)) {
                            subjectsText.append("Development Mobile\n");
                            setListMatOnClickListener("Development Mobile", tableau_devmob.class);
                        }

                        listmat.setText(subjectsText.toString());
                    } else {
                        Log.d("TeacherData", "Le nœud Teacher avec l'ID spécifié n'existe pas.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setListMatOnClickListener(String subject, Class<?> destinationClass) {
        listmat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clk", "onClick: " + subject);

                // Create an Intent
                Intent nextIntent = new Intent(plat_ens.this, destinationClass);

                // Add the clicked subject as an extra to the Intent
                nextIntent.putExtra("KEY_SUBJECT", subject);

                // Start the next activity
                startActivity(nextIntent);
                finish();
            }
        });
    }
}