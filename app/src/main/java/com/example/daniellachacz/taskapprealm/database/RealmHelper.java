package com.example.daniellachacz.taskapprealm.database;

import com.example.daniellachacz.taskapprealm.model.Task;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;


    public RealmHelper(Realm realm) {
        this.realm = realm;
    }


    public List<Task> getAllTasks() {
        RealmResults<Task> taskResult = realm.where(Task.class).findAll();

        return taskResult;
    }


    public void saveTask(final Task task) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task t = realm.copyToRealm(task);
            }
        });

    }


    public void deleteTask(final String taskID) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
              RealmResults<Task> results = bgRealm.where(Task.class).equalTo("id", taskID).findAll();
                  results.deleteAllFromRealm();


            }

        });

    }


    public void editTask(final String taskID) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Task task = realm.where(Task.class).equalTo("id", taskID).findFirst();

            }
        });
    }


}