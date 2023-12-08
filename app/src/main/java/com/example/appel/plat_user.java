package com.example.appel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class plat_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plat_user);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        DatabaseReference absenceReference = FirebaseDatabase.getInstance().getReference().child("Absence");
        LinearLayout usersLayout = findViewById(R.id.users_layout);

        if (currentUser != null) {
            absenceReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot absenceSnapshot : dataSnapshot.getChildren()) {
                        String email = absenceSnapshot.child("email").getValue(String.class);
                        if (Objects.equals(email, currentUser.getEmail())) {
                            // Inflate layout for each record
                            View userLayout = getLayoutInflater().inflate(R.layout.user_matiere_layout, usersLayout, false);
                            String display = absenceSnapshot.child("display").getValue(String.class);
                            String matiere = absenceSnapshot.child("matiere").getValue(String.class);
                            TextView nommatiere = userLayout.findViewById(R.id.nom_matiere);
                            TextView nbrabs = userLayout.findViewById(R.id.nbr_abs);
                            TextView etat=userLayout.findViewById(R.id.status);
                            nommatiere.setText(matiere);
                            nbrabs.setText(display);
                            int displayValue=Integer.parseInt(display);
                            if(displayValue > 3)

                                etat.setText("eliminé");
                            else
                                etat.setText("non eliminé");

                            // Add the user layout to the main layout
                            usersLayout.addView(userLayout);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Erreur", "onCancelled: "+ " matiere");
                }
            });
        }
    }

}