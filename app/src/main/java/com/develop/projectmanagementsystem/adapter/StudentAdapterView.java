package com.develop.projectmanagementsystem.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.develop.projectmanagementsystem.R;
import com.develop.projectmanagementsystem.entity.Project;
import com.develop.projectmanagementsystem.entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class StudentAdapterView extends RecyclerView.Adapter<StudentAdapterView.MyViewholder> {

    Context context;
    User user;
    ArrayList<Project> arrayList;
    FirebaseFirestore db;

    public StudentAdapterView(Context context, User user, ArrayList<Project> arrayList) {
        this.context = context;
        this.user = user;
        this.arrayList = arrayList;
        db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public StudentAdapterView.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_student, parent, false);
        return new StudentAdapterView.MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Project project = arrayList.get(position);
        String email = project.getAssigned();
        db.collection("projects").document(project.getEmail() + "-" + project.getProjectName()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.hod_name.setText(String.valueOf(documentSnapshot.get("headOfDepartment")));
                holder.in_charge_name.setText(String.valueOf(documentSnapshot.get("projectInCharge")));
                holder.guide_name.setText(String.valueOf(documentSnapshot.get("internalGuide")));
                holder.source_code_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, String.valueOf(documentSnapshot.get("sourceCodeLink")), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(documentSnapshot.get("sourceCodeLink"))));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setPackage("com.android.chrome");
                        context.startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        holder.project_name.setText(project.getProjectName());
        holder.status.setText(project.getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView project_name, status, hod_name, in_charge_name, guide_name;
        Button source_code_link;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            project_name = itemView.findViewById(R.id.textView_student_project_name);
            status = itemView.findViewById(R.id.textView_student_status);
            hod_name = itemView.findViewById(R.id.textView_hod_name);
            in_charge_name = itemView.findViewById(R.id.textView_incharge_name);
            guide_name = itemView.findViewById(R.id.textView_guide_name);
            source_code_link = itemView.findViewById(R.id.button_click_source_code);
        }
    }
}
