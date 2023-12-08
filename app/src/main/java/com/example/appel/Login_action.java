package com.example.appel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_action extends AppCompatActivity {
    private EditText editTextLoginEmail,editTextLoginPassword;
    Button login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_action);
        Toast.makeText(Login_action.this,"You can login now",Toast.LENGTH_LONG).show();
        editTextLoginEmail=findViewById(R.id.editText_login_email);
        editTextLoginPassword=findViewById(R.id.editText_login_password);
        login=findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=editTextLoginEmail.getText().toString();
                String Pass=editTextLoginPassword.getText().toString();
                if(TextUtils.isEmpty(Email))
                {
                    Toast.makeText(Login_action.this,"Please Enter your email",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    Toast.makeText(Login_action.this,"Please re-enter email",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("verify your email");
                    editTextLoginEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(Pass))
                {
                    Toast.makeText(Login_action.this,"Please Enter your password",Toast.LENGTH_LONG).show();
                    editTextLoginPassword.setError("pass is required");
                    editTextLoginPassword.requestFocus();
                }
                else
                {
                    LoginUser(Email,Pass);
                }
            }
        });
    }

    public void LoginUser(String Email, String Pass) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login_action.this, "User Logged in", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    if (firebaseUser != null) {
                        String userId = firebaseUser.getUid();

                        // Check if the user is an "utilisateur"
                        DatabaseReference utilisateurRef = FirebaseDatabase.getInstance().getReference("utilisateurs").child(userId);
                        utilisateurRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    User utilisateur = dataSnapshot.getValue(User.class);
                                    Intent intent = new Intent(Login_action.this, plat_user.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // User is not an "utilisateur", check if the user is a "teacher"
                                    DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("Teacher").child(userId);
                                    teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot teacherSnapshot) {
                                            if (teacherSnapshot.exists()) {
                                                // User is a "teacher"
                                                Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                                                Intent intent = new Intent(Login_action.this, plat_ens.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle errors
                                            Log.e("Database", "Error fetching teacher data", databaseError.toException());
                                        }
                                    });
                                }
                            }



                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle errors
                                Log.e("Database", "Error fetching utilisateur data", databaseError.toException());
                            }
                        });
                    } else {
                        Log.e("Authentication", "FirebaseUser is null");
                    }
                }
            }
        });
    }

}