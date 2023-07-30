package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RecentBooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_books);
        Bundle bundle = getIntent().getExtras();
        String string = bundle.getString("recentBook");

        if(string == null) {
            finish();
        }

        ImageView imageView = findViewById(R.id.imageView6);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecentBooks.this, profile.class));
            }
        });

        TextView textView = findViewById(R.id.textView5);
        textView.setText(string);

        loadData(string);
    }

    public void loadData(String genre) {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONObject jsonObject = MainActivity.api.getGenres(genre);
            JSONArray jsonArray = jsonObject.getJSONArray("books");
            GridView coursesGV1 = findViewById(R.id.idGVcourses1);
            ArrayList<CourseModel1> courseModelArrayList = new ArrayList<CourseModel1>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject book = jsonArray.getJSONObject(i);
                courseModelArrayList.add(new CourseModel1(book.getString("name"), R.drawable.ic_gfglogo, book.getString("author")));
            }
            coursesGV1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(RecentBooks.this, OrderbookActivity.class);
                    try {
                        intent.putExtra("currentBook", jsonArray.getJSONObject(position).getString("id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
            CourseGVAdapter1 adapter1 = new CourseGVAdapter1(getApplicationContext(), courseModelArrayList);
            coursesGV1.setAdapter(adapter1);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } catch (HTTPError e) {
            if (e.code == 403) {
                toast.setText("Login Again");
                toast.show();
            } else {
                toast.setText(String.valueOf(e.code));
                toast.show();
            }
        }
    }
}