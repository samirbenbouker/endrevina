package com.example.endrevina;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoRecords extends RecyclerView.Adapter<InfoRecords.ViewHolder> {

    private static ArrayList<User> usersList;

    public InfoRecords(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNameUser;
        public TextView txtAttemptsUser;
        public TextView txtTimeUser;
        public ImageView photoUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtNameUser = itemView.findViewById(R.id.txtRecordName);
            this.txtAttemptsUser = itemView.findViewById(R.id.txtRecordAttempts);
            this.txtTimeUser = itemView.findViewById(R.id.txtRecordTime);
            this.photoUser = itemView.findViewById(R.id.imgRecordPhoto);
        }

        public void setData(User user){
            this.txtNameUser.setText(user.getName());
            this.txtAttemptsUser.setText(String.valueOf(user.getAttempts()));
            this.txtTimeUser.setText(String.valueOf(user.getTime()));
            this.photoUser.setImageBitmap(user.getPhoto());
        }
    }

    @NonNull
    @Override
    public InfoRecords.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_info_records, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}