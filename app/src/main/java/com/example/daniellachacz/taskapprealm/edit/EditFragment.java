package com.example.daniellachacz.taskapprealm.edit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.daniellachacz.taskapprealm.R;
import com.example.daniellachacz.taskapprealm.database.IRecyclerItemData;
import com.example.daniellachacz.taskapprealm.database.RealmHelper;
import com.example.daniellachacz.taskapprealm.main.MainActivity;
import com.example.daniellachacz.taskapprealm.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EditFragment extends Fragment implements IRecyclerItemData {

    private static final String TAG = "EditFragment";

    @BindView(R.id.time_edit) TextView timeEdit;
    @BindView(R.id.date_edit) TextView dateEdit;
    @BindView(R.id.title_edit) EditText titleEdit;
    @BindView(R.id.save_edit_button) Button saveEditButton;

    private DatePickerDialog.OnDateSetListener mDateEditListener;
    private TimePickerDialog.OnTimeSetListener mTimeEditListener;

    List<Task> tasks;
    String taskID;
    RealmHelper realmHelper;
    Realm realm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_fragment, container, false);
        
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideFloatingActionButton();

        Realm.init(getActivity());
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);

        realmHelper = new RealmHelper(realm);
        tasks = new ArrayList<>();
        tasks = realmHelper.getAllTasks();

        mDateEditListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDataEditSet: dd/mm/yyyy: " + year + "/" + month + "/" + dayOfMonth);
                String date = dayOfMonth + "/" + month + "/" + year;
                dateEdit.setText(date);
            }
        };

        mTimeEditListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeEditSet: hh/mm: " + hourOfDay + "/" + minute);
                String time = hourOfDay + ":" + minute;
                timeEdit.setText(time);

            }
        };

        return view;
    }


    @OnClick(R.id.date_edit)
    public void onEditDateClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateEditListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @SuppressLint("ResourceType")
    @OnClick(R.id.time_edit)
    public void onEditTimeClick() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeEditListener,
                hourOfDay, minute, DateFormat.is24HourFormat(getActivity()));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @OnClick(R.id.save_edit_button)
    public  void onEditSaveClick() {

    }


    @Override
    public void itemData(int position, List<Task> tasks) {
            if (isAdded()&&!getActivity().isFinishing()) {
                Task task = tasks.get(position);
                taskID = task.getId();
                String taskTexT = task.getDate();
                titleEdit.setText(task.getText());

                Log.d(TAG, "EDIT FRAGMENT " + position + task + taskTexT);
            }
    }


    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }


}
