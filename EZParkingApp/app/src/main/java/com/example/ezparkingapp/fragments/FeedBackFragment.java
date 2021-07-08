package com.example.ezparkingapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ezparkingapp.R;

import es.dmoral.toasty.Toasty;

public class FeedBackFragment extends Fragment {
    private EditText mSubjectET, mMessageET;
    private Button sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_back, container, false);
        mSubjectET = view.findViewById(R.id.feedback_subject_et);
        mMessageET = view.findViewById(R.id.feedback_message_et);
        sendButton = view.findViewById(R.id.feedback_send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailIntent();
            }
        });


        return view;
    }

    private void sendEmailIntent() {
        String subject = mSubjectET.getText().toString();
        String message = mMessageET.getText().toString();

        if (subject.isEmpty() || message.isEmpty()) {
            Toasty.error(requireContext(), "Please Fill all Fields", Toasty.LENGTH_LONG).show();
        } else {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","ezparkingproject@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(emailIntent, "Send feedback"));
        }
    }
}