package com.example.readnreturn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class DisplayBooks extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public NavigationView navigation;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_books);

        loadData();

        TextView textView = findViewById(R.id.textView4);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayBooks.this, UploadBook.class));
            }
        });

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplayBooks.this, profile.class));
            }
        });
    }


    public void logout() {
        try {
            boolean didLogout = MainActivity.api.logout();
        } catch (JSONException | IOException | HTTPError e) {
            e.printStackTrace();
        }
        startActivity(new Intent(DisplayBooks.this, MainActivity.class));
    }

    public void loadData() {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        try {
            JSONObject jsonObject = MainActivity.api.getGenres(null);
            JSONArray jsonArray = jsonObject.getJSONArray("genres");
            GridView coursesGV = findViewById(R.id.idGVcourses);
            ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
            for (int i = 0; i < jsonArray.length(); i++) {
                courseModelArrayList.add(new CourseModel(jsonArray.getString(i)));
            }
            coursesGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intent = new Intent(DisplayBooks.this, RecentBooks.class);
                        intent.putExtra("recentBook", jsonArray.getString(position));
                        startActivity(intent);

                    } catch (JSONException e) {
                        toast.setText("couldn't add");
                        toast.show();
                    }

                }
            });
            CourseGVAdapter adapter = new CourseGVAdapter(getApplicationContext(), courseModelArrayList);
            coursesGV.setAdapter(adapter);

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