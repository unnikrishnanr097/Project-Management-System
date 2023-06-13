package com.develop.projectmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.develop.projectmanagementsystem.adapter.AdapterView;
import com.develop.projectmanagementsystem.entity.Project;
import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ApprovalActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterView adapterView;
    ArrayList<Project> projectArrayList;
    ImageView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        User user = (User) getIntent().getSerializableExtra("user");
//        Log.i(TAG,user.getEmail());
        Toast.makeText(getApplicationContext(), "Data received: " + user.toString(), Toast.LENGTH_SHORT).show();
        projectArrayList = new ArrayList<>();
        account = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclrView1);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("projects").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Project c = d.toObject(Project.class);
                        assert c != null;
                        String s2 = c.getAssigned();
                        String s1 = user.getEmail();
                        if (s1.equals(s2)) {
                            String s = d.getId();
                            String[] arr = s.split("-");
                            String email = arr[0];
                            c.setEmail(email);
                            projectArrayList.add(c);
                        }
                    }
                    adapterView = new AdapterView(getApplicationContext(), user, projectArrayList);
                    recyclerView.setAdapter(adapterView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ApprovalActivity.this));

                } else {
                    Toast.makeText(ApprovalActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("user", user);
                startActivity(i);

            }
        });
    }
}