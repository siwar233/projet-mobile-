package com.example.appel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_action extends AppCompatActivity {

    private EditText editTextRegisterFullName,editTextRegisterEmail,editTextRegisterDoB,
            editTextRegisterMobile,editTextRegisterPwd,editTextRegisterConfirmPwd;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_action);
        Toast.makeText(Register_action.this,"You can register now",Toast.LENGTH_LONG).show();


        editTextRegisterFullName=findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail=findViewById(R.id.editText_register_email);
        editTextRegisterDoB=findViewById(R.id.editText_register_dob);
        editTextRegisterMobile=findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd=findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd=findViewById(R.id.editText_register_confirm_password);

        radioGroupRegisterGender=findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();
        TextView account = findViewById(R.id.haveaccountuser);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register_action.this,Login_action.class);
                startActivity(i);
                finish();
            }
        });

        Button buttonRegister=findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId=radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected=findViewById(selectedGenderId);

                String textFullName=editTextRegisterFullName.getText().toString();
                String textEmail=editTextRegisterEmail.getText().toString();
                String textDoB=editTextRegisterDoB.getText().toString();
                String textMobile=editTextRegisterMobile.getText().toString();
                String textPwd=editTextRegisterPwd.getText().toString();
                String textConfirmPwd=editTextRegisterConfirmPwd.getText().toString();
                String textGender;

                if(TextUtils.isEmpty(textFullName))
                {
                    Toast.makeText(Register_action.this,"Please Enter your full name",Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is required");
                    editTextRegisterFullName.requestFocus();
                }
                else if(TextUtils.isEmpty(textEmail))
                {
                    Toast.makeText(Register_action.this,"Please Enter your email",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches())
                {
                    Toast.makeText(Register_action.this,"Please re-enter email",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("verify your email");
                    editTextRegisterEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(textDoB))
                {
                    Toast.makeText(Register_action.this,"Please Enter your Date",Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Date of birth is required");
                    editTextRegisterDoB.requestFocus();
                }
                else if(radioGroupRegisterGender.getCheckedRadioButtonId()== -1)
                {
                    Toast.makeText(Register_action.this,"Please check gender",Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Check gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                }
                else if(TextUtils.isEmpty(textMobile))
                {
                    Toast.makeText(Register_action.this,"Please Enter your mobile",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile number is required");
                    editTextRegisterMobile.requestFocus();
                }
                else if(textMobile.length()!=8)
                {
                    Toast.makeText(Register_action.this,"Please re-enter your mobile",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError(" verif your mobile number");
                    editTextRegisterMobile.requestFocus();
                }
                else if(TextUtils.isEmpty(textPwd))
                {
                    Toast.makeText(Register_action.this,"Please Enter your password",Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("pass is required");
                    editTextRegisterPwd.requestFocus();
                }
                else if(textPwd.length()<6)
                {
                    Toast.makeText(Register_action.this,"Please re-enter your password",Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("password too weak");
                    editTextRegisterPwd.requestFocus();
                }
                else if (TextUtils.isEmpty(textConfirmPwd))
                {
                    Toast.makeText(Register_action.this,"Please Enter your confirmpass",Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("confirm pass is required");
                    editTextRegisterConfirmPwd.requestFocus();
                }
                else if (!textPwd.equals(textConfirmPwd))
                {
                    Toast.makeText(Register_action.this,"Please confirm your pass",Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("confirm pass is required");
                    editTextRegisterConfirmPwd.requestFocus();
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }
                else
                {
                    textGender=radioButtonRegisterGenderSelected.getText().toString();
                    registerUser(textFullName,textEmail,textDoB,textGender,textMobile,textPwd);

                }
            }
        });
    }

    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwd)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("utilisateurs");
        auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(Register_action.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Register_action.this,"User Created",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    if(firebaseUser !=null)
                    {
                        String userId = firebaseUser.getUid();
                        User user = new User(textFullName, textEmail, textDoB, textGender, textMobile);
                        DatabaseReference userRef = databaseReference.child(userId);
                        userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> databaseTask)
                            {
                                if (databaseTask.isSuccessful()) {
                                    Log.d("Database", "User data saved successfully");
                                } else {
                                    Log.e("Database", "Error saving user data to database", databaseTask.getException());
                                }
                            }
                        });
                        Intent intent = new Intent(Register_action.this, Login_action.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Log.e("Authentication", "FirebaseUser is null");
                    }
                }
                else
                {
                    Log.e("Authentication", "Error creating user", task.getException());

                }

            }
        });
    }
}