package com.example.ezparkingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.activities.auth.SignInOrSignUpActivity;
import com.example.ezparkingapp.activities.auth.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private EditText mNameET, mEmailET,mPhoneET;
    private Button updateProfileButton, logoutButton;
    private RelativeLayout mProgressBarLayout;
    private ConstraintLayout mainLayout;

    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        onClickListeners();
        getUserInfo();
        return view;
    }

    private void getUserInfo() {

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    mNameET.setText(userModel.getName());
                    mEmailET.setText(userModel.getEmail());
                    mPhoneET.setText(userModel.getPhone());
                    mainLayout.setVisibility(View.VISIBLE);
                    mProgressBarLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void onClickListeners() {
        logoutButton.setOnClickListener(this);
        updateProfileButton.setOnClickListener(this);
    }

    private void initViews(View view) {
        mNameET = view.findViewById(R.id.profile_name_et);
        mPhoneET = view.findViewById(R.id.profile_phone_et);
        mEmailET = view.findViewById(R.id.profile_email_et);

        mainLayout = view.findViewById(R.id.main_constraint_layout);

        updateProfileButton = view.findViewById(R.id.profile_update_button);
        logoutButton = view.findViewById(R.id.logout_button);

        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);

        userRef = FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onClick(View v) {
        if (v == logoutButton) {
            FirebaseAuth.getInstance().signOut();
            requireActivity().startActivity(new Intent(requireContext(), SignInOrSignUpActivity.class));
            requireActivity().finish();
        } else if (v == updateProfileButton) {
            updateProfileButton.setVisibility(View.INVISIBLE);
            mProgressBarLayout.setVisibility(View.VISIBLE);
            String name = mNameET.getText().toString().trim();
            String phone = mPhoneET.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toasty.error(requireContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                updateProfileButton.setVisibility(View.VISIBLE);
                mProgressBarLayout.setVisibility(View.GONE);
            } else {
                updateUser(name, phone);
            }
        }
    }

    private void updateUser(String name, String phone) {

        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("name", name);
        hashMap.put("phone", phone);

        userRef.update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressBarLayout.setVisibility(View.GONE);
                updateProfileButton.setVisibility(View.VISIBLE);
                Toasty.success(requireContext(), "Updated", Toasty.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBarLayout.setVisibility(View.GONE);
                updateProfileButton.setVisibility(View.VISIBLE);
                Toasty.error(requireContext(), "Error: " + e, Toasty.LENGTH_LONG).show();
            }
        });
    }
}