package com.example.appel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class tableau_devmob extends AppCompatActivity {
    DatabaseReference usersReference;
    LinearLayout usersLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_devmob);
        // Initialize DatabaseReference and LinearLayout
        usersReference = FirebaseDatabase.getInstance().getReference().child("utilisateurs");
        usersLayout = findViewById(R.id.users_layout);
        Log.d("layout", "onCreate: " + usersLayout);

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersLayout.removeAllViews();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    Log.d("email", "onDataChange:" + email);

                    View userLayout = getLayoutInflater().inflate(R.layout.user_item_layout, usersLayout, false);

                    Log.d("layouuuuuuut", "onDataChange: " + userLayout);

                    TextView emailTextView = userLayout.findViewById(R.id.user_email);
                    Button increment = userLayout.findViewById(R.id.increment_btn);
                    Button decrement = userLayout.findViewById(R.id.decrement_btn);
                    TextView display = userLayout.findViewById(R.id.tx_display);
                    emailTextView.setText(email);

                    increment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int count = Integer.parseInt(display.getText().toString());
                            count++;
                            display.setText(String.valueOf(count));
                        }
                    });

                    decrement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int count = Integer.parseInt(display.getText().toString());
                            if (count > 0) {
                                count--;
                                display.setText(String.valueOf(count));
                            }
                        }
                    });

                    // Add the user layout to the main layout
                    usersLayout.addView(userLayout);

                }

                Button save = findViewById(R.id.button_save_abs);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Absence");

                        // Iterate through child views to gather data for each user
                        for (int i = 0; i < usersLayout.getChildCount(); i++) {
                            View userLayout = usersLayout.getChildAt(i);
                            TextView emailTextView = userLayout.findViewById(R.id.user_email);
                            TextView display = userLayout.findViewById(R.id.tx_display);
                            Intent intent = getIntent();
                            if (intent != null && intent.hasExtra("KEY_SUBJECT")) {
                                String emailValue = emailTextView.getText().toString();
                                String displayValue = display.getText().toString();
                                String matiere = intent.getStringExtra("KEY_SUBJECT");
                                Absence absence = new Absence(emailValue, displayValue, matiere);
                                databaseReference.push().setValue(absence).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(tableau_devmob.this,"Absence saved",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }


                            // Extract email and display values


                            // Save to the database

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("erreur", "onCancelled: ");
            }
        });
    }
}