package com.example.aplikasiabsensi.model;

//import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

public class Guru implements Serializable{

    private String key;
    private String email;
    private String id;
    private String nama;
    private String jurusan;
    private String jk;
    private String no_hp;
    private String alamat;
    private String tgl_lahir;

    public Guru(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    @Override
    public String toString() {
        return " "+email+"\n" +
                " "+id+"\n" +
                " "+nama +"\n" +
                " "+jurusan +"\n" +
                " "+jk +"\n" +
                " "+no_hp +"\n" +
                " "+alamat +"\n" +
                " "+tgl_lahir;
    }

    public Guru(String emaill, String idd, String namaa, String jurusann, String jkk, String no_hpp, String alamatt, String tgl_lahirr){
        email = emaill;
        id = idd;
        nama = namaa;
        jurusan = jurusann;
        jk = jkk;
        no_hp = no_hpp;
        alamat = alamatt;
        tgl_lahir = tgl_lahirr;
    }
}