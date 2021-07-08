package com.example.ezvote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    Button login_button,register_button;
    EditText student_id, student_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register_button = (Button) findViewById(R.id.register_btn_login);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToRegisterView();
            }
        });
    }

    public void GoToRegisterView() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}