package com.develop.projectmanagementsystem.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
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
import com.develop.projectmanagementsystem.entity.User;
import com.develop.projectmanagementsystem.entity.User1;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewholder> {
    Context context;
    ArrayList<User1> userArrayList;
    FirebaseFirestore db;

    public AdminAdapter(Context context, ArrayList<User1> userArrayList) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        this.userArrayList = (ArrayList<User1>) userArrayList.stream()
                .sorted(Comparator.comparingInt(User1::getStatus))
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public AdminAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_recycler_admin, parent, false);
        return new AdminAdapter.MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.MyViewholder holder, int position) {
        User1 user = userArrayList.get(position);
        System.out.println(userArrayList.get(0).getName());
        holder.message.setTextColor(Color.WHITE);
        holder.name.setText(user.getName());
        holder.role.setText(user.getRole());
        holder.department.setText(user.getDepartment());
        holder.email.setText(user.getEmail());

        if (user.getStatus() == 2) {
            holder.approve.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
            holder.itemView.setBackgroundColor(Color.RED);
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText("REJECTED");
        } else if (user.getStatus() == 0) {
            holder.itemView.setBackgroundColor(Color.rgb(83, 85, 82));
        } else if (user.getStatus() == 1) {
            holder.itemView.setBackgroundColor(Color.GRAY);
            holder.approve.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText("APPROVED");
        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> status = new HashMap<>();
                status.put("status", 1);
                db.collection("users").document(user.getEmail()).update(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userArrayList.get(holder.getAdapterPosition()).setStatus(1);
                        userArrayList.sort(User1.StatusComparator);
                        Log.i(TAG, "Accept: ");
                        for (User1 user1 : userArrayList) {
                            Log.i(TAG, String.valueOf(user1.getEmail() + " : " + user1.getStatus()));
                        }
                        notifyItemChanged(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> status = new HashMap<>();
                status.put("status", 2);
                db.collection("users").document(user.getEmail()).update(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userArrayList.get(holder.getAdapterPosition()).setStatus(2);
                        Log.i(TAG, "Reject: ");

                        for (User1 user1 : userArrayList) {
                            Log.i(TAG, String.valueOf(user1.getEmail() + " : " + user1.getStatus()));
                        }
                        userArrayList.sort(User1.StatusComparator);
                        notifyItemChanged(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public class MyViewholder extends RecyclerView.ViewHolder {
        TextView name, email, role, department, message;
        Button approve, reject;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_id);
            email = itemView.findViewById(R.id.email_id);
            role = itemView.findViewById(R.id.role_id);
            department = itemView.findViewById(R.id.dept_id);
            approve = itemView.findViewById(R.id.approve_btn);
            reject = itemView.findViewById(R.id.reject_btn);
            message = itemView.findViewById(R.id.textView2);

        }
    }

}
