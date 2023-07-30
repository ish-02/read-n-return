package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        Button myBooks = findViewById(R.id.button6);
        myBooks.setOnClickListener(view -> startActivity(new Intent(profile.this, MyUploadsPage.class)));


        Button myOrders = findViewById(R.id.button7);
        myOrders.setOnClickListener(view -> startActivity(new Intent(profile.this, MyOrdersPage.class)));


        Button logout = findViewById(R.id.button8);
        logout.setOnClickListener(view -> {
            try {
                boolean jsonObject = MainActivity.api.logout();
                finish();
                Toast.makeText(getApplicationContext(), "Logging out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(profile.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } catch (JSONException | IOException | HTTPError e) {
                throw new RuntimeException(e);
            }
        });
    }
}