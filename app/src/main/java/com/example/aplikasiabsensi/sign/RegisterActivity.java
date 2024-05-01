package com.example.aplikasiabsensi.sign;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aplikasiabsensi.R;

import com.example.aplikasiabsensi.model.Guru;
import com.example.aplikasiabsensi.model.Mahasiswa;
import com.example.aplikasiabsensi.model.User;
import com.example.aplikasiabsensi.toast.CustomToastAttention;
import com.example.aplikasiabsensi.toast.CustomToastSuccess;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;

    ConnectivityManager conMgr;

    private EditText etEmail,etPassword,etConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private Spinner spStatus;

    private String statusSpinner;

    //private FirebaseAuth fAuth;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener fStateListener;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View rootView = getWindow().getDecorView().getRootView();

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

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User sedang login
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User sedang logout
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        initView();
        onClickListener();

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }
        });
    }

    private void initView() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_passwordSignup);
        etConfirmPassword = findViewById(R.id.et_confirmpassword);
        btnRegister = findViewById(R.id.button_signup);
        tvBackToLogin = findViewById(R.id.tv_login);
        spStatus = findViewById(R.id.sp_status);
        auth = FirebaseAuth.getInstance();
        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();
    }

    private void onClickListener() {
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //menampung imputan user
                String emailUser = etEmail.getText().toString().trim();
                String passwordUser = etPassword.getText().toString().trim();
                String confirmPasswordUser = etConfirmPassword.getText().toString().trim();

                //validasi email dan password
                // jika email kosong
                if (emailUser.isEmpty()){
                    etEmail.setError("Email tidak boleh kosong");
                }
                // jika email not valid
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()){
                    etEmail.setError("Email tidak valid");
                }
                // jika password kosong
                else if (passwordUser.isEmpty()){
                    etPassword.setError("Password tidak boleh kosong");
                }
                //jika password kurang dari 6 karakter
                else if (passwordUser.length() < 6){
                    etPassword.setError("Password minimal terdiri dari 6 karakter");
                }
                // jika confirm password kosong
                else if (confirmPasswordUser.isEmpty()){
                    etConfirmPassword.setError("Confirm Password tidak boleh kosong");
                }
                //jika confirm password kurang dari 6 karakter
                else if (confirmPasswordUser.length() < 6){
                    etConfirmPassword.setError("Confirm Password minimal terdiri dari 6 karakter");
                }
                //jika confirm password beda dengan password
                //else if (confirmPasswordUser != passwordUser ){
                    //etConfirmPassword.setError("Confirm Password berbeda dengan password");
                //}
                else {
                    register(etEmail.getText().toString(), etPassword.getText().toString(), view);
                }
            }
        });
    }

    private void register(final String email, String password, View view){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            task.getException().printStackTrace();
                            new CustomToastAttention().Show_Toast(getApplicationContext(), view,"Gagal mendaftar.");
                        } else {
                            statusSpinner = spStatus.getSelectedItem().toString();
                            insertUser(new User(etEmail.getText().toString(), statusSpinner));
                            /*if(statusSpinner.equals("Guru")){
                                insertGuru(new Guru(email," "," "," "," "," "," "," "));
                            }else if(statusSpinner.equals("Mahasiswa")){
                                insertMahasiswa(new Mahasiswa(email," "," "," "," "," "," "," "));
                            }*/
                            FirebaseAuth.getInstance().signOut();
                            new CustomToastSuccess().Show_Toast(getApplicationContext(), view,"Berhasil mendaftar.");
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

    }

    private void insertUser(User user) {
        database.child("user").push().setValue(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(user.getStatus().equals("Guru")){
                    insertGuru(new Guru(user.getEmail()," "," "," "," "," "," "," "));
                }else{
                    insertMahasiswa(new Mahasiswa(user.getEmail()," "," "," "," "," "," "," "));
                }
                etEmail.setText("");
                etPassword.setText("");
                etConfirmPassword.setText("");
            }
        });
    }

    private void insertGuru(Guru guru) {
        database.child("guru").push().setValue(guru).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Push data guru berhasil
            }
        });
    }

    private void insertMahasiswa(Mahasiswa mahasiswa) {
        database.child("mahasiswa").push().setValue(mahasiswa).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Push data mahasiswa berhasil
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(fStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fStateListener != null) {
            auth.removeAuthStateListener(fStateListener);
        }
    }

}