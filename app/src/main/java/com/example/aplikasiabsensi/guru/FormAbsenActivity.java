package com.example.aplikasiabsensi.guru;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.model.Absen;
import com.example.aplikasiabsensi.model.Mahasiswa;
import com.example.aplikasiabsensi.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FormAbsenActivity extends AppCompatActivity {

    // variable yang merefers ke Firebase Realtime Database
    private DatabaseReference database;

    // variable fields EditText dan Button
    private Button btnSave;
    private EditText etEmailMhs;
    private EditText etNamaMhs;
    private Spinner spJurusanKelas, spMahasiswa;
    private RadioButton rbAbsen;
    private RadioButton rbMasuk;
    private EditText etKeterangan;
    private EditText etTanggal;
    private ImageButton ibKalender;

    private String jkSelected, jurusanSpinner, emailUser;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    ValueEventListener listener;
    ArrayList<String> spinnerDatalistMahasiswa;
    ArrayAdapter<String> adapterMahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_absen);
        initView();
        onClickListener();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailUser = user.getEmail();
        } else {
            emailUser = "-";
        }

        adapterMahasiswa = new ArrayAdapter<String>(FormAbsenActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerDatalistMahasiswa);
        spMahasiswa.setAdapter(adapterMahasiswa);
        retrieveDataForSpinner();
        //spMahasiswa.setPrompt("--Pilih Mahasiswa--");
        spMahasiswa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String currentString = spMahasiswa.getSelectedItem().toString();
                if(currentString.equals("--Pilih Mahasiswa--")){
                    etEmailMhs.getText().clear();
                    etNamaMhs.getText().clear();
                }else {
                    String[] strEmail = currentString.split("\\(");
                    String[] strEmail2 = strEmail[1].split("\\)");
                    etEmailMhs.setText(strEmail2[0].trim());

                    String[] strNama = currentString.split("\\(");
                    etNamaMhs.setText(strNama[0].trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                etEmailMhs.getText().clear();
                etNamaMhs.getText().clear();
            }

        });

    }

    private void initView() {
        // inisialisasi fields EditText dan Button
        etEmailMhs = findViewById(R.id.et_emailmhs);
        etNamaMhs = findViewById(R.id.et_namamhs);
        spMahasiswa = findViewById(R.id.sp_mahasiswa);
        spJurusanKelas = findViewById(R.id.sp_jurusankelas);
        rbAbsen = findViewById(R.id.rb_absen);
        rbMasuk = findViewById(R.id.rb_masuk);
        etKeterangan = findViewById(R.id.et_keterangan);
        etTanggal = findViewById(R.id.et_date);
        ibKalender = findViewById(R.id.ib_calendar);
        btnSave = findViewById(R.id.btn_save);
        spinnerDatalistMahasiswa= new ArrayList<>();

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jurusanSpinner = spJurusanKelas.getSelectedItem().toString();

                if (rbAbsen.isChecked()) {
                    jkSelected = rbAbsen.getText().toString();
                } else if (rbMasuk.isChecked()) {
                    jkSelected = rbMasuk.getText().toString();
                }

                if(!isEmpty(etEmailMhs.getText().toString()) &&
                        //!isEmpty(etNama.getText().toString()) &&
                        !isEmpty(jurusanSpinner) &&
                        !isEmpty(jkSelected) &&
                        !isEmpty(etKeterangan.getText().toString()) &&
                        !isEmpty(etTanggal.getText().toString()))
                    insertAbsen(new Absen(etEmailMhs.getText().toString(),
                            etNamaMhs.getText().toString(),
                            jurusanSpinner,
                            jkSelected,
                            etKeterangan.getText().toString(),
                            etTanggal.getText().toString(),
                            emailUser));
                else
                    Snackbar.make(findViewById(R.id.btn_save), "Data absen tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        etEmailMhs.getWindowToken(), 0);
            }
        });
    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    private void insertAbsen(Absen absen) {
        database.child("absen").push().setValue(absen).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etEmailMhs.setText("");
                etNamaMhs.setText("");
                //selectValue(spJurusan, mahasiswa.getJurusan());
                rbAbsen.setChecked(false);
                rbMasuk.setChecked(false);
                etKeterangan.setText("");
                etTanggal.setText("");
                Snackbar.make(findViewById(R.id.btn_save), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                //startActivity(new Intent(AddGuruActivity.this, DashboardGuruActivity.class));
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, FormAbsenActivity.class);
    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etTanggal.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void retrieveDataForSpinner(){

        listener = database.child("mahasiswa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                spinnerDatalistMahasiswa.add(0, "--Pilih Mahasiswa--");
                for(DataSnapshot item:snapshot.getChildren()){
                    Mahasiswa mahasiswa = item.getValue(Mahasiswa.class);
                    //Log.d(TAG, user.getStatus());
                    spinnerDatalistMahasiswa.add(mahasiswa.getNama()+" ("+mahasiswa.getEmail()+")");
                }
                adapterMahasiswa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}