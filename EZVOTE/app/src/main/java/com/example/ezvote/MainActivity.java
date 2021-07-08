package com.example.ezvote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView hello,have_account;
    EditText id,name,course;
    Button register;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = (EditText) findViewById(R.id.student_id);
        name = (EditText) findViewById(R.id.student_name);
        course = (EditText) findViewById(R.id.student_course);
        register = (Button) findViewById(R.id.register_button);
//        have_account = (TextView) findViewById(R.id.Already_have_account);
        hello = (TextView) findViewById(R.id.hello);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

//        have_account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GoToLoginView();
//            }
//        });
    }

//    public void GoToLoginView(){
//        Intent intent = new Intent(this,login.class);
//        startActivity(intent);
//    }

    private void registerUser(){
        String student_id = id.getText().toString().trim();
        String student_name = name.getText().toString().trim();
        String student_course = course.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("student_id",student_id);
                params.put("student_name",student_name);
                params.put("student_course",student_course);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == register)
            registerUser();
    }
}