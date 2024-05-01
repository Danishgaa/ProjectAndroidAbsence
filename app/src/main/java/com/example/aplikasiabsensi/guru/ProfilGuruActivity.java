package com.example.aplikasiabsensi.guru;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.model.Guru;
import com.example.aplikasiabsensi.model.User;
import com.example.aplikasiabsensi.sign.LoginActivity;
import com.example.aplikasiabsensi.siswa.DashboardMahasiswaActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfilGuruActivity extends AppCompatActivity {

    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;

    // variable fields EditText dan Button
    private Button btnSave, btnLogout;
    private EditText etEmail;
    private EditText etId;
    private EditText etNama;
    private Spinner spJurusan;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText etNoHp;
    private EditText etAlamat;
    private EditText etTglLahir;
    private ImageButton ibKalender;

    private String jkSelected, jurusanSpinner;

    private Guru guru;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public static final String EXTRA_GURU = "extra_guru";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_guru);

        initView();
        onClickListener();

        //final Guru guru = (Guru) getIntent().getSerializableExtra("data");
        guru = getIntent().getParcelableExtra(EXTRA_GURU);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            etEmail.setText(userEmail);

            //Log.d(TAG, userEmail);
            Query query = database.child("guru").orderByChild("email").equalTo(userEmail);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        guru = ds.getValue(Guru.class);
                        //Log.d(TAG, user.getStatus());
                        guru.setKey(ds.getKey());
                        etId.setText(guru.getId());
                        if (guru.getId().equals(" ")){
                            etId.setEnabled(true);
                        }else{
                            etId.setEnabled(false);
                        }
                        etNama.setText(guru.getNama());
                        selectValue(spJurusan, guru.getJurusan());
                        if(guru.getJk().equals("Male")){
                            rbMale.setChecked(true);
                            rbFemale.setChecked(false);
                        }else if(guru.getJk().equals("Male")){
                            rbFemale.setChecked(true);
                            rbMale.setChecked(false);
                        }else{
                            rbFemale.setChecked(false);
                            rbMale.setChecked(false);
                        }
                        etNoHp.setText(guru.getNo_hp());
                        etAlamat.setText(guru.getAlamat());
                        etTglLahir.setText(guru.getTgl_lahir());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            };
            query.addListenerForSingleValueEvent(valueEventListener);
        } else {
            etEmail.setText("-");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jurusanSpinner = spJurusan.getSelectedItem().toString();

                if (rbMale.isChecked()) {
                    jkSelected = rbMale.getText().toString();
                } else if (rbFemale.isChecked()) {
                    jkSelected = rbFemale.getText().toString();
                }

                if(!isEmpty(etId.getText().toString()) &&
                        !isEmpty(etNama.getText().toString()) &&
                        !isEmpty(jurusanSpinner) &&
                        !isEmpty(jkSelected) &&
                        !isEmpty(etNoHp.getText().toString()) &&
                        !isEmpty(etTglLahir.getText().toString()) &&
                        !isEmpty(etAlamat.getText().toString())) {

                    guru.setEmail(etEmail.getText().toString());
                    guru.setId(etId.getText().toString());
                    guru.setNama(etNama.getText().toString());
                    guru.setJurusan(jurusanSpinner);
                    guru.setJk(jkSelected);
                    guru.setNo_hp(etNoHp.getText().toString());
                    guru.setAlamat(etAlamat.getText().toString());
                    guru.setTgl_lahir(etTglLahir.getText().toString());

                    updateGuru(guru);
                }else {
                    Snackbar.make(findViewById(R.id.btn_save), "Data guru tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initView() {
        // inisialisasi fields EditText dan Button
        etEmail = findViewById(R.id.et_email);
        etId = findViewById(R.id.et_id);
        etNama = findViewById(R.id.et_fullname);
        spJurusan = findViewById(R.id.sp_jurusan);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        etNoHp = findViewById(R.id.et_nohp);
        etTglLahir = findViewById(R.id.et_birth);
        etAlamat = findViewById(R.id.et_address);
        ibKalender = findViewById(R.id.ib_calendar);
        btnSave = findViewById(R.id.btn_save);
        btnLogout = findViewById(R.id.btn_logout);

        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    }

    private void onClickListener() {
        ibKalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfilGuruActivity.this, LoginActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                startActivity(intent);
            }
        });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void updateGuru(Guru guru) {
        database.child("guru") //akses parent index, ibaratnya seperti nama tabel
                .child(guru.getKey()) //select barang berdasarkan key
                .setValue(guru) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.btn_save), "Data berhasil diupdate", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(ProfilGuruActivity.this, DashboardGuruActivity.class));
                                finish();
                            }
                        }).show();
                        //startActivity(new Intent(ProfilGuruActivity.this, DashboardGuruActivity.class));
                    }
                });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, ProfilGuruActivity.class);
    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etTglLahir.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}