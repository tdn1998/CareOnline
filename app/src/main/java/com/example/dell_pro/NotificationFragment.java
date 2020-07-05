package com.example.dell_pro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell_pro.Model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    RecyclerView recyclerView;
    List<News> newsList;
    private static String JSON_URL = "http://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=01148580376441d49c8e12e36c2b8970";
    Notification_recycler notification_recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.newsrecycler);
        newsList = new ArrayList<>();
        extractSongs();
        return view;
    }

    private void extractSongs() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray array = response.getJSONArray("articles");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject newsObject = array.getJSONObject(i);
                        News news = new News();
                        news.setTitle(newsObject.getString("title").toString());
                        news.setAuthor(newsObject.getString("author".toString()));
                        news.setImgUrl(newsObject.getString("urlToImage"));
                        news.setNewsUrl(newsObject.getString("url"));
                        news.setDate(newsObject.getString("publishedAt").split("T", -1)[0]);
                        newsList.add(news);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                notification_recycler = new Notification_recycler(getContext(), newsList);
                recyclerView.setAdapter(notification_recycler);
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        }
            );
        queue.add(jsonObjectRequest);
    }
}

