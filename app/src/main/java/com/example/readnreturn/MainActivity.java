package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.api = new Api("http://192.168.232.13:5000", getApplicationContext());

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        try {
            JSONObject jsonObject = MainActivity.api.secure();
            if(jsonObject != null) {
                startActivity(new Intent(MainActivity.this, DisplayBooks.class));
                finish();
            }
        } catch (JSONException | IOException | HTTPError e) {
            Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
        }


        Button button = findViewById(R.id.button);
        // login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.inputName);
                EditText editText1 = findViewById(R.id.inputPassword);

                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String email = editText.getText().toString().trim();
                String password = editText1.getText().toString().trim();
                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                try {
                    JSONObject jsonObject = MainActivity.api.login(email, password);
                    try {
                        editor.putString("email", jsonObject.getString("email"));
                        editor.putString("name", jsonObject.getString("name"));
                        editor.apply();

                        toast.setText("Logged in successfully!");
                        toast.show();

                        startActivity(new Intent(MainActivity.this, DisplayBooks.class));
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (HTTPError e) {

                    switch (e.code) {
                        case 404: {
                            toast.setText("user doesn't exist");
                            toast.show();
                        }
                        case 405: {
                            toast.setText("Invalid email or password");
                            toast.show();
                        }
                        default: {
                            toast.setText(String.valueOf(e.code));
                            toast.show();
                        }
                    }
                }

            }
        });

        Button button1 = findViewById(R.id.button3);
        // register
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

    }
}