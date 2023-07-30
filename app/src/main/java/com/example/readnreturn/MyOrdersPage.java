package com.example.readnreturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MyOrdersPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_page);


        ImageView imageView = findViewById(R.id.imageView8);
        imageView.setOnClickListener(view -> startActivity(new Intent(MyOrdersPage.this, profile.class)));
    }
}