package com.example.aplikasiabsensi.siswa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.adapter.AdapterAbsen;
import com.example.aplikasiabsensi.guru.DashboardGuruActivity;
import com.example.aplikasiabsensi.guru.ProfilGuruActivity;
import com.example.aplikasiabsensi.guru.ViewDataAbsenGuruActivity;
import com.example.aplikasiabsensi.model.Absen;
import com.example.aplikasiabsensi.sign.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardMahasiswaActivity extends AppCompatActivity {

    private LinearLayout linGangguanInternet, linDataKosong;
    private TextView tvAppName, tvHaloEmail;
    private CircleImageView civProfil;

    private DatabaseReference database;
    private RecyclerView rvDataAbsen;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Absen> dataAbsen;

    String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_mahasiswa);

        initView();
        onClickListener();

        tvAppName.setText("Absensi Saya");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvHaloEmail.setText("Halo "+user.getEmail());
            emailUser = user.getEmail();
        } else {
            tvHaloEmail.setText("Halo mahasiswa");
            emailUser = " ";
        }

        database.child("absen").orderByChild("email").equalTo(emailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataAbsen = new ArrayList<>();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                        Absen absen = noteDataSnapshot.getValue(Absen.class);
                        absen.setKey(noteDataSnapshot.getKey());

                        dataAbsen.add(absen);
                    }

                    adapter = new AdapterAbsen(dataAbsen, DashboardMahasiswaActivity.this);
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

    private void initView() {
        linGangguanInternet = findViewById(R.id.lin_gangguan_internet);
        linDataKosong = findViewById(R.id.lin_data_kosong);
        tvAppName = findViewById(R.id.tv_appname);
        tvHaloEmail = findViewById(R.id.tv_haloemail);
        civProfil = findViewById(R.id.iv_profil);
        rvDataAbsen = findViewById(R.id.rv_dataabsen);
        rvDataAbsen.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvDataAbsen.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickListener() {
        civProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardMahasiswaActivity.this, ProfilMahasiswaActivity.class));
            }
        });
    }

}