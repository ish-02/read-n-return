package com.example.readnreturn;

import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class HTTPError extends Throwable {
    public int code;
    HTTPError(int code) {
        this.code = code;
    }
}

public class Api {

    private final OkHttpClient client;
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final String apiURL;


    public Api(String apiURL, Context context) {
        this.apiURL = apiURL;
        this.client = new OkHttpClient.Builder()
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)))
                .build();
    }

    JSONObject toJson(String raw) throws JSONException {
        return new JSONObject(raw);
    }

    JSONObject post(String path, String json) throws IOException, JSONException, HTTPError {
        RequestBody body = RequestBody.create(json, this.JSON);
        Request request = new Request.Builder()
                .url(this.apiURL.concat(path))
                .post(body)
                .build();
        try (Response response = this.client.newCall(request).execute()) {
            if(response.code() == 200) {
                if(response.body() != null) {
                    Log.d("", response.body().toString());
                    return this.toJson(response.body().string());
                }
            } else {
                throw new HTTPError(response.code());
            }
            return new JSONObject();
        }
    }

    JSONObject get(String path) throws IOException, JSONException, HTTPError {
        Request request = new Request.Builder()
                .url(this.apiURL.concat(path))
                .get()
                .build();
        try (Response response = this.client.newCall(request).execute()) {
            if(response.code() == 200) {
                if(response.body() != null) {
                    Log.d("", response.body().toString());
                    return this.toJson(response.body().string());
                }
            } else {
                throw new HTTPError(response.code());
            }
            return new JSONObject();
        }
    }

    JSONObject login(String email, String password) throws HTTPError {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return this.post("/login", jsonObject.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    JSONObject register(String name, String email, String password) throws HTTPError {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return this.post("/register", jsonObject.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    JSONObject addBook(String name, String author, String genre, String description) throws HTTPError {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("author", author);
            jsonObject.put("genre", genre);
            jsonObject.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return this.post("/add-book", jsonObject.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    JSONObject getGenres(String genre) throws JSONException, IOException, HTTPError {
        if(genre != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("genre", genre);

            return this.post("/genres", jsonObject.toString());
        }
        return this.get("/genres");
    }

    JSONObject getBooks(String email) throws JSONException, IOException, HTTPError {
        if(email != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);

            return this.post("/books", jsonObject.toString());
        }
        return this.get("/books");
    }

    JSONObject getBook(String bookid) throws JSONException, IOException, HTTPError {
        return this.get("/books/" + bookid);
    }

    JSONObject secure() throws JSONException, IOException, HTTPError {
        return this.get("/secure");
    }

    JSONObject deleteBook(int bookId) throws JSONException, IOException, HTTPError {
        return this.get("/delete-book/" + String.valueOf(bookId));
    }

    boolean logout() throws JSONException, IOException, HTTPError {
        this.get("/logout");
        return true;
    }
}
