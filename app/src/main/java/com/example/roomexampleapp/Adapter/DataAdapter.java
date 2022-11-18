package com.example.roomexampleapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.roomexampleapp.DAO.ItemDAO;
import com.example.roomexampleapp.Database.APPDB;
import com.example.roomexampleapp.Entity.ItemClass;
import com.example.roomexampleapp.R;


import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Context context;
    List<ItemClass> itemClassList;

    public DataAdapter(Context context, List<ItemClass> itemClassList) {
        this.context = context;
        this.itemClassList = itemClassList;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        ItemClass data = itemClassList.get(holder.getAdapterPosition());

        holder.taskID.setText(data.getId().toString());
        holder.taskName.setText(data.getTaskName().trim());
        holder.taskDescription.setText(data.getTaskDesc().trim());


        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask();
            }

            private void deleteTask() {
                APPDB database = Room.databaseBuilder(holder.itemView.getContext(), APPDB.class,"itemdb")
                        .allowMainThreadQueries()
                        .build();

                ItemDAO itemDAO = database.getItemDAO();

                ItemClass item = new ItemClass();
                item.setId(data.getId());
                item.setTaskName(data.getTaskName());
                item.setTaskDesc(data.getTaskDesc());

                try {
                    itemDAO.delete(item);
                    Toast.makeText(holder.itemView.getContext(), "Data id :"+data.getId() + "is deleted !", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Failed :"+e, Toast.LENGTH_SHORT).show();
                }
                itemClassList = itemDAO.getAllItems();
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), itemClassList.size());
                notifyDataSetChanged();
            }
        });

        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APPDB database = Room.databaseBuilder(holder.itemView.getContext(), APPDB.class,"itemdb")
                        .allowMainThreadQueries()
                        .build();

                ItemDAO itemDAO = database.getItemDAO();

                AlertDialog builder = new AlertDialog.Builder(view.getContext())
                        .setView(R.layout.edit_data_layout)
                        .show();

                builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
                EditText nameEdittext = builder.findViewById(R.id.dbEditTaskNameEditText);
                EditText descEdittext = builder.findViewById(R.id.dbEditTaskDescEditText);

                nameEdittext.setText(holder.taskName.getText());
                descEdittext.setText(holder.taskDescription.getText());

                builder.findViewById(R.id.dbEditTaskButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String id = String.valueOf(data.getId());
                        String taskName = nameEdittext.getText().toString();
                        String taskDesc = descEdittext.getText().toString();


                        try {
                            itemDAO.updateDataOf(id,taskName,taskDesc);
                            Toast.makeText(holder.itemView.getContext(), "Data id :"+data.getId() + "is updated !", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, "Failed :"+e, Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(view.getContext(), "this is " + itemDAO.getItemByID(data.getId()).getTaskName(), Toast.LENGTH_SHORT).show();

                        itemClassList = itemDAO.getAllItems();
                        notifyItemChanged(holder.itemView.getId());
                        notifyDataSetChanged();
                        builder.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskID,taskName,taskDescription;
        AppCompatImageButton deleteTask,editTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskID = itemView.findViewById(R.id.dbTaskID);
            taskName = itemView.findViewById(R.id.dbTaskName);
            taskDescription = itemView.findViewById(R.id.dbTaskDesc);
            editTask = itemView.findViewById(R.id.editTask);
            deleteTask = itemView.findViewById(R.id.deleteTask);
        }
    }



}
