package com.example.readnreturn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class OrderbookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderbook);

        Bundle bundle = getIntent().getExtras();
        int string = bundle.getInt("currentBook");

        ImageView imageView = findViewById(R.id.imageView7);
        imageView.setOnClickListener(view -> startActivity(new Intent(OrderbookActivity.this, profile.class)));

        loadData(string + 1);
    }

    public void loadData(Integer book) {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONObject jsonObject = MainActivity.api.getBook(book.toString());
            JSONObject bookObj = jsonObject.getJSONObject("book");

            TextView bookName = findViewById(R.id.editTextText);
            TextView author = findViewById(R.id.editTextText2);
            TextView description = findViewById(R.id.Text4);

            bookName.setText(bookObj.getString("name"));
            author.setText(bookObj.getString("author"));
            description.setText(bookObj.getString("description"));


            JSONObject user = MainActivity.api.secure();
            String email = user.getString("email");

            Button orderBook = findViewById(R.id.button4);


            if(email.equals(bookObj.getJSONObject("owner").getString("email"))) {
                orderBook.setText(R.string.delete_book);
                orderBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = MainActivity.api.deleteBook(bookObj.getInt("id"));

                            if(jsonObject1 != null) {
                                toast.setText("Book deleted");
                                toast.show();

                                finish();

                            }

                        } catch (JSONException | IOException | HTTPError e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } else {
                orderBook.setText(R.string.request_book);
                orderBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }



        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HTTPError | IOException e) {
            throw new RuntimeException(e);
        }
    }
}