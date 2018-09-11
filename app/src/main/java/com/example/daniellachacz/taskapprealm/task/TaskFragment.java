package com.example.daniellachacz.taskapprealm.task;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.daniellachacz.taskapprealm.database.RealmHelper;
import com.example.daniellachacz.taskapprealm.main.MainActivity;
import com.example.daniellachacz.taskapprealm.R;
import com.example.daniellachacz.taskapprealm.model.Task;
import com.melnykov.fab.FloatingActionButton;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class TaskFragment extends Fragment {

    private static final String TAG = "TaskFragment";

    @Nullable
    @BindView(R.id.floating_button) FloatingActionButton floatingButton;
    @BindView(R.id.date_text) TextView dateText;
    @BindView(R.id.time_text) TextView timeText;
    @BindView(R.id.title_text) TextView titleText;
    @BindView(R.id.save_button) Button saveButton;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    Realm realm;
    RealmHelper realmHelper;

    String mText, mDate, mTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment, container, false);

        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).hideFloatingActionButton();


        realmHelper = new RealmHelper(realm);
        Realm.init(getActivity());
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDataSet: dd/mm/yyyy: " + year + "/" + month + "/" + dayOfMonth);
                String date = dayOfMonth + "/" + month + "/" + year;
                dateText.setText(date);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, "onTimeSet: hh/mm: " + hourOfDay + "/" + minute);
                String time = hourOfDay + ":" + minute;
                timeText.setText(time);
            }
        };

        return view;

    }


    @OnClick(R.id.date_text)
    public void onClickDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @OnClick(R.id.time_text)
    public void onClickTime() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener,
                hourOfDay, minute, DateFormat.is24HourFormat(getActivity()));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @OnClick(R.id.save_button)
    public void onClickSave() {
        mText = titleText.getText().toString();
        mDate = dateText.getText().toString();
        mTime = timeText.getText().toString();

        Task task = new Task();
        task.setText(mText);
        task.setDate(mDate);
        task.setTime(mTime);

        Log.d(TAG, "ON_CLICK_SAVE" + " / " + mText + " / " + mDate + " / " + mTime + " / " + getId());

        realmHelper = new RealmHelper(realm);
        realmHelper.saveTask(task);


        if(!mText.isEmpty() && !mDate.isEmpty() && !mTime.isEmpty()) {

            Toast.makeText(getActivity(), "Task added", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_LONG).show();
        }

        titleText.setText("");
        dateText.setText("");
        timeText.setText("");
    }


    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }


}
