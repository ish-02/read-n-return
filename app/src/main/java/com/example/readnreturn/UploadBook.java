package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);

        Button button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);

                EditText editText = findViewById(R.id.editText);
                EditText editText1 = findViewById(R.id.editText3);
                EditText editText2 = findViewById(R.id.editText2);
                EditText editText3 = findViewById(R.id.editText5);

                String name = editText.getText().toString().trim();
                String author = editText3.getText().toString().trim();
                String genre = editText1.getText().toString().trim();
                String description = editText2.getText().toString().trim();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", name);
                    jsonObject.put("author", author);
                    jsonObject.put("genre", genre);
                    jsonObject.put("description", description);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    MainActivity.api.addBook(name, author, genre, description);
                    toast.setText("Book added successfully!");
                    toast.show();
                    finish();
                } catch (HTTPError e) {
                    if(e.code == 403) {
                        toast.setText("Login Again");
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