package com.example.appel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_ens extends AppCompatActivity {

    private EditText editTextRegisterTeacherFullName,editTextRegisterTeacherEmail,editTextRegisterTeacherPwd;
    private RadioGroup radioGroupRegisterTeacherGender;
    private RadioButton radioButtonRegisterTeacherGenderSelected;

    private CheckBox integrationweb;
    private CheckBox developpementmobile;
    private CheckBox IA;

    private TextView account;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ens);
        editTextRegisterTeacherFullName=findViewById(R.id.editText_teacher_full_name);
        editTextRegisterTeacherEmail=findViewById(R.id.editText_teacher_email);
        editTextRegisterTeacherPwd=findViewById(R.id.editText_teacher_password);
        radioGroupRegisterTeacherGender=findViewById(R.id.radio_group_teacher_gender);
        radioGroupRegisterTeacherGender.clearCheck();
        integrationweb=findViewById(R.id.teacher_subject_inweb);
        IA=findViewById(R.id.teacher_subject_ia);
        developpementmobile=findViewById(R.id.teacher_subject_devmob);

        account=findViewById(R.id.haveaccount);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register_ens.this,Login_action.class);
                startActivity(i);
                finish();
            }
        });

        Button registerTeacher=findViewById(R.id.button_teacher_register);
        registerTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedTeacherGenderId=radioGroupRegisterTeacherGender.getCheckedRadioButtonId();
                radioButtonRegisterTeacherGenderSelected=findViewById(selectedTeacherGenderId);
                String textTeacherFullName=editTextRegisterTeacherFullName.getText().toString();
                String textTeacherEmail=editTextRegisterTeacherEmail.getText().toString();
                String textTeacherPwd=editTextRegisterTeacherPwd.getText().toString();
                String textTeacherGender;


                if(TextUtils.isEmpty(textTeacherFullName))
                {
                    Toast.makeText(Register_ens.this,"Please Enter your full name",Toast.LENGTH_LONG).show();
                    editTextRegisterTeacherFullName.setError("Full Name is required");
                    editTextRegisterTeacherFullName.requestFocus();
                }
                else if(TextUtils.isEmpty(textTeacherEmail))
                {
                    Toast.makeText(Register_ens.this,"Please Enter your email",Toast.LENGTH_LONG).show();
                    editTextRegisterTeacherEmail.setError("Email is required");
                    editTextRegisterTeacherEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textTeacherEmail).matches())
                {
                    Toast.makeText(Register_ens.this,"Please re-enter email",Toast.LENGTH_LONG).show();
                    editTextRegisterTeacherEmail.setError("verify your email");
                    editTextRegisterTeacherEmail.requestFocus();
                }
                else if(radioGroupRegisterTeacherGender.getCheckedRadioButtonId()== -1)
                {
                    Toast.makeText(Register_ens.this,"Please check gender",Toast.LENGTH_LONG).show();
                    radioButtonRegisterTeacherGenderSelected.setError("Check gender is required");
                    radioButtonRegisterTeacherGenderSelected.requestFocus();
                }
                else if(TextUtils.isEmpty(textTeacherPwd))
                {
                    Toast.makeText(Register_ens.this,"Please Enter your password",Toast.LENGTH_LONG).show();
                    editTextRegisterTeacherPwd.setError("pass is required");
                    editTextRegisterTeacherPwd.requestFocus();
                }
                else if(textTeacherPwd.length()<6)
                {
                    Toast.makeText(Register_ens.this,"Please re-enter your password",Toast.LENGTH_LONG).show();
                    editTextRegisterTeacherPwd.setError("password too weak");
                    editTextRegisterTeacherPwd.requestFocus();
                }
                else
                {
                    textTeacherGender=radioButtonRegisterTeacherGenderSelected.getText().toString();
                    registerEns(textTeacherFullName,textTeacherEmail,textTeacherGender,textTeacherPwd,integrationweb.isChecked(), IA.isChecked(), developpementmobile.isChecked());

                }
            }
        });
    }

    private void registerEns(String textTeacherFullName, String textTeacherEmail, String textTeacherGender, String textTeacherPwd, boolean isIntegrationWebChecked, boolean isIAChecked, boolean isDeveloppementMobileChecked)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Teacher");
        auth.createUserWithEmailAndPassword(textTeacherEmail,textTeacherPwd).addOnCompleteListener(Register_ens.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Register_ens.this,"User Created",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    if(firebaseUser !=null)
                    {
                        String userId = firebaseUser.getUid();
                        Teacher teacher = new Teacher(textTeacherFullName, textTeacherEmail,textTeacherGender,isIntegrationWebChecked,isIAChecked,isDeveloppementMobileChecked);
                        Log.d("TeacherData", "Name: " + teacher.fullName);
                        Log.d("TeacherData", "Email: " + teacher.email);
                        Log.d("TeacherData", "Gender: " + teacher.gender);
                        Log.d("TeacherData", "IntegrationWeb: " + teacher.integrationWeb);
                        Log.d("TeacherData", "IA: " + teacher.ia);
                        Log.d("TeacherData", "DevelopmentMobile: " + teacher.developmentMobile);
                        DatabaseReference userRef = databaseReference.child(userId);
                        userRef.setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        Intent intent = new Intent(Register_ens.this, Login_action.class);
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