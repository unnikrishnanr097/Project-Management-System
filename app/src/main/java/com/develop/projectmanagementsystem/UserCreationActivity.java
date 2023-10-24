package com.develop.projectmanagementsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.develop.projectmanagementsystem.entity.EmailSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

public class UserCreationActivity extends AppCompatActivity {
    Button registerButton;
    EditText name_edt, email_edt, password_edt, department_edt;
    RadioButton hod_btn, inCharge_btn, guide_btn, student_btn;
    RadioGroup radioGroup;
    RadioButton selected_btn;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

        registerButton = findViewById(R.id.button_register);
        registerButton.setEnabled(false);
        name_edt = findViewById(R.id.editTextPersonName);
        email_edt = findViewById(R.id.editTextEmailAddress);
        password_edt = findViewById(R.id.editTextTextPassword);
        department_edt = findViewById(R.id.editTextDepartment);
        hod_btn = findViewById(R.id.radioButton_HOD);
//        inCharge_btn = findViewById(R.id.radioButton_INCHARGE);
        guide_btn = findViewById(R.id.radioButton_GUIDE);
        student_btn = findViewById(R.id.radioButton_STUDENT);
        radioGroup = findViewById(R.id.radioGroup);
        db = FirebaseFirestore.getInstance();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i != -1) {
                    // At least one radio button is selected, so enable the button
                    registerButton.setEnabled(true);
                } else {
                    // No radio button is selected, so disable the button
                    registerButton.setEnabled(false);
                }
            }
        });
        registerButton.setOnClickListener(view -> {
            int flag=0;
            String name = name_edt.getText().toString();
            String email = email_edt.getText().toString();
            String password = password_edt.getText().toString();
            String department = department_edt.getText().toString();
            int selectedId = radioGroup.getCheckedRadioButtonId();
            selected_btn = (RadioButton) findViewById(selectedId);
            String role = selected_btn.getText().toString();

            if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();

            } else if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();

            } else if (department.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter Department", Toast.LENGTH_SHORT).show();

            } else if (role.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Select role", Toast.LENGTH_SHORT).show();

            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(getApplicationContext(), "Please Enter valid mail", Toast.LENGTH_SHORT).show();
            } else {
                some_func();
            }
        });
    }
    public void some_func(){
        Query query = db.collection("users").whereEqualTo("email", email_edt.getText().toString());
        Map<String, Object> user = new HashMap<>();
        String name = name_edt.getText().toString();
        String email = email_edt.getText().toString();
        String password = password_edt.getText().toString();
        String department = department_edt.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        selected_btn = (RadioButton) findViewById(selectedId);
        String role = selected_btn.getText().toString();
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                long documentCount = queryDocumentSnapshots.size();
                Toast.makeText(getApplicationContext(), "count: "+documentCount, Toast.LENGTH_SHORT).show();
                if (documentCount == 0) {
                    user.put("name", name);
                    user.put("email", email);
                    user.put("password", password);
                    user.put("department", department);
                    user.put("role", role);

                    db.collection("users").document(email).set(user)
                            .addOnSuccessListener(documentReference -> {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "Email Sent...", Toast.LENGTH_SHORT).show();
                                EmailSender.sendEmail(email, "Registration Successfully Completed!!!", "Hi " + name + ", you have registered successfully. Please login through application. Password: " + password);
                                startActivity(new Intent(getApplicationContext(), UserCreationActivity.class));
                            }).addOnFailureListener(e -> {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
