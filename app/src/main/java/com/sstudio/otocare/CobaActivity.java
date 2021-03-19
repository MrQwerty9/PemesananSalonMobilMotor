package com.sstudio.otocare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.sstudio.otocare.listener.IAllSalonLoadListener;

public class CobaActivity extends AppCompatActivity {

    CollectionReference allSalonRef;
    IAllSalonLoadListener iAllSalonLoadListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);

    }


}
