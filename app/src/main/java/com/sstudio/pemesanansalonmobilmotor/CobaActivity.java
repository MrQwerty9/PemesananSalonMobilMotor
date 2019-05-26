package com.sstudio.pemesanansalonmobilmotor;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sstudio.pemesanansalonmobilmotor.Fragments.HomeFragment;
import com.sstudio.pemesanansalonmobilmotor.Fragments.ShoppingFragment;

public class CobaActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);
        BottomNavigationView bn_home = (BottomNavigationView) findViewById(R.id.bn_home);
        bn_home.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if (menuItem.getItemId() == R.id.action_shopping)
                    fragment = new ShoppingFragment();
                return true;
            }
        });
    }

}
