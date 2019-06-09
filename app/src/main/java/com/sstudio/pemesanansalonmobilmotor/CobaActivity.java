package com.sstudio.pemesanansalonmobilmotor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sstudio.pemesanansalonmobilmotor.Fragments.HomeFragment;
import com.sstudio.pemesanansalonmobilmotor.Fragments.ShoppingFragment;
import com.sstudio.pemesanansalonmobilmotor.listener.IAllSalonLoadListener;

import java.util.ArrayList;
import java.util.List;

public class CobaActivity extends AppCompatActivity {

    CollectionReference allSalonRef;
    IAllSalonLoadListener iAllSalonLoadListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba);
        allSalonRef = FirebaseFirestore.getInstance().collection("AllSalon");
        BottomNavigationView bn_home = (BottomNavigationView) findViewById(R.id.bn_home);

        allSalonRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<String> list = new ArrayList<>();
                            list.add("Silahkan pilih kota");
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                list.add(queryDocumentSnapshot.getId());
                            }
                            iAllSalonLoadListener.onAllSalonLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllSalonLoadListener.onAllSalonLoadFailed(e.getMessage());
            }
        });
    }


}
