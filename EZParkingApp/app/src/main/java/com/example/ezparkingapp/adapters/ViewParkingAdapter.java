package com.example.ezparkingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.models.ParkingModelClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewParkingAdapter extends RecyclerView.Adapter<ViewParkingAdapter.ViewParkingViewHolder> {
    private ArrayList<ParkingModelClass> parkingModelClassArrayList = new ArrayList<>();
    private Context context;
    private FirebaseFirestore mRootRef;

    public ViewParkingAdapter(ArrayList<ParkingModelClass> parkingModelClassArrayList, Context context) {
        this.parkingModelClassArrayList = parkingModelClassArrayList;
        this.context = context;
        mRootRef = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_parking_layout, parent, false);
        return new ViewParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewParkingViewHolder holder, final int position) {
        final ParkingModelClass parkingModelClass = parkingModelClassArrayList.get(position);
        holder.slotName.setText(parkingModelClass.getSlot_name());
        holder.levelName.setText(parkingModelClass.getLevel_name());
        holder.dateAndTime.setText(parkingModelClass.getDate() + ", " + parkingModelClass.getTime());

        if (parkingModelClass.getStatus().equals("Booked")) {
            holder.parkedButton.setVisibility(View.VISIBLE);
            holder.cancelBookingButton.setVisibility(View.VISIBLE);
            holder.finishParkingButton.setVisibility(View.GONE);
        } else {
            holder.parkedButton.setVisibility(View.GONE);
            holder.cancelBookingButton.setVisibility(View.GONE);
            holder.finishParkingButton.setVisibility(View.VISIBLE);
        }

        holder.parkedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateParkingStatus(holder, parkingModelClass.getParking_id(), position);
            }
        });

        holder.cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking(holder, parkingModelClass.getParking_id(), parkingModelClass.getLevel_id(), parkingModelClass.getSlot_id(), position);
            }
        });

        holder.finishParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking(holder, parkingModelClass.getParking_id(), parkingModelClass.getLevel_id(), parkingModelClass.getSlot_id(), position);
            }
        });
    }

    private void cancelBooking(final ViewParkingViewHolder holder, String parking_id, final String level_id, final String slot_id, final int position) {
        holder.progressBarLayout.setVisibility(View.VISIBLE);
        DocumentReference documentReference = mRootRef.collection("Parking").document(parking_id);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference slotRef = mRootRef.collection("Levels").document(level_id)
                        .collection("Slots").document(slot_id);
                HashMap<String, Object> newHashMap = new HashMap();
                newHashMap.put("is_available", true);
                slotRef.update(newHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.progressBarLayout.setVisibility(View.GONE);
                        parkingModelClassArrayList.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        });
    }

    private void updateParkingStatus(final ViewParkingViewHolder holder, String parking_id, final int position) {
        holder.progressBarLayout.setVisibility(View.VISIBLE);
        DocumentReference documentReference = mRootRef.collection("Parking").document(parking_id);
        HashMap<String, Object> newHashMap = new HashMap();
        newHashMap.put("status", "Parked");
        documentReference.update(newHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                holder.progressBarLayout.setVisibility(View.GONE);
                parkingModelClassArrayList.get(position).setStatus("Parked");
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.max(parkingModelClassArrayList.size(), 0);
    }

    public static class ViewParkingViewHolder extends RecyclerView.ViewHolder {
        TextView slotName, levelName, dateAndTime;
        Button parkedButton, finishParkingButton, cancelBookingButton;
        RelativeLayout progressBarLayout;

        public ViewParkingViewHolder(@NonNull View itemView) {
            super(itemView);

            levelName = itemView.findViewById(R.id.view_parking_level_name);
            slotName = itemView.findViewById(R.id.view_parking_slot_name);
            dateAndTime = itemView.findViewById(R.id.view_parking_date_and_time);
            parkedButton = itemView.findViewById(R.id.start_parking_button);
            cancelBookingButton = itemView.findViewById(R.id.cancel_booking_button);
            finishParkingButton = itemView.findViewById(R.id.finish_parking_button);
            progressBarLayout = itemView.findViewById(R.id.view_parking_item_progress_bar_layout);
        }
    }
}
