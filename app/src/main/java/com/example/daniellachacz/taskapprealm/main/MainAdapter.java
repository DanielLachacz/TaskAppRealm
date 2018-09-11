package com.example.daniellachacz.taskapprealm.main;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.daniellachacz.taskapprealm.R;
import com.example.daniellachacz.taskapprealm.database.IRecyclerItemClickListener;
import com.example.daniellachacz.taskapprealm.database.IRecyclerItemData;
import com.example.daniellachacz.taskapprealm.model.Task;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.TaskViewHolder> {

   private static IRecyclerItemClickListener callback;
   private static IRecyclerItemData callback2;
   private List<Task> tasks;

   private static final String TAG = "MainAdapter";


   public MainAdapter(List<Task> tasks, IRecyclerItemClickListener callback, IRecyclerItemData callback2) {
       this.tasks = tasks;
       this.callback = callback;
       this.callback2 = callback2;
   }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_item) TextView textItem;
        @BindView(R.id.date_item) TextView dateItem;
        @BindView(R.id.time_item) TextView timeItem;
        @BindView(R.id.delete_button) Button deleteButton;
        @BindView(R.id.edit_button) Button editButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, @SuppressLint("RecyclerView") final int position) {
       final Task task = tasks.get(position);
       holder.textItem.setText(task.getText());
       holder.dateItem.setText(task.getDate());
       holder.timeItem.setText(task.getTime());
       holder.deleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int position = holder.getAdapterPosition();
               callback.onDeleteClick(position);
               notifyItemRemoved(holder.getAdapterPosition());
               Log.d(TAG, "DELETE_CLICK " + holder.getAdapterPosition());
           }

       });

       holder.editButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int position = holder.getAdapterPosition();
               callback.onEditClick(position);
               callback2.itemData(position, tasks);


           }
       });

    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }



}