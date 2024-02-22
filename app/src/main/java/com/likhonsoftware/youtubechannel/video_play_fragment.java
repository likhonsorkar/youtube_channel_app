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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class video_play_fragment extends Fragment {
    WebView video;
    public static String vid_id = "";
    public static String vid_title = "";
    RecyclerView allvideolist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container!=null){
            container.removeAllViews();
        }
        View myview = inflater.inflate(R.layout.video_play_fragment, container, false);
        String iframe_video = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <style>\n" +
                "      body {\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "      }\n" +
                "\n" +
                "      .videoWrapper {\n" +
                "        position: relative;\n" +
                "        padding-bottom: 56.25%; /* 16:9 aspect ratio */\n" +
                "        height: 0;\n" +
                "      }\n" +
                "\n" +
                "      .videoWrapper iframe {\n" +
                "        position: absolute;\n" +
                "        top: 0;\n" +
                "        left: 0;\n" +
                "        width: 100%;\n" +
                "        height: 100%;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"videoWrapper\">\n" +
                "      <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                "      <div id=\"player\"></div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                "      var tag = document.createElement('script');\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                "      //    after the API code downloads.\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '390',\n" +
                "          width: '640',\n" +
                "          videoId: '"+vid_id+"',// Replace this with your video ID\n" +
                "          playerVars: {\n" +
                "            'playsinline': 1\n" +
                "          },\n" +
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      // 4. The API will call this function when the video player is ready.\n" +
                "      function onPlayerReady(event) {\n" +
                "        event.target.playVideo();\n" +
                "      }\n" +
                "\n" +
                "      // 5. The API calls this function when the player's state changes.\n" +
                "      //    The function indicates that when playing a video (state=1),\n" +
                "      //    the player should play for six seconds and then stop.\n" +
                "      var done = false;\n" +
                "      function onPlayerStateChange(event) {\n" +
                "        if (event.data == YT.PlayerState.PLAYING && !done) {\n" +
                "          done = true;\n" +
                "        }\n" +
                "      }\n" +
                "      function stopVideo() {\n" +
                "        player.stopVideo();\n" +
                "      }\n" +
                "    </script>\n" +
                "  </body>\n" +
                "</html>";
        String iframe_video2 = "<html><head><style>body{margin:0;padding:0;} .videoWrapper{position:relative;padding-bottom:56.25%;height:0;} .videoWrapper iframe{position:absolute;top:0;left:0;width:100%;height:100%;}</style></head><body><div class=\"videoWrapper\"><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + vid_id + "\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>";
        video = myview.findViewById(R.id.video);
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
        allvideolist = myview.findViewById(R.id.allvideolist);
        MyAdapter adapter = new MyAdapter();
        allvideolist.setAdapter(adapter);
        allvideolist.setLayoutManager(new LinearLayoutManager(getActivity()));
        allvideolist.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inflate the layout for this fragment
        return myview;
    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewholder>{
        private ArrayList<HashMap<String, String>> shuffledList;
        public MyAdapter() {
            // Create a copy of the original list
            shuffledList = new ArrayList<>(home_fragment.arrayList);
            // Shuffle the list randomly
            Collections.shuffle(shuffledList);
        }
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
            HashMap<String,String> hashMap1 = shuffledList.get(position);
            String thumburl = hashMap1.get("video_thumbnail");
            String video_title = hashMap1.get("video_title");
            String video_views  = hashMap1.get("video_views");
            String video_id = hashMap1.get("video_id");

            holder.title.setText(video_title);
            Picasso.get().load(""+thumburl).into(holder.thumb);
            holder.viewcount.setText(video_views+" Views");
            holder.videoclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Here The video Open Code
                    vid_id = video_id;
                    vid_title = video_title;
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new video_play_fragment());
                    fragmentTransaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return Math.min(shuffledList.size(), 20);
        }

    }
}