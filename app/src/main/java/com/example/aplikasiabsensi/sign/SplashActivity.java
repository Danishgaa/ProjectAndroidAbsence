package com.example.aplikasiabsensi.sign;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.guru.DashboardGuruActivity;
import com.example.aplikasiabsensi.model.User;
import com.example.aplikasiabsensi.siswa.DashboardMahasiswaActivity;
import com.example.aplikasiabsensi.toast.CustomToastAttention;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private int waktu_loading=4000;

    private static ImageView ivlogo;
    private static TextView tvpengembang;

    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;
    ConnectivityManager conMgr;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivlogo=(ImageView)findViewById(R.id.ivLogo);
        tvpengembang=(TextView) findViewById(R.id.tvPengembang);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        View rootView = getWindow().getDecorView().getRootView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah activity

                //CEK KONEKSI
                conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        //ivLoading.setVisibility(View.VISIBLE);
                        if (user != null) {
                            Log.d(TAG, "Sudah login");
                            String userEmail = user.getEmail();
                            Query query = database.child("user").orderByChild("email").equalTo(userEmail);
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                        User user = ds.getValue(User.class);
                                        Log.d(TAG, user.getStatus());
                                        if (user.getStatus().equals("Guru")){
                                            startActivity(new Intent(SplashActivity.this, DashboardGuruActivity.class));
                                        }else{
                                            startActivity(new Intent(SplashActivity.this, DashboardMahasiswaActivity.class));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d(TAG, databaseError.getMessage());
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                    new CustomToastAttention().Show_Toast(getApplicationContext(), rootView,"Cek koneksi internet anda.");
                                }
                            };
                            query.addListenerForSingleValueEvent(valueEventListener);
                            //ivLoading.setVisibility(View.GONE);
                        } else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            new CustomToastAttention().Show_Toast(getApplicationContext(), rootView,"Silahkan login terlebih dahulu.");
                            //ivLoading.setVisibility(View.GONE);
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        new CustomToastAttention().Show_Toast(getApplicationContext(), rootView,"Cek koneksi internet anda.");
                    }
                }
            }
        },waktu_loading);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.left_to_right);
        Animation myanim2 = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        ivlogo.startAnimation(myanim);
        tvpengembang.startAnimation(myanim2);
    }
}
