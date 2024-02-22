package com.likhonsoftware.youtubechannel;

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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class playlist_player_fragment extends Fragment {
    WebView video;
    public  static String postion_id = "";
    public static  String playlist_id = "";
    String vid_id;
    String vid_title;
    RecyclerView allvideolist;
    ArrayList<HashMap<String,String>> videolist = new ArrayList<>();
    HashMap<String,String> hashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        View myview = inflater.inflate(R.layout.fragment_playlist_player_fragment, container, false);
        video = myview.findViewById(R.id.video);
        allvideolist = myview.findViewById(R.id.allvideolist);
        String apiKey = getResources().getString(R.string.youtubeapikey);
        String url2 = "https://www.googleapis.com/youtube/v3/search?key="+apiKey+"&list="+playlist_id+"&part=snippet,id&order=date&maxResults=1000";
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playlist_id+"&key="+apiKey+"&maxResults=100";
        String url3 = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playlist_id+"&key="+apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    Log.d("jsonarray", String.valueOf(items));
                    for (int in = 0; in < items.length(); in++) {
                        JSONObject item = items.getJSONObject(in);
                        Log.d("jsonobject", String.valueOf(item));
                        JSONObject snippet = item.getJSONObject("snippet");
                        String title = snippet.optString("title", "");  // Use optString to provide a default value if key not found
                        String publishedAt = snippet.optString("publishedAt", "");  // Use optString to provide a default value if key not found
                        String description = snippet.optString("description", "");  // Use optString to provide a default value if key not found

                        JSONObject thumbnails = snippet.optJSONObject("thumbnails");  // Use optJSONObject to handle potential null value
                        String thumbnailUrl = "";
                        if (thumbnails != null) {
                            JSONObject mediumThumbnail = thumbnails.optJSONObject("high");  // Use optJSONObject to handle potential null value
                            if (mediumThumbnail != null) {
                                thumbnailUrl = mediumThumbnail.optString("url", "");  // Use optString to provide a default value if key not found
                            }
                        }
                        JSONObject resourceId = snippet.getJSONObject("resourceId");
                        String videoId = resourceId.getString("videoId");
                        Log.d("resourceId", String.valueOf(resourceId));

                        // Add video data to HashMap and ArrayList
                        HashMap<String, String> hasmap = new HashMap<>();
                        hasmap.put("vid_position", String.valueOf(in));
                        hasmap.put("video_id", videoId);
                        hasmap.put("video_title", title);
                        hasmap.put("video_upload_date", publishedAt);
                        hasmap.put("video_description", description);
                        hasmap.put("video_thumbnail", thumbnailUrl);
                        videolist.add(hasmap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Access elements from videolist
                // Example: videolist.get(0)
                videoplayandlist("0");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue que = Volley.newRequestQueue(getActivity());
        que.add(jsonObjectRequest);
        // Inflate the layout for this fragment
        return myview;
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
            HashMap<String,String> hashMap1 = videolist.get(position);
            String vid_postion = hashMap1.get("vid_position");
            String thumburl = hashMap1.get("video_thumbnail");
            String video_title = hashMap1.get("video_title");
            String video_id = hashMap1.get("video_id");

            holder.title.setText(video_title);
            Picasso.get().load(""+thumburl).into(holder.thumb);
            holder.videoclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Here The video Open Code
                    postion_id = String.valueOf(vid_postion);
                    vid_id = video_id;
                    vid_title = video_title;
                    videoplayandlist(postion_id);
                }
            });
        }

        @Override
        public int getItemCount() {
            return videolist.size();
        }

    }
    private void videoplayandlist(String postion_id){
        HashMap<String,String> single_vid_map = videolist.get(Integer.parseInt(postion_id));
        String vid_id = single_vid_map.get("video_id");
        String iframe_video = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n<style>body{margin:0;padding:0;} .videoWrapper{position:relative;padding-bottom:56.25%;height:0;} .videoWrapper iframe{position:absolute;top:0;left:0;width:100%;height:100%;}</style></head><body><div class=\"videoWrapper\"><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + vid_id + "\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>";
        video.getSettings().setJavaScriptEnabled(true);
        video.loadData(iframe_video, "text/html", "utf-8");
        video.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });
        MyAdapter adapter = new MyAdapter();
        allvideolist.setAdapter(adapter);
        allvideolist.setLayoutManager(new LinearLayoutManager(getActivity()));
        allvideolist.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}