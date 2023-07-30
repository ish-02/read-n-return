package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MyOrdersPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_page);


        ImageView imageView = findViewById(R.id.imageView8);
        imageView.setOnClickListener(view -> startActivity(new Intent(MyOrdersPage.this, profile.class)));

        loadData();
    }

    void loadData() throws RuntimeException {
        try {

            JSONArray jsonArray = MainActivity.api.getOrders().getJSONArray("orders");

            ListView coursesGV1 = findViewById(R.id.idLV2);
            ArrayList<CourseModel2> courseModelArrayList = new ArrayList<CourseModel2>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject order = jsonArray.getJSONObject(i);
                JSONObject book = order.getJSONObject("book");
                courseModelArrayList.add(new CourseModel2(book.getString("name"), R.drawable.ic_gfglogo, book.getString("author"), order.getString("status")));
            }

            CourseGVAdapter2 adapter1 = new CourseGVAdapter2(getApplicationContext(), courseModelArrayList);
            coursesGV1.setAdapter(adapter1);

        } catch (JSONException | IOException | HTTPError e) {
            throw new RuntimeException(e);
        }

    }
}