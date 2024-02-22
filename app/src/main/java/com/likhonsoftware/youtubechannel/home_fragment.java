package com.likhonsoftware.youtubechannel;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class home_fragment extends Fragment {
    RecyclerView allvideolist;
    public static ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hasmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            arrayList.clear();
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View Myview = inflater.inflate(R.layout.home_fragment, container, false);
        allvideolist = Myview.findViewById(R.id.allvideolist);
        RequestQueue que = Volley.newRequestQueue(getActivity());
        // Using Youtube Api To Data
        String apiKey = getResources().getString(R.string.youtubeapikey);
        String channelId = getResources().getString(R.string.channel_id);
        String maxresult = getResources().getString(R.string.max_result);
        String urlString = "https://www.googleapis.com/youtube/v3/search?key="+apiKey+"&channelId=" + channelId + "&part=snippet,id&order=date&maxResults="+maxresult;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    Log.d("jsonarray", String.valueOf(items));
                    for (int in = 0; in<items.length(); in++){
                        JSONObject item = items.getJSONObject(in);
                        JSONObject id = item.getJSONObject("id");
                        String Kind = id.getString("kind");
                        if (Kind.contains("youtube#video")){
                            JSONObject snippet = item.getJSONObject("snippet");
                            String title = snippet.getString("title");
                            String publishedAt = snippet.getString("publishedAt");
                            String description = snippet.getString("description");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject mediumThumbnail = thumbnails.getJSONObject("high");
                            String thumbnailUrl = mediumThumbnail.getString("url");
                            String videoId = item.getJSONObject("id").getString("videoId");
                            // Add video data to HashMap and ArrayList
                            HashMap<String, String> hasmap = new HashMap<>();
                            hasmap.put("video_id", videoId);
                            hasmap.put("video_title", title);
                            hasmap.put("video_upload_date", publishedAt);
                            hasmap.put("video_description", description);
                            hasmap.put("video_thumbnail", thumbnailUrl);
                            arrayList.add(hasmap);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyAdapter adapter = new MyAdapter();
                allvideolist.setAdapter(adapter);
                allvideolist.setLayoutManager(new LinearLayoutManager(getActivity()));
                allvideolist.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        que.add(jsonObjectRequest);

        return Myview;
    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewholder>{
        private class MyViewholder extends RecyclerView.ViewHolder{
            ImageView thumb;
            TextView title, viewcount;
            LinearLayout videoclick;
            public MyViewholder(@NonNull View itemView) {
                super(itemView);
                thumb = itemView.findViewById(R.id.image);
                title = itemView.findViewById(R.id.title);
                viewcount = itemView.findViewById(R.id.views);
                videoclick = itemView.findViewById(R.id.videoclick);
            }
        }

        @NonNull
        @Override
        public MyAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View myView = inflater.inflate(R.layout.allvideoitem, parent, false);
            return new MyAdapter.MyViewholder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewholder holder, int position) {
            HashMap<String,String> hashMap1 = arrayList.get(position);
            String thumburl = hashMap1.get("video_thumbnail");
            String video_title = hashMap1.get("video_title");
            String video_views  = hashMap1.get("video_views");
            String video_id = hashMap1.get("video_id");

            holder.title.setText(video_title);
            Picasso.get()
                    .load(""+thumburl)
                    .into(holder.thumb);
            holder.viewcount.setText(video_views+" Views");
            holder.videoclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Here The video Open Code
                    video_play_fragment.vid_id = video_id;
                    video_play_fragment.vid_title = video_title;
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new video_play_fragment());
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

    }
}