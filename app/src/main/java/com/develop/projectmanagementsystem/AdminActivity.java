package com.develop.projectmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.develop.projectmanagementsystem.adapter.AdapterView;
import com.develop.projectmanagementsystem.adapter.AdminAdapter;
import com.develop.projectmanagementsystem.entity.Project;
import com.develop.projectmanagementsystem.entity.User;
import com.develop.projectmanagementsystem.entity.User1;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    AdminAdapter adapterView;
    RecyclerView recyclerView;
    ArrayList<User1> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        recyclerView = findViewById(R.id.recyclrView_admin);
        userArrayList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        User1 c = d.toObject(User1.class);
                        userArrayList.add(c);

                    }
                }
                adapterView = new AdminAdapter(getApplicationContext(), userArrayList);
                recyclerView.setAdapter(adapterView);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
                Toast.makeText(getApplicationContext(), "Users: " + (long) userArrayList.size(), Toast.LENGTH_SHORT).show();
//                adapterView.notifyDataSetChanged();
//                recyclerView.scrollToPosition(0);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        Toast.makeText(getApplicationContext(), "SET: " + (long) userArrayList.size(), Toast.LENGTH_SHORT).show();


    }
}