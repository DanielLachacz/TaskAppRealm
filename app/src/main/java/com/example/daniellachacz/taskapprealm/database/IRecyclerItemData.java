package com.example.daniellachacz.taskapprealm.database;

import com.example.daniellachacz.taskapprealm.model.Task;

import java.util.List;

public interface IRecyclerItemData {

    void itemData(int position, List<Task> tasks);
}
