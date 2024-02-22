package com.likhonsoftware.youtubechannel;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class playlist_fragment extends Fragment {
    RecyclerView allplaylist;
    ArrayList<HashMap<String,String>> playlistarray = new ArrayList<>();
    HashMap<String,String> hashMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        View myview = inflater.inflate(R.layout.playlist_fragment, container, false);
        allplaylist = myview.findViewById(R.id.playlist);
        RequestQueue que = Volley.newRequestQueue(getActivity());
        String apiKey = getResources().getString(R.string.youtubeapikey);
        String channelId = getResources().getString(R.string.channel_id);
        String playlist = "https://www.googleapis.com/youtube/v3/playlists?key="+apiKey+"&channelId="+channelId+"&part=snippet,id&order=date&maxResults=200";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, playlist, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject snippet = item.getJSONObject("snippet");
                        String playlistid = item.getString("id");
                        String title = snippet.getString("title");
                        String description = snippet.getString("description");
                        String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                        String publishedAt = snippet.getString("publishedAt");
                        hashMap = new HashMap<>();
                        hashMap.put("title", title);
                        hashMap.put("description", description);
                        hashMap.put("Thumnail", thumbnailUrl);
                        hashMap.put("pdate", publishedAt);
                        hashMap.put("playlistid", playlistid);
                        playlistarray.add(hashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyAdapter adapter = new MyAdapter();
                allplaylist.setAdapter(adapter);
                allplaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
                allplaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        que.add(jsonObjectRequest);
        return myview;
    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewholder>{
        private class MyViewholder extends RecyclerView.ViewHolder{
            ImageView thumb;
            TextView title, time, description;
            LinearLayout playlistclick;
            public MyViewholder(@NonNull View itemView) {
                super(itemView);
                thumb = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                time = itemView.findViewById(R.id.time);
                description = itemView.findViewById(R.id.description);
                playlistclick = itemView.findViewById(R.id.playlistclick);
            }
        }

        @NonNull
        @Override
        public MyAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.playlist_item, parent, false);
            return new MyAdapter.MyViewholder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewholder holder, int position) {
            HashMap<String,String> hashMap1 = playlistarray.get(position);
            String thumburl = hashMap1.get("Thumnail");
            String video_title = hashMap1.get("title");
            String description = hashMap1.get("description");
            String create_time = hashMap1.get("pdate");
            String playlistid = hashMap1.get("playlistid");
            holder.title.setText(video_title);
            holder.description.setText(description);
            Picasso.get()
                    .load(""+thumburl)
                    .into(holder.thumb);
            holder.time.setText("Created Time: "+create_time);
            holder.playlistclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playlist_player_fragment.postion_id = "0";
                    playlist_player_fragment.playlist_id = playlistid;
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new playlist_player_fragment());
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return playlistarray.size();
        }

    }
}