package com.develop.projectmanagementsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.develop.projectmanagementsystem.adapter.AdapterView;
import com.develop.projectmanagementsystem.adapter.StudentAdapterView;
import com.develop.projectmanagementsystem.entity.Project;
import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
    User user;
    FirebaseFirestore db;
    ArrayList<Project> arrayList;
    RecyclerView recyclerView;
    StudentAdapterView adapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        arrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_student);
        user = (User) getIntent().getSerializableExtra("user");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("projects");
        Query query = collectionRef.whereEqualTo("email", user.getEmail());

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                Log.e(TAG,"List: "+list.toString());

                for (DocumentSnapshot d : list) {
                    Project c = d.toObject(Project.class);
                    assert c != null;
                    Log.e(TAG,"List: "+c.toString());
                    arrayList.add(c);
                }
                adapterView = new StudentAdapterView(getApplicationContext(), user, arrayList);
                Log.i(TAG, "StudentAdapterView: "+arrayList);
                recyclerView.setAdapter(adapterView);
                recyclerView.setLayoutManager(new LinearLayoutManager(StudentActivity.this));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}