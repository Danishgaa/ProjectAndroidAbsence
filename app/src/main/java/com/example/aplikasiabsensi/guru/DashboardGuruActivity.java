package com.example.aplikasiabsensi.guru;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.siswa.ViewDataMahasiswaActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardGuruActivity extends AppCompatActivity {

    private LinearLayout linMenuAbsen, linMenuDataGuru, linMenuDataMahasiswa, linMenuDataAbsenMahasiswa, linMenuDataAbsenGuru;
    private TextView tvAppName, tvHaloEmail;
    private CircleImageView civProfil;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_guru);

        initView();
        onClickListener();

        tvAppName.setText("My Dashboard");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvHaloEmail.setText("Halo "+user.getEmail());
        } else {
            tvHaloEmail.setText("Halo guru");
        }
    }

    private void initView() {
        linMenuAbsen = findViewById(R.id.lin_menu_absen);
        linMenuDataGuru = findViewById(R.id.lin_menu_data_guru);
        linMenuDataMahasiswa = findViewById(R.id.lin_menu_data_mahasiswa);
        linMenuDataAbsenMahasiswa = findViewById(R.id.lin_menu_data_absen_mahasiswa);
        linMenuDataAbsenGuru = findViewById(R.id.lin_menu_data_absen_guru);
        tvAppName = findViewById(R.id.tv_appname);
        tvHaloEmail = findViewById(R.id.tv_haloemail);
        civProfil = findViewById(R.id.iv_profil);
        auth = FirebaseAuth.getInstance();
    }

    private void onClickListener() {
        civProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardGuruActivity.this, ProfilGuruActivity.class));
            }
        });
        linMenuAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardGuruActivity.this, FormAbsenActivity.class));
            }
        });
        linMenuDataGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardGuruActivity.this, ViewDataGuruActivity.class));
            }
        });
        linMenuDataMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardGuruActivity.this, ViewDataMahasiswaActivity.class));
            }
        });
        linMenuDataAbsenMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardGuruActivity.this, ViewAllDataAbsenActivity.class));
            }
        });
        linMenuDataAbsenGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardGuruActivity.this, ViewDataAbsenGuruActivity.class));
            }
        });
    }
}