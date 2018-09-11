package com.example.daniellachacz.taskapprealm.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.daniellachacz.taskapprealm.R;
import com.example.daniellachacz.taskapprealm.database.IRecyclerItemClickListener;
import com.example.daniellachacz.taskapprealm.database.IRecyclerItemData;
import com.example.daniellachacz.taskapprealm.database.RealmHelper;
import com.example.daniellachacz.taskapprealm.edit.EditFragment;
import com.example.daniellachacz.taskapprealm.model.Task;
import com.example.daniellachacz.taskapprealm.task.TaskFragment;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity implements IRecyclerItemClickListener {

    @Nullable
    @BindView(R.id.floating_button) FloatingActionButton floatingButton;

    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    Realm realm;
    RealmHelper realmHelper;
    List<Task> tasks;
    String taskID;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        final EditFragment editFragment = new EditFragment();

        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);

        realmHelper = new RealmHelper(realm);
        tasks = new ArrayList<>();
        tasks = realmHelper.getAllTasks();

        mainAdapter = new MainAdapter(tasks, this, new IRecyclerItemData() {
            @Override
            public void itemData(int position, List<Task> tasks) {
                editFragment.itemData(position, tasks);
            }
        });
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setHasFixedSize(true);

    }


    @Optional
    @OnClick(R.id.floating_button)
    public void onClick() {
        android.app.Fragment fragment = new TaskFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void showFloatingActionButton() {
        floatingButton.show();
    }


    public void hideFloatingActionButton() {
        floatingButton.hide();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showFloatingActionButton();
        recyclerView.removeAllViews();
    }


    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }


    @Override
    public void onDeleteClick(int position) {
            Task task = tasks.get(position);
            taskID = task.getId();
            if (taskID != null) {
                realmHelper.deleteTask(taskID);
                Log.d(TAG, "ON DELETE CLICK " + taskID);

                Toast.makeText(getApplicationContext(), "Task removed", Toast.LENGTH_LONG).show();
            }
    }


    @Override
    public void onEditClick(int position) {
        Fragment fragment = new EditFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





}
