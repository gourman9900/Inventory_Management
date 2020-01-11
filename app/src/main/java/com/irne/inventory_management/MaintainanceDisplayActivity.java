package com.irne.inventory_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MaintainanceDisplayActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Maintainance> listdata;
    private MaintainanceAdapter maintainanceAdapter;
    private TextView maintainance_sn_display;
    private LottieAnimationView animation_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainance_display);

        Intent intent = getIntent();
        String sn = intent.getStringExtra("sn");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listdata = new ArrayList<>();
        maintainance_sn_display = (TextView) findViewById(R.id.maintainance_sn_display);
        animation_loading = (LottieAnimationView) findViewById(R.id.animation_loading);
        maintainance_sn_display.setText(sn);

        final DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Maintainance").child(sn);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Maintainance maintainance = dataSnapshot1.getValue(Maintainance.class);
                            listdata.add(maintainance);
                            Log.i("data",listdata.toString());
                        }
                        maintainanceAdapter = new MaintainanceAdapter(listdata);
                        recyclerView.setAdapter(maintainanceAdapter);
                        animation_loading.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
