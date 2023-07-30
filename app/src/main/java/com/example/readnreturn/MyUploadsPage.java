package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyUploadsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_uploads_page);

        ImageView imageView = findViewById(R.id.imageView9);
        imageView.setOnClickListener(view -> startActivity(new Intent(MyUploadsPage.this, profile.class)));

        loadData();
    }


    void loadData() throws RuntimeException {
        try {
            JSONObject job = MainActivity.api.secure();
            String email = job.getString("email");

            JSONArray jsonArray = MainActivity.api.getBooks(email).getJSONArray("books");

            ListView coursesGV1 = findViewById(R.id.idLV1);
            ArrayList<CourseModel1> courseModelArrayList = new ArrayList<CourseModel1>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject book = jsonArray.getJSONObject(i);
                courseModelArrayList.add(new CourseModel1(book.getString("name"), R.drawable.ic_gfglogo, book.getString("author")));
            }
            coursesGV1.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(MyUploadsPage.this, OrderbookActivity.class);
                intent.putExtra("currentBook", position);
                startActivity(intent);

            });
            CourseGVAdapter1 adapter1 = new CourseGVAdapter1(getApplicationContext(), courseModelArrayList);
            coursesGV1.setAdapter(adapter1);

        } catch (JSONException | IOException | HTTPError e) {
            throw new RuntimeException(e);
        }

    }
}