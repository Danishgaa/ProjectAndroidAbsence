package com.example.aplikasiabsensi.guru;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.model.Guru;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailDataGuruActivity extends AppCompatActivity {

    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;

    // variable fields EditText dan Button
    private Button btnUpdate;
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

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data_guru);

        initView();
        onClickListener();

        final Guru guru = (Guru) getIntent().getSerializableExtra("data");

        if (guru != null) {
            etEmail.setText(guru.getEmail());
            etId.setText(guru.getId());
            etNama.setText(guru.getNama());
            selectValue(spJurusan, guru.getJurusan());
            if(guru.getJk()=="Male"){
                rbMale.setChecked(true);
                rbFemale.setChecked(false);
            }else{
                rbFemale.setChecked(true);
                rbMale.setChecked(false);
            }
            etNoHp.setText(guru.getNo_hp());
            etAlamat.setText(guru.getAlamat());
            etTglLahir.setText(guru.getTgl_lahir());
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jurusanSpinner = spJurusan.getSelectedItem().toString();

                if (rbMale.isChecked()) {
                    jkSelected = rbMale.getText().toString();
                } else if (rbFemale.isChecked()) {
                    jkSelected = rbFemale.getText().toString();
                }

                if(!isEmpty(etEmail.getText().toString()) &&
                        !isEmpty(etNama.getText().toString()) &&
                        !isEmpty(jurusanSpinner) &&
                        !isEmpty(jkSelected) &&
                        !isEmpty(etNoHp.getText().toString()) &&
                        !isEmpty(etTglLahir.getText().toString()) &&
                        !isEmpty(etAlamat.getText().toString())) {

                    guru.setId(etEmail.getText().toString());
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
        btnUpdate = findViewById(R.id.btn_update);
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
                        Snackbar.make(findViewById(R.id.btn_update), "Data berhasil diupdate", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                        //startActivity(new Intent(ModifyDataGuruActivity.this, ViewDataGuruActivity.class));
                    }
                });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, DetailDataGuruActivity.class);
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