package com.example.ezparkingapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.models.SlotModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder> {
    private WeakReference<Context> mContext;
    private ArrayList<SlotModelClass> slotModelClassArrayList = new ArrayList<>();
    private HashMap<String, String> viewsDataFromFragment = new HashMap<>();

    public SlotAdapter(Context context, ArrayList<SlotModelClass> slotModelClassArrayList) {
        this.slotModelClassArrayList = slotModelClassArrayList;
        this.mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public SlotAdapter.SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_slots_item_layout, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SlotViewHolder holder, final int position) {
        if (slotModelClassArrayList.get(position).isIs_available()) {
            holder.mainRelativeLayout.setBackgroundResource(R.drawable.icons8_rounded_square_green_100);
            holder.carImageView.setImageResource(R.drawable.green_car);
            holder.slotName.setTextColor(mContext.get().getResources().getColor(R.color.green));
        } else {
            holder.mainRelativeLayout.setBackgroundResource(R.drawable.rounded_square_red);
            holder.carImageView.setImageResource(R.drawable.red_car);
            holder.slotName.setTextColor(mContext.get().getResources().getColor(R.color.red));
        }
        holder.slotName.setText(slotModelClassArrayList.get(position).getSlot_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slotModelClassArrayList.get(position).isIs_available()) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    uploadDataToDatabase(position, holder);
                } else {
                    Toasty.warning(mContext.get(), "Not Available", Toasty.LENGTH_LONG).show();
                }
            }
        });
    }

    private void uploadDataToDatabase(final int position, final SlotViewHolder holder) {
        final SlotModelClass slotModelClass = slotModelClassArrayList.get(position);
        DocumentReference slotRef = FirebaseFirestore.getInstance()
                .collection("Levels")
                .document(viewsDataFromFragment.get("level_id"))
                .collection("Slots")
                .document(slotModelClass.getSlot_id());
        Log.d(TAG, "uploadDataToDatabase: Started");
        HashMap<String, Object> myHashMap = new HashMap();
        myHashMap.put("is_available", false);
        myHashMap.put("Date", viewsDataFromFragment.get("Date"));
        myHashMap.put("Time", viewsDataFromFragment.get("Time"));

        Log.d(TAG, "uploadDataToDatabase: HashMap " + myHashMap);
        slotRef.update(myHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DocumentReference parkingRef = FirebaseFirestore.getInstance()
                            .collection("Parking").document();
                    HashMap<String, Object> newHashMap = new HashMap();
                    newHashMap.put("status", "Booked");
                    newHashMap.put("level_id", viewsDataFromFragment.get("level_id"));
                    newHashMap.put("level_name", viewsDataFromFragment.get("level_name"));
                    newHashMap.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    newHashMap.put("slot_id", slotModelClass.getSlot_id());
                    newHashMap.put("slot_name", slotModelClass.getSlot_name());
                    newHashMap.put("date", viewsDataFromFragment.get("Date"));
                    newHashMap.put("time", viewsDataFromFragment.get("Time"));
                    newHashMap.put("parking_id", parkingRef.getId());

                    parkingRef.set(newHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            holder.progressBar.setVisibility(View.GONE);
                            slotModelClassArrayList.get(position).setIs_available(false);
                            notifyItemChanged(position);
                            Toasty.success(mContext.get(), "Success", Toasty.LENGTH_LONG).show();
                        }
                    });
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    Toasty.error(mContext.get(), "" + task.getException().getLocalizedMessage(), Toasty.LENGTH_LONG).show();
                    Log.d(TAG, "onComplete: Error: " + task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void setViewsData(HashMap<String, String> viewsData) {
        viewsDataFromFragment = viewsData;
    }

    @Override
    public int getItemCount() {
        return Math.max(slotModelClassArrayList.size(), 0);
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView slotName;
        RelativeLayout mainRelativeLayout, progressBar;
        ImageView carImageView;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);

            slotName = itemView.findViewById(R.id.slot_name);
            mainRelativeLayout = itemView.findViewById(R.id.main_linear_layout);
            progressBar = itemView.findViewById(R.id.slot_item_progress_bar_layout);
            carImageView = itemView.findViewById(R.id.car_image);
        }
    }
}
