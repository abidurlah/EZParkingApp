package com.example.ezparkingapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.adapters.ViewParkingAdapter;
import com.example.ezparkingapp.models.ParkingModelClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewParkingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CollectionReference mParkingCollectionRef;
    private String currentUserId;
    private ArrayList<ParkingModelClass> parkingModelClassArrayList = new ArrayList<>();
    private RelativeLayout mProgressBarLayout;
    private TextView noBookingsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_parking, container, false);

        mRecyclerView = view.findViewById(R.id.view_parking_recycler_view);
        mProgressBarLayout = view.findViewById(R.id.view_parking_progress_bar_layout);
        noBookingsTextView = view.findViewById(R.id.view_parking_no_booking_tv);


        mParkingCollectionRef = FirebaseFirestore.getInstance().collection("Parking");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getCurrentUserBookings();

        return view;
    }

    private void getCurrentUserBookings() {
        Query query = mParkingCollectionRef.whereEqualTo("user_id", currentUserId);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots) {
                        ParkingModelClass parkingModelClass = snapshot.toObject(ParkingModelClass.class);
                        parkingModelClassArrayList.add(parkingModelClass);
                    }
                    mProgressBarLayout.setVisibility(View.GONE);
                    noBookingsTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                    ViewParkingAdapter viewParkingAdapter = new ViewParkingAdapter(parkingModelClassArrayList, requireActivity());
                    mRecyclerView.setAdapter(viewParkingAdapter);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.GONE);
                    noBookingsTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}