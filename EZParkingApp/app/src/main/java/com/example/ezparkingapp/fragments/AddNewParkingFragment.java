package com.example.ezparkingapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.adapters.SlotAdapter;
import com.example.ezparkingapp.models.LevelsModelClass;
import com.example.ezparkingapp.models.SlotModelClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddNewParkingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddNewParkingFragment";

    private SwitchCompat mHourlyDailySwitch;
    private RecyclerView mRecyclerView;
    private Spinner mLevelsSpinner;
    private Button mSearchButton;
    private LinearLayout mTopLinearLayout;
    private RelativeLayout mProgressBarLayout;
    private TextView mDateTV, mTimeTV;
    //
    private List<String> mLevelNames = new ArrayList<>();
    private ArrayAdapter<String> mLevelsArrayAdapter;
    private ArrayList<SlotModelClass> slotModelClassArrayList = new ArrayList<>();
    private ArrayList<LevelsModelClass> levelsModelClassArrayList = new ArrayList<>();
    private SlotAdapter slotAdapter;

    //Firebase
    FirebaseFirestore mRootRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_parking, container, false);

        initAllViews(view);
        getAllLevels();

        mHourlyDailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTimeTV.setVisibility(View.VISIBLE);
                    mHourlyDailySwitch.setText(getResources().getString(R.string.hourly));
                }
                else {
                    mTimeTV.setVisibility(View.INVISIBLE);
                    mHourlyDailySwitch.setText(getResources().getString(R.string.daily));
                }
            }
        });
        mSearchButton.setOnClickListener(this);
        mDateTV.setOnClickListener(this);
        mTimeTV.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        mProgressBarLayout.setVisibility(View.VISIBLE);
        super.onStart();
    }

    private void initAllViews(View view) {
        mHourlyDailySwitch = view.findViewById(R.id.switch_hourly_daily);
        mRecyclerView = view.findViewById(R.id.slots_recycler_view);
        mLevelsSpinner = view.findViewById(R.id.level_spinner);
        mSearchButton = view.findViewById(R.id.search_parking);
        mTopLinearLayout = view.findViewById(R.id.top_linear_layout);
        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);
        mDateTV = view.findViewById(R.id.date_tv);
        mTimeTV = view.findViewById(R.id.time_tv);
        mRootRef = FirebaseFirestore.getInstance();
    }

    private void getAllLevels() {
        CollectionReference collectionReference = mRootRef.collection("Levels");
        collectionReference.orderBy("level_name").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    mLevelNames.add(snapshot.get("level_name").toString());
                    levelsModelClassArrayList.add(new LevelsModelClass(snapshot.get("level_id").toString(), snapshot.get("level_name").toString()));
                }
                mLevelsArrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, mLevelNames);
                mLevelsSpinner.setAdapter(mLevelsArrayAdapter);

                mProgressBarLayout.setVisibility(View.GONE);
                mTopLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getSlotsWithLevelId(final String levelId, final String level_name) {
        slotModelClassArrayList.clear();
        CollectionReference collectionReference = mRootRef.collection("Levels").document(levelId).collection("Slots");
        collectionReference.orderBy("slot_name").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot: queryDocumentSnapshots) {
                    SlotModelClass slotModelClass = snapshot.toObject(SlotModelClass.class);
                    slotModelClassArrayList.add(slotModelClass);
                }
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false);
                mRecyclerView.setLayoutManager(gridLayoutManager);
                slotAdapter = new SlotAdapter(requireContext(), slotModelClassArrayList);
                setViewsData(levelId, level_name);
                mRecyclerView.setAdapter(slotAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBarLayout.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ",e );
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mSearchButton == v) {
            if (mDateTV.getText() != getResources().getString(R.string.date)){
                mProgressBarLayout.setVisibility(View.VISIBLE);
                getSlotsWithLevelId(levelsModelClassArrayList.get(mLevelsSpinner.getSelectedItemPosition()).getLevel_id(),
                        levelsModelClassArrayList.get(mLevelsSpinner.getSelectedItemPosition()).getLevel_name());
            } else {
                Toasty.error(requireContext(), "Please Select Date & Time", Toasty.LENGTH_LONG).show();
            }
        } else if (mTimeTV == v) {
            showTimePickerDialog();
        } else if (mDateTV == v) {
            showDatePickerDialog();
        }
    }

    private void setViewsData(String levelId, String level_name) {
        HashMap<String, String> stringHashMap = new HashMap<>();
        if (mHourlyDailySwitch.isChecked()) {
            stringHashMap.put("Date", mDateTV.getText().toString());
            stringHashMap.put("Time", mTimeTV.getText().toString());
            stringHashMap.put("level_id", levelId);
            stringHashMap.put("level_name", level_name);
        } else {
            stringHashMap.put("Date", mDateTV.getText().toString());
            stringHashMap.put("Time", null);
            stringHashMap.put("level_id", levelId);
            stringHashMap.put("level_name", level_name);
        }
        slotAdapter.setViewsData(stringHashMap);
    }

    private void showDatePickerDialog() {
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
                int month = monthOfYear + 1;
                mDateTV.setText(dayOfMonth + "/" + month + "/" + year );
//                activitydate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMinDate(System.currentTimeMillis());
        StartTime.show();
    }

    private void showTimePickerDialog() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mTimeTV.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}