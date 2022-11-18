package com.example.roomexampleapp.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.Update;

import com.example.roomexampleapp.Database.APPDB;
import com.example.roomexampleapp.Entity.ItemClass;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ItemDAO {

    @Insert
    public void insert(ItemClass... items);
    @Update
    public void update(ItemClass... items);
    @Delete
    public void delete(ItemClass item);

    @Query("SELECT * FROM items WHERE id = :id")
    public ItemClass getItemByID(Long id);

    @Query("select * from items")
    public List<ItemClass> getAllItems();

    @Query("UPDATE items SET taskName = :name, taskDesc = :desc WHERE id =:taskId")
    public void updateDataOf(String taskId,String name,String desc);
}
