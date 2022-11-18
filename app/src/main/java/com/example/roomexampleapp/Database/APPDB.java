package com.example.roomexampleapp.Database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.roomexampleapp.DAO.ItemDAO;
import com.example.roomexampleapp.Entity.ItemClass;

@Database(entities = {ItemClass.class}, version = 1)
public abstract class APPDB extends RoomDatabase {
    public abstract ItemDAO getItemDAO();

    public ItemDAO getDaoInstance()
    {

        ItemDAO itemDAO = this.getItemDAO();
        return itemDAO;
    }

}
