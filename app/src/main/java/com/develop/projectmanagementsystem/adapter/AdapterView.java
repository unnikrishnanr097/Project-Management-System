package com.develop.projectmanagementsystem.adapter;

import static android.content.ContentValues.TAG;

import static androidx.core.app.ActivityCompat.recreate;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.develop.projectmanagementsystem.R;
import com.develop.projectmanagementsystem.entity.EmailSender;
import com.develop.projectmanagementsystem.entity.Project;
import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;


public class AdapterView extends RecyclerView.Adapter<AdapterView.MyViewholder> {
    Context context;
    User user;
    ArrayList<Project> projectArrayList;
    StorageReference storageRef;
    FirebaseFirestore db;


    public AdapterView(Context context, User user, ArrayList<Project> projectArrayList) {
        this.context = context;
        this.user = user;
        this.projectArrayList = projectArrayList;
        storageRef = FirebaseStorage.getInstance().getReference().child("projects");
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_recycler, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Project project = projectArrayList.get(position);
        holder.emailView.setText(project.getEmail());
        holder.projectNameView.setText(project.getProjectName());
        Map<String, Object> user_data = new HashMap<>();
        holder.approval_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> user_data = new HashMap<>();
                String role = "";
                String status = "";

                if (user.getRole() == 1 && Objects.equals(project.getStatus(), "GUIDE APPROVED")) {
                    role = "HEAD OF DEPARTMENT";
                    status = "APPROVED";
                } else if (user.getRole() == 2) {
                    role = "INTERNAL GUIDE";
                    status = "GUIDE APPROVED";
                } else if (user.getRole() == 1 && !Objects.equals(project.getStatus(), "GUIDE APPROVED")) {
                    role = "INTERNAL GUIDE";
                }
                float rating = holder.ratingBar.getRating();
                String review = holder.multiText.getText().toString();
                if (user.getRole() == 2) {
                    user_data.put("guideRating", rating);
                    user_data.put("guideReview", review);
                } else if (user.getRole() == 1) {
                    user_data.put("hodRating", rating);
                    user_data.put("hodReview", review);
                }

                if (user.getRole()==1 && !Objects.equals(project.getStatus(), "GUIDE APPROVED")) {
                    Query query = FirebaseFirestore.getInstance().collectionGroup("users").whereEqualTo("role", role).whereEqualTo("department", user.getDepartment());
                    query.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot querySnapshot) {
                                    long count = querySnapshot.size();
                                    String userRole = "";
                                    if (user.getRole() == 1) {
                                        userRole = "headOfDepartment";
                                    } else if (user.getRole() == 2) {
                                        userRole = "internalGuide";
                                    }
                                    System.out.println("Number of documents with role HOD: " + user.toString());
                                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                        System.out.println("Document ID: " + documentSnapshot.getId());
                                        user_data.put("assigned", documentSnapshot.getId());
                                        user_data.put(userRole, user.getEmail());
                                        user_data.put("status","HOD APPROVED");

                                        System.out.println("Data: " + documentSnapshot.get("email"));
                                        db.collection("projects").document(project.getEmail() + "-" + project.getProjectName())
                                                .update(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Project Updated: ", Toast.LENGTH_SHORT).show();
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
                }
                if (user.getRole() == 2 && Objects.equals(project.getStatus(), "HOD APPROVED")) {
                    Query query = FirebaseFirestore.getInstance().collectionGroup("users").whereEqualTo("role", role).whereEqualTo("department", user.getDepartment());
                    query.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot querySnapshot) {
                                    long count = querySnapshot.size();
                                    String userRole = "";
                                    if (user.getRole() == 1) {
                                        userRole = "headOfDepartment";
                                    } else if (user.getRole() == 2) {
                                        userRole = "internalGuide";
                                    }
                                    System.out.println("Number of documents with role HOD: " + user.toString());
                                    System.out.println("Number of documents with role HOD: " + querySnapshot.size());
                                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                        System.out.println("Document ID: " + documentSnapshot.getId());
                                        user_data.put("assigned", project.getHeadOfDepartment());
                                        user_data.put(userRole, user.getEmail());
                                        user_data.put("status", "GUIDE APPROVED");

                                        System.out.println("Data: " + documentSnapshot.get("email"));
                                        db.collection("projects").document(project.getEmail() + "-" + project.getProjectName())
                                                .update(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        Toast.makeText(context, "Project Updated: ", Toast.LENGTH_SHORT).show();
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
                }

                if (status.equals("APPROVED")) {
                    Map<String, Object> statusMap = new HashMap<>();
                    statusMap.put("status", status);
                    statusMap.put("assigned", "STUDENT");

                    db.collection("projects").document(project.getEmail() + "-" + project.getProjectName())
                            .update(statusMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Status Updated: ", Toast.LENGTH_SHORT).show();
                                    EmailSender.sendEmail(project.getEmail(), "Project Approved", "Congratulations, your project " + project.getProjectName() + " has been successfully completed 3 level verification.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }

//                ((Activity) context).recreate();
            }

        });

        holder.abstract_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageRef.child(project.getEmail()).child(project.getProjectName()).child("abstract.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String mFileName = project.getProjectName() + "_abstract";
                        Toast.makeText(context, "Download Starting", Toast.LENGTH_SHORT).show();
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDescription("Downloading file...");
                        request.setTitle(mFileName);
                        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, mFileName);
                        downloadManager.enqueue(request);
                    }
                });
            }
        });

        holder.report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageRef.child(project.getEmail()).child(project.getProjectName()).child("report.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String mFileName = project.getProjectName() + "_report";
                        Toast.makeText(context, "Download Starting", Toast.LENGTH_SHORT).show();
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDescription("Downloading file...");
                        request.setTitle(mFileName);
                        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, mFileName);
                        downloadManager.enqueue(request);
                    }
                });
            }
        });

        holder.synopsis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageRef.child(project.getEmail()).child(project.getProjectName()).child("synopsis.pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String mFileName = project.getProjectName() + "_synopsis";
                        Toast.makeText(context, "Download Starting", Toast.LENGTH_SHORT).show();
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDescription("Downloading file...");
                        request.setTitle(mFileName);
                        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, mFileName);
                        downloadManager.enqueue(request);
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return projectArrayList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        Button synopsis_btn, report_btn, abstract_btn, approval_btn, cancel_btn;
        TextView emailView, projectNameView;
        RatingBar ratingBar;
        EditText multiText;


        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            synopsis_btn = itemView.findViewById(R.id.button_synopsis);
            report_btn = itemView.findViewById(R.id.button_report);
            abstract_btn = itemView.findViewById(R.id.button_abstract);
            emailView = itemView.findViewById(R.id.textView_mail_id);
            projectNameView = itemView.findViewById(R.id.textView_project_name);
            approval_btn = itemView.findViewById(R.id.imageButton_approval);
            cancel_btn = itemView.findViewById(R.id.imageButton_cancel);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            multiText = itemView.findViewById(R.id.editTextTextMultiLine_Review);

        }
    }
}
