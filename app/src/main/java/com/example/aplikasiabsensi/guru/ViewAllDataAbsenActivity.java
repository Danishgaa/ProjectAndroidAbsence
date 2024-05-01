package com.example.aplikasiabsensi.guru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.adapter.AdapterAbsen;
import com.example.aplikasiabsensi.model.Absen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllDataAbsenActivity extends AppCompatActivity {

    private LinearLayout linGangguanInternet, linDataKosong;
    private DatabaseReference database;
    private RecyclerView rvDataAbsen;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Absen> dataAbsen;

    String emailUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_absen);

        initView();
        onClickListener();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailUser = user.getEmail();
        } else {
            emailUser = " ";
        }

        database.child("absen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataAbsen = new ArrayList<>();

                if(dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Absen absen = noteDataSnapshot.getValue(Absen.class);
                        absen.setKey(noteDataSnapshot.getKey());

                        dataAbsen.add(absen);
                    }

                    adapter = new AdapterAbsen(dataAbsen, ViewAllDataAbsenActivity.this);
                    rvDataAbsen.setAdapter(adapter);
                    rvDataAbsen.setVisibility(View.VISIBLE);
                    linGangguanInternet.setVisibility(View.GONE);
                    linDataKosong.setVisibility(View.GONE);
                }else{
                    rvDataAbsen.setVisibility(View.GONE);
                    linGangguanInternet.setVisibility(View.GONE);
                    linDataKosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, ViewAllDataAbsenActivity.class);
    }

    private void initView() {
        linGangguanInternet = findViewById(R.id.lin_gangguan_internet);
        linDataKosong = findViewById(R.id.lin_data_kosong);
        rvDataAbsen = findViewById(R.id.rv_dataabsen);
        rvDataAbsen.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvDataAbsen.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickListener() {

    }
}