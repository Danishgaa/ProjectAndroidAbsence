package com.example.aplikasiabsensi.model;

//import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

public class Absen implements Serializable{

    private String key;
    private String email;
    private String nama;
    private String jurusan_kelas;
    private String absensi;
    private String keterangan;
    private String tanggal;
    private String create_by;

    public Absen(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJurusan_kelas() {
        return jurusan_kelas;
    }

    public void setJurusan_kelas(String jurusan_kelas) {
        this.jurusan_kelas = jurusan_kelas;
    }

    public String getAbsensi() {
        return absensi;
    }

    public void setAbsensi(String absensi) {
        this.absensi = absensi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    @Override
    public String toString() {
        return " "+email +"\n" +
                " "+nama +"\n" +
                " "+jurusan_kelas +"\n" +
                " "+absensi +"\n" +
                " "+keterangan +"\n" +
                " "+tanggal +"\n" +
                " "+create_by;
    }

    public Absen(String emaill, String namaa, String jurusan_kelass, String absensii, String keterangann, String tanggall, String create_byy){
        email = emaill;
        nama = namaa;
        jurusan_kelas = jurusan_kelass;
        absensi = absensii;
        keterangan = keterangann;
        tanggal = tanggall;
        create_by = create_byy;
    }
}