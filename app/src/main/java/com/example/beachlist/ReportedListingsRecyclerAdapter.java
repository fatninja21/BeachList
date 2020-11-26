package com.example.beachlist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReportedListingsRecyclerAdapter extends RecyclerView.Adapter<ReportedListingsRecyclerAdapter.MyViewHolder> {
    private static final String TAG = "Firebase Database";
    Context context;
    List<DataSnapshot> list;
    FirebaseDatabase database;

    public ReportedListingsRecyclerAdapter(Context context, List<DataSnapshot> list) {
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ReportedListingsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reported_listing_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportedListingsRecyclerAdapter.MyViewHolder holder, final int position) {
        Glide.with(context)
                .load(list.get(position).child("imageUrl").getValue(String.class))
                .centerCrop()
                .into(holder.reportedListingImage);
        holder.reportedListingTitle.setText(list.get(position).child("title").getValue(String.class));
        holder.reportedReason.setText(list.get(position).child("reason").getValue(String.class));
        //holder.timesReported

        final String type = list.get(position).child("type").getValue(String.class);
        final String listingId = list.get(position).getKey();
        // when a listing is clicked, the position is taken to get that person info
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                assert type != null;
                if(type.equalsIgnoreCase("item"))
                    intent = new Intent(context, SelectedItem.class);
                else
                    intent = new Intent(context, SelectedService.class);
                intent.putExtra("type", type);
                intent.putExtra("listingID", listingId);
                context.startActivity(intent);
            }
        });

        holder.waiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference listingRef = database.getReference("listings").child(type).child(listingId).child("banned");
                listingRef.setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference reportedRef = database.getReference("reported").child("listings").child(listingId);
                        reportedRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                list.remove(position);
                                System.out.println("Waived");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: " + e.getLocalizedMessage(), e.getCause());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getLocalizedMessage(), e.getCause());
                    }
                });
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference listingRef = database.getReference("listings").child(type).child(listingId);
                listingRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference reportedRef = database.getReference("reported").child("listings").child(listingId);
                        reportedRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Deleted");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: " + e.getLocalizedMessage(), e.getCause());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                    }
                });
            }
        });

        holder.banUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Its working 3");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView reportedListingImage;
        TextView reportedListingTitle, reportedReason, timesReported;
        Button waiveButton, deleteButton, banUserButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reportedListingImage = itemView.findViewById(R.id.reported_listing_image);
            reportedListingTitle = itemView.findViewById(R.id.reported_listing_name_tv);
            reportedReason = itemView.findViewById(R.id.reason_for_report_listing_tv);
            timesReported = itemView.findViewById(R.id.num_times_list_reported_tv);
            waiveButton = itemView.findViewById(R.id.waive_report_listing_btn);
            deleteButton = itemView.findViewById(R.id.delete_post_btn);
            banUserButton = itemView.findViewById(R.id.ban_owner_btn);
        }
    }
}
