package com.develop.projectmanagementsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.develop.projectmanagementsystem.entity.EmailSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        name_edt = findViewById(R.id.editTextPersonName);
        email_edt = findViewById(R.id.editTextEmailAddress);
        password_edt = findViewById(R.id.editTextTextPassword);
        department_edt = findViewById(R.id.editTextDepartment);
        hod_btn = findViewById(R.id.radioButton_HOD);
        inCharge_btn = findViewById(R.id.radioButton_INCHARGE);
        guide_btn = findViewById(R.id.radioButton_GUIDE);
        student_btn = findViewById(R.id.radioButton_STUDENT);
        radioGroup = findViewById(R.id.radioGroup);
        db = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(view -> {
            String name = name_edt.getText().toString();
            String email = email_edt.getText().toString();
            String password = password_edt.getText().toString();
            String department = department_edt.getText().toString();

            int selectedId = radioGroup.getCheckedRadioButtonId();
            selected_btn = (RadioButton) findViewById(selectedId);
            String role = selected_btn.getText().toString();


            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("password", password);
            user.put("department", department);
            user.put("role", role);

            db.collection("users").document(email).set(user)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        EmailSender.sendEmail(email, "Registration Successfully Completed!!!", "Hi " + name + ", you have registered successfully. Please login through application. Password: " + password);
                        Toast.makeText(getApplicationContext(), "Email Sent...", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                    });
        });

    }
}
