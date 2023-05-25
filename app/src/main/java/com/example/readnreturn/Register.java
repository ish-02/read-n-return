package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = findViewById(R.id.button5);
        // login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editText);
                EditText editText1 = findViewById(R.id.editText3);
                EditText editText2 = findViewById(R.id.editText2);

                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String name = editText.getText().toString().trim();
                String email = editText1.getText().toString().trim();
                String password = editText2.getText().toString().trim();
                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                try {
                    JSONObject jsonObject = MainActivity.api.register(name, email, password);
                    try {
                        editor.putString("email", jsonObject.getString("email"));
                        editor.putString("name", jsonObject.getString("name"));
                        editor.apply();
                        toast.setText("Registered successfully!");
                        toast.show();
                        startActivity(new Intent(Register.this, DisplayBooks.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (HTTPError e) {

                    if (e.code == 409) {
                        toast.setText("user already exist");
                        toast.show();
                    } else {
                        toast.setText(String.valueOf(e.code));
                        toast.show();
                    }
                }
            }
        });
    }
}