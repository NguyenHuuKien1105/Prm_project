package com.example.prm;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ClassViewHolder> {
    ArrayList<UserItem> userItems;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ManagerAdapter(ArrayList<UserItem> userItems) {
        this.userItems = userItems;
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView username;
        TextView password;
        TextView roll;

        public ClassViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            username = itemView.findViewById(R.id.username1);
            password = itemView.findViewById(R.id.password1);
            roll = itemView.findViewById(R.id.roll1);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 0,0,"EDIT");
            menu.add(getAdapterPosition(), 1,0,"DELETE");
        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_item,
                        parent, false);
        return new ClassViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.username.setText(userItems.get(position).getUsername());
        holder.password.setText(userItems.get(position).getPassword());
        holder.roll.setText(String.valueOf(userItems.get(position).getRoll()));
    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }


}