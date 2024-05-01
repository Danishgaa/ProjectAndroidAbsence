package com.example.aplikasiabsensi.siswa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.adapter.AdapterAbsen;
import com.example.aplikasiabsensi.adapter.AdapterDaftarMahasiswa;
import com.example.aplikasiabsensi.guru.ViewAllDataAbsenActivity;
import com.example.aplikasiabsensi.model.Absen;
import com.example.aplikasiabsensi.model.Mahasiswa;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDataMahasiswaActivity extends AppCompatActivity implements AdapterDaftarMahasiswa.FirebaseDataListener {

    private LinearLayout linGangguanInternet, linDataKosong;
    private DatabaseReference database;
    private RecyclerView rvDataMahasiswa;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Mahasiswa> dataMahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_mahasiswa);

        initView();
        onClickListener();

        database.child("mahasiswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataMahasiswa = new ArrayList<>();

                if(dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Mahasiswa mahasiswa = noteDataSnapshot.getValue(Mahasiswa.class);
                        mahasiswa.setKey(noteDataSnapshot.getKey());

                        dataMahasiswa.add(mahasiswa);
                    }

                    adapter = new AdapterDaftarMahasiswa(dataMahasiswa, ViewDataMahasiswaActivity.this);
                    rvDataMahasiswa.setAdapter(adapter);
                    rvDataMahasiswa.setVisibility(View.VISIBLE);
                    linGangguanInternet.setVisibility(View.GONE);
                    linDataKosong.setVisibility(View.GONE);
                }else{
                    rvDataMahasiswa.setVisibility(View.GONE);
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
        return new Intent(activity, ViewDataMahasiswaActivity.class);
    }

    private void initView() {
        linGangguanInternet = findViewById(R.id.lin_gangguan_internet);
        linDataKosong = findViewById(R.id.lin_data_kosong);
        rvDataMahasiswa = findViewById(R.id.rv_datamahasiswa);
        rvDataMahasiswa.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvDataMahasiswa.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickListener() {

    }

    @Override
    public void onDeleteData(Mahasiswa mahasiswa, final int position) {
        if (database != null) {
            database.child("mahasiswa").child(mahasiswa.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ViewDataMahasiswaActivity.this, "Berhasil delete data", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}