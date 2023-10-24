package com.develop.projectmanagementsystem;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProjectUploadActivity extends AppCompatActivity {
    private static final int FILE_PICKER_REQUEST_CODE_SYNOPSIS = 1;
    private static final int FILE_PICKER_REQUEST_CODE_ABSTRACT = 2;
    private static final int FILE_PICKER_REQUEST_CODE_REPORT = 3;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    ImageButton synopsis_upload_button, report_upload_button, abstract_upload_button;
    ImageView imageViewSynopsis, imageViewReport, imageViewAbstract;
    EditText projectName, sourceCode;
    Button save_btn, next_btn;
    User user;
    CardView cardView_abstract, cardView_synopsis, cardView_report;
    FirebaseFirestore db;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_upload);
        synopsis_upload_button = findViewById(R.id.synopsis_upload_button);
        report_upload_button = findViewById(R.id.report_upload_button);
        abstract_upload_button = findViewById(R.id.abstract_upload_button);
        imageViewSynopsis = findViewById(R.id.imageView_synopsis);
        imageViewAbstract = findViewById(R.id.imageView_abstract);
        imageViewReport = findViewById(R.id.imageView_report);
        projectName = findViewById(R.id.editTextProjectName);
        sourceCode = findViewById(R.id.editTextSourceCodeLink);
        next_btn = findViewById(R.id.button_next);
        save_btn = findViewById(R.id.button_saveProject);
        db = FirebaseFirestore.getInstance();
        cardView_abstract = findViewById(R.id.cardView_abstract);
        cardView_report = findViewById(R.id.cardView_report);
        cardView_synopsis = findViewById(R.id.cardView_synopsis);

        user = (User) getIntent().getSerializableExtra("user");

        synopsis_upload_button.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE_SYNOPSIS);
        });
        report_upload_button.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE_REPORT);
        });
        abstract_upload_button.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE_ABSTRACT);
        });

        if(projectName!=null || sourceCode!=null) {

            next_btn.setOnClickListener(view -> {
                String project_name = projectName.getText().toString();
                String source_code = sourceCode.getText().toString();
                Log.i(TAG, project_name);
                storageReference = storageRef.child("projects/").child(user.getEmail()).child(project_name);
                cardView_abstract.setVisibility(View.VISIBLE);
                cardView_report.setVisibility(View.VISIBLE);
                cardView_synopsis.setVisibility(View.VISIBLE);
                save_btn.setVisibility(View.VISIBLE);
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
        }

        save_btn.setOnClickListener(view -> {
//            if (Objects.equals(user.getDepartment(), "CS")) {
                // Create a new user_data with a first and last name
                Map<String, Object> user_data = new HashMap<>();
                user_data.put("projectName", projectName.getText().toString());
                user_data.put("sourceCodeLink", sourceCode.getText().toString());
                db.collection("projects").document();


//                CollectionReference usersCollectionRef = (CollectionReference) db.collectionGroup("users");
                Query query = FirebaseFirestore.getInstance().collectionGroup("users").whereEqualTo("role", "HEAD OF DEPARTMENT").whereEqualTo("department", this.user.getDepartment());

                query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot querySnapshot) {
                                long count = querySnapshot.size();
                                System.out.println("Number of documents with role HOD: " + count);
                                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                    System.out.println("Document ID: " + documentSnapshot.getId());
                                    user_data.put("assigned", documentSnapshot.getId());
                                    user_data.put("email", user.getEmail());

                                    System.out.println("Data: " + documentSnapshot.get("email"));
                                    db.collection("projects").document(user.getEmail() + "-" + projectName.getText().toString())
                                            .set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getApplicationContext(), "Project Created: ", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(), StudentActivity.class);
                                                    i.putExtra("user", user);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error getting documents", e);
                            }
                        });
//            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath;
        if (requestCode == FILE_PICKER_REQUEST_CODE_SYNOPSIS && resultCode == RESULT_OK) {
            filePath = data.getData();
            if (filePath != null) {
                StorageReference ref = storageReference.child("synopsis.pdf");
                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            imageViewSynopsis.setVisibility(View.VISIBLE);
                        })
                        .addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), "Failed to upload file", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            Toast.makeText(getApplicationContext(), "Upload progress: " + progress + "%", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == FILE_PICKER_REQUEST_CODE_ABSTRACT && resultCode == RESULT_OK) {
            filePath = data.getData();
            if (filePath != null) {
                StorageReference ref = storageReference.child("abstract.pdf");
                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            imageViewAbstract.setVisibility(View.VISIBLE);
                        })
                        .addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), "Failed to upload file", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            Toast.makeText(getApplicationContext(), "Upload progress: " + progress + "%", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == FILE_PICKER_REQUEST_CODE_REPORT && resultCode == RESULT_OK) {
            filePath = data.getData();
            if (filePath != null) {
                StorageReference ref = storageReference.child("report.pdf");
                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getApplicationContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                            imageViewReport.setVisibility(View.VISIBLE);
                        })
                        .addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), "Failed to upload file", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            Toast.makeText(getApplicationContext(), "Upload progress: " + progress + "%", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please select a file to upload", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
