package com.likhonsoftware.youtubechannel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* MD. Likhon Sorkar
           B.Sc in Computer Scirnce & Engineering
           Facebook : https://www.facebook.com/wdLikhonsorkar/
           Linkdin : https://www.linkedin.com/in/likhonsorkar/
           Whatsapp : +8801571230198
         */
        bottomNavigationView = findViewById(R.id.bottom_nav);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentcontainer, new home_fragment());
        fragmentTransaction.commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homebtn){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new home_fragment());
                    fragmentTransaction.commit();
                } else if (item.getItemId() == R.id.playlistbtn) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new playlist_fragment());
                    fragmentTransaction.commit();

                } else if (item.getItemId() == R.id.aboutbtn) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentcontainer, new channel_info_fragment());
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Get the currently visible fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragmentcontainer);

        // Check if the current fragment is VideoPlayFragment
        if (currentFragment instanceof video_play_fragment) {
            // Replace VideoPlayFragment with HomeFragment
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentcontainer, new home_fragment());
            transaction.commit();
        } else if (currentFragment instanceof home_fragment) {
            // Close the app
            super.onBackPressed();
        }
    }
}
