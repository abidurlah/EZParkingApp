package com.example.ezparkingapp.activities.auth.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ezparkingapp.R;
import com.example.ezparkingapp.activities.HomeActivity;
import com.example.ezparkingapp.activities.auth.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private EditText mNameET, mEmailET, mPasswordET, mPhoneET;
    private Button mSignUpButton;

    private RelativeLayout mProgressBarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initViews(view);

        mSignUpButton.setOnClickListener(this);

        return view;
    }

    private void initViews(View view) {
        mNameET = view.findViewById(R.id.sign_up_name_et);
        mEmailET = view.findViewById(R.id.sign_up_email_et);
        mPasswordET = view.findViewById(R.id.sign_up_password_et);
        mPhoneET = view.findViewById(R.id.sign_up_phone_et);
        mSignUpButton = view.findViewById(R.id.sign_up_button);
        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);
    }

    @Override
    public void onClick(View v) {
        if (v == mSignUpButton) {
            mSignUpButton.setVisibility(View.INVISIBLE);
            mProgressBarLayout.setVisibility(View.VISIBLE);
            String name = mNameET.getText().toString().trim();
            String email = mEmailET.getText().toString().trim();
            String password = mPasswordET.getText().toString().trim();
            String phone = mPhoneET.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toasty.error(requireContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                mSignUpButton.setVisibility(View.VISIBLE);
                mProgressBarLayout.setVisibility(View.GONE);
            } else {
                createNewUser(name, email, password, phone);
            }
        }
    }

    private void createNewUser(final String name, final String email, final String password, final String phone) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uploadUserDataToDatabase(task.getResult().getUser().getUid(),name, email, phone);
                } else {
                    Toasty.error(requireContext(), "" + task.getException().getLocalizedMessage(), Toasty.LENGTH_LONG).show();
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mProgressBarLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void uploadUserDataToDatabase(String uid, String name, String email, String phone) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection(getString(R.string.users_collection));
//        String userId = collectionReference.document().getId();
        DocumentReference documentReference = collectionReference.document(uid);

        UserModel userModel = new UserModel(uid, name, email, phone);
        documentReference.set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toasty.success(requireContext(), "New User Created", Toasty.LENGTH_LONG).show();
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mProgressBarLayout.setVisibility(View.GONE);
                    sendUserToHomeActivity();
                } else {
                    Toasty.error(requireContext(), "" + task.getException().getLocalizedMessage(), Toasty.LENGTH_LONG).show();
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().getCurrentUser().delete();
                    }
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mProgressBarLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void sendUserToHomeActivity() {
        requireActivity().startActivity(new Intent(requireContext(), HomeActivity.class));
        requireActivity().finish();
    }
}