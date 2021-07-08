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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mEmailET, mPasswordET;
    private Button mLoginButton;
    private RelativeLayout mProgressBarLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initViews(view);
        mLoginButton.setOnClickListener(this);



        return view;
    }

    private void initViews(View view) {
        mEmailET = view.findViewById(R.id.login_email_et);
        mPasswordET = view.findViewById(R.id.login_password_et);
        mLoginButton = view.findViewById(R.id.login_button);
        mProgressBarLayout = view.findViewById(R.id.progress_bar_layout);
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginButton) {
            mLoginButton.setVisibility(View.INVISIBLE);
            mProgressBarLayout.setVisibility(View.VISIBLE);
            String email = mEmailET.getText().toString().trim();
            String password = mPasswordET.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                mLoginButton.setVisibility(View.VISIBLE);
                mProgressBarLayout.setVisibility(View.GONE);
            } else {
                signUserWithEmailAndPassword(email, password);
            }
        }
    }

    private void signUserWithEmailAndPassword(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toasty.success(requireContext(), "LoggedIn Successfully", Toast.LENGTH_SHORT).show();
                    mLoginButton.setVisibility(View.VISIBLE);
                    mProgressBarLayout.setVisibility(View.GONE);
                    sendUserToHomeActivity();
                } else {
                    Toasty.error(requireContext(), "" + task.getException().getLocalizedMessage(), Toasty.LENGTH_LONG).show();
                    mLoginButton.setVisibility(View.VISIBLE);
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