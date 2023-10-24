package com.develop.projectmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.develop.projectmanagementsystem.entity.EmailSender;
import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import javax.mail.MessagingException;

public class LoginActivity extends AppCompatActivity {
    EditText email_txt, password_text;
    Button login_btn;
    FirebaseFirestore db;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        email_txt = findViewById(R.id.editTextTextEmailAddress);
        password_text = findViewById(R.id.editTextTextPassword2);
        login_btn = findViewById(R.id.button);

        final User[] user = new User[1];
        login_btn.setOnClickListener(view -> {
            String email = email_txt.getText().toString();
            String password = password_text.getText().toString();
            int verify=0;
            if (email.isBlank()) {
                Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                verify=1;
            }
            else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(getApplicationContext(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
                verify=1;
            }
            else if (password.isBlank()) {
                Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                verify=1;
            }
            if(email.equals("admin@gmail.com") && password.equals("admin"))
            {
                startActivity(new Intent(getApplicationContext(),UserCreationActivity.class));
            }
//            DocumentReference docRef = db.collection("users").document(email);
            if(verify==0) {
                db.collection("users").document(email).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login OK", Toast.LENGTH_SHORT).show();

                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("role");
                        String name = document.getString("name");
                        String email1 = document.getString("email");
                        String department = document.getString("department");
                        if (Objects.equals(role, "HEAD OF DEPARTMENT")) {
                            User user1 = new User(name, email1, department, 1);
                            Intent i = new Intent(getApplicationContext(), ApprovalActivity.class);
                            i.putExtra("user", user1);
                            startActivity(i);
                        }
//                    if (Objects.equals(role, "PROJECT IN CHARGE")) {
//                        User user1 = new User(name, email1, department, 2);
//                        Intent i = new Intent(getApplicationContext(), ApprovalActivity.class);
//                        i.putExtra("user", user1);
//                        startActivity(i);
//                    }
                        if (Objects.equals(role, "INTERNAL GUIDE")) {
                            User user1 = new User(name, email1, department, 2);
                            Intent i = new Intent(getApplicationContext(), ApprovalActivity.class);
                            i.putExtra("user", user1);
                            startActivity(i);
                        }
                        if (Objects.equals(role, "STUDENT")) {

                            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View popupView = inflater.inflate(R.layout.popup_window, null);
                            popupWindow = new PopupWindow(popupView, 1000, 1100);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                            User user1 = new User(name, email1, department, 4);

                            Button popupButton_creation = popupView.findViewById(R.id.popupButton_project_creation);
                            popupButton_creation.setOnClickListener(view13 -> {
                                Intent i = new Intent(getApplicationContext(), ProjectUploadActivity.class);
                                i.putExtra("user", user1);
                                startActivity(i);
                            });
                            popupView.findViewById(R.id.popupButton_project_list).setOnClickListener(view12 -> {
                                Intent i = new Intent(getApplicationContext(), StudentActivity.class);
                                i.putExtra("user", user1);
                                startActivity(i);
                            });

                            popupView.findViewById(R.id.button_profile).setOnClickListener(view1 -> {
                                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                i.putExtra("user", user1);
                                startActivity(i);
//                            popupWindow.dismiss();

                            });

                        }
                        Toast.makeText(getApplicationContext(), name + email1 + department, Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(e -> {

                });
            }
        });

    }


}