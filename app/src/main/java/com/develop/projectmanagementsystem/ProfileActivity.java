package com.develop.projectmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText editText_name, editText_role, editText_password, editText_dept, editText_email;
    Button save_button;
    User user;
    ImageView edit_password, edit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra("user");
        editText_name = findViewById(R.id.editText_Name);
        editText_role = findViewById(R.id.editText_role);
        editText_password = findViewById(R.id.editTextPasswrd);
        editText_dept = findViewById(R.id.editText_Dept);
        editText_email = findViewById(R.id.editText_Email);
        edit_password = findViewById(R.id.imageView_edit_password);
        edit_name = findViewById(R.id.imageView_edit_name);


        save_button = findViewById(R.id.button2);
        editText_name.setEnabled(false);
        editText_password.setEnabled(false);
        editText_role.setEnabled(false);
        editText_dept.setEnabled(false);
        editText_email.setEnabled(false);
        editText_name.setText(user.getName());
        editText_dept.setText(user.getDepartment());
        editText_email.setText(user.getEmail());

        if (user.getRole() == 1) {
            editText_role.setText("Head Of Department");
        } else if (user.getRole() == 2) {
            editText_role.setText("Project In Charge");
        } else if (user.getRole() == 3) {
            editText_role.setText("Internal Guide");
        } else if (user.getRole() == 4) {
            editText_role.setText("Student");
        }

        edit_name.setOnClickListener(view -> editText_name.setEnabled(true));
        edit_password.setOnClickListener(view -> editText_password.setEnabled(true));


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> updates = new HashMap<>();
                String newName = editText_name.getText().toString();
                String newPassword = editText_password.getText().toString();
                if (!newName.equals(user.getName())) {
                    updates.put("name", editText_name.getText().toString());
                }
                if (!newPassword.isEmpty()) {
                    updates.put("password", editText_password.getText().toString());
                }
                FirebaseFirestore.getInstance().collection("users").document(user.getEmail()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Updated ", Toast.LENGTH_SHORT).show();
                        recreate();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Updating Failed ", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
