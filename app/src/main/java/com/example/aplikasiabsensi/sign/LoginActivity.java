package com.example.aplikasiabsensi.sign;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.guru.DashboardGuruActivity;
import com.example.aplikasiabsensi.model.User;
import com.example.aplikasiabsensi.siswa.DashboardMahasiswaActivity;
import com.example.aplikasiabsensi.toast.CustomToastAttention;
import com.example.aplikasiabsensi.toast.CustomToastSuccess;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity{

    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvToRegister;
    private static ImageView ivLoading;

    private FirebaseAuth auth;

    ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View rootView = getWindow().getDecorView().getRootView();

        initView();
        onClickListener();

        //CEK KONEKSI
        conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {

            } else {
                new CustomToastAttention().Show_Toast(getApplicationContext(), rootView,"Cek koneksi internet anda.");
            }
        }
    }

    private void initView() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.button_signin);
        tvToRegister = findViewById(R.id.tv_signup);
        ivLoading = findViewById(R.id.iv_loading);
        auth = FirebaseAuth.getInstance();
        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickListener() {
        tvToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampung imputan user
                final String emailUser = etEmail.getText().toString().trim();
                final String passwordUser = etPassword.getText().toString().trim();

                //validasi email dan password
                // jika email kosong
                if (emailUser.isEmpty()) {
                    etEmail.setError("Email tidak boleh kosong");
                }
                // jika email not valid
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()){
                    etEmail.setError("Email tidak valid");
                }
                // jika password kosong
                else if (passwordUser.isEmpty()) {
                    etPassword.setError("Password tidak boleh kosong");
                }
                //jika password kurang dari 6 karakter
                else if (passwordUser.length() < 6) {
                    etPassword.setError("Password minimal terdiri dari 6 karakter");
                } else {
                    login(etEmail.getText().toString(), etPassword.getText().toString(), view);
                }
            }
        });
    }

    private void login(final String email, String password, View view){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    new CustomToastAttention().Show_Toast(getApplicationContext(), view,"Proses Login Gagal.");
                } else{
                    new CustomToastSuccess().Show_Toast(getApplicationContext(), view,"Login Berhasil.");

                    Query query = database.child("user").orderByChild("email").equalTo(email);
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                Log.d(TAG, user.getStatus());
                                if (user.getStatus().equals("Guru")){
                                    startActivity(new Intent(LoginActivity.this, DashboardGuruActivity.class));
                                }else{
                                    startActivity(new Intent(LoginActivity.this, DashboardMahasiswaActivity.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage());
                        }
                    };
                    query.addListenerForSingleValueEvent(valueEventListener);
                }
            }
        });
    }
}