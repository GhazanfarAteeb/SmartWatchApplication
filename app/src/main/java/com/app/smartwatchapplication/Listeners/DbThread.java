package com.app.smartwatchapplication.Listeners;

import com.app.smartwatchapplication.DB.DatabaseHelper;
import com.app.smartwatchapplication.Modals.PostReadings;

import java.util.List;

public class DbThread extends Thread {
    DBThreadListener listener;
    List<PostReadings> postReadingsList;
    DatabaseHelper dbHelper;
    public DbThread(DBThreadListener listener, List<PostReadings> postReadingsList, DatabaseHelper dbHelper) {
        this.listener = listener;
        this.dbHelper = dbHelper;
        this.postReadingsList = postReadingsList;
    }

    public void run() {
        listener.updateDB(postReadingsList, dbHelper);
    }

    public interface DBThreadListener {
        void updateDB(List<PostReadings> postReadingsList, DatabaseHelper dbHelper);
    }
}
