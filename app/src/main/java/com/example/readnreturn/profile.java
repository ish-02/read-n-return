package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            JSONObject jsonObject = MainActivity.api.secure();
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");


            TextView nameView = findViewById(R.id.nameView);
            nameView.setText(name);

            TextView emailView = findViewById(R.id.emailView);
            emailView.setText(email);


        } catch (JSONException | IOException | HTTPError e) {
            throw new RuntimeException(e);
        }

    }
}