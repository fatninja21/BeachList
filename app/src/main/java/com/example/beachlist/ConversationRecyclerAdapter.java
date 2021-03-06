package com.example.beachlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ConversationRecyclerAdapter extends RecyclerView.Adapter<ConversationRecyclerAdapter.MyViewHolder> {
    private Context context;
    private final List<DataSnapshot> list;
    private String imageUrl;
    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    public ConversationRecyclerAdapter(Context context, List<DataSnapshot> list, String imageUrl) {
        this.context = context;
        this.list = list;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ConversationRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationRecyclerAdapter.MyViewHolder holder, final int position) {
        //TODO access messages
//        String senderId = list.get(position).child("senderId").getValue(String.class);
//        System.out.println(imageUrl);
//        if (!senderId.equals(user.getUid())) {
//            System.out.println(senderId);
//            Glide.with(context)
//                    .load(imageUrl)
//                    .centerCrop()
//                    .into(holder.userPic);
//        }
        holder.userMessage.setText(list.get(position).child("message").getValue(String.class));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView userPic;
        TextView userMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userPic = itemView.findViewById(R.id.profile_image);
            userMessage = itemView.findViewById(R.id.show_message);
        }
    }

    public int getItemViewType(int position){
        // Show user's messages on the right side
        if (list.get(position).child("senderId").getValue(String.class).equals(user.getUid())){
            return MSG_RIGHT;
        }
        else{
            return MSG_LEFT;
        }
    }
}

