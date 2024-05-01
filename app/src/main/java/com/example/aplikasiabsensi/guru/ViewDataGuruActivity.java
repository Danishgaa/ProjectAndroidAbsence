package com.example.aplikasiabsensi.guru;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.adapter.AdapterAbsen;
import com.example.aplikasiabsensi.adapter.AdapterDaftarGuru;
import com.example.aplikasiabsensi.model.Absen;
import com.example.aplikasiabsensi.model.Guru;
import com.example.aplikasiabsensi.siswa.DashboardMahasiswaActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDataGuruActivity extends AppCompatActivity implements AdapterDaftarGuru.FirebaseDataListener {

    private LinearLayout linGangguanInternet, linDataKosong;
    private DatabaseReference database;
    private RecyclerView rvDataGuru;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Guru> daftarGuru;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_data_guru);

        initView();
        onClickListener();

        database.child("guru").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarGuru = new ArrayList<>();

                if(dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Guru guru = noteDataSnapshot.getValue(Guru.class);
                        guru.setKey(noteDataSnapshot.getKey());

                        daftarGuru.add(guru);
                    }

                    adapter = new AdapterDaftarGuru(daftarGuru, ViewDataGuruActivity.this);
                    rvDataGuru.setAdapter(adapter);
                    rvDataGuru.setVisibility(View.VISIBLE);
                    linGangguanInternet.setVisibility(View.GONE);
                    linDataKosong.setVisibility(View.GONE);
                }else{
                    rvDataGuru.setVisibility(View.GONE);
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
        return new Intent(activity, ViewDataGuruActivity.class);
    }

    private void initView() {
        linGangguanInternet = findViewById(R.id.lin_gangguan_internet);
        linDataKosong = findViewById(R.id.lin_data_kosong);
        rvDataGuru = findViewById(R.id.rv_dataguru);
        rvDataGuru.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvDataGuru.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickListener() {

    }

    @Override
    public void onDeleteData(Guru guru, final int position) {
        if(database!=null){database.child("guru").child(guru.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ViewDataGuruActivity.this,"Berhasil delete data", Toast.LENGTH_LONG).show();
            }
        });

        }
    }
}