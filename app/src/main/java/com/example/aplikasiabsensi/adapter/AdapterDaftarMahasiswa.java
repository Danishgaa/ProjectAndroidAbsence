package com.example.aplikasiabsensi.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.guru.DetailDataGuruActivity;
import com.example.aplikasiabsensi.model.Mahasiswa;
import com.example.aplikasiabsensi.siswa.ViewDataMahasiswaActivity;

import java.util.ArrayList;

public class AdapterDaftarMahasiswa extends RecyclerView.Adapter<AdapterDaftarMahasiswa.ViewHolder> {

    private ArrayList<Mahasiswa> daftarMahasiswa;
    private Context context;
    FirebaseDataListener listener;

    public AdapterDaftarMahasiswa(ArrayList<Mahasiswa> mahasiswas, Context ctx){
        daftarMahasiswa = mahasiswas;
        context = ctx;
        listener = (ViewDataMahasiswaActivity)ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvEmail, tvKelas, tvNohp;
        ImageView ivGurumhs;
        LinearLayout linItem;

        ViewHolder(View v) {
            super(v);
            tvNama = (TextView) v.findViewById(R.id.tv_nama);
            tvEmail = (TextView) v.findViewById(R.id.tv_email);
            tvKelas = (TextView) v.findViewById(R.id.tv_kelas);
            tvNohp = (TextView) v.findViewById(R.id.tv_nohp);
            ivGurumhs = (ImageView) v.findViewById(R.id.iv_gurumhs);
            linItem = (LinearLayout) v.findViewById(R.id.lin_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guru_mhs, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String nama = daftarMahasiswa.get(position).getNama();
        final String email = daftarMahasiswa.get(position).getEmail();
        final String nohp = daftarMahasiswa.get(position).getNo_hp();
        final String kelas = daftarMahasiswa.get(position).getJurusan();
        final String jk = daftarMahasiswa.get(position).getJk();
        holder.linItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For one click
            }
        });

        holder.linItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //For long click
                return true;
            }
        });
        if(jk.equals("Male")){
            holder.ivGurumhs.setImageResource(R.drawable.siswam);
        }else{
            holder.ivGurumhs.setImageResource(R.drawable.siswaf);
        }
        holder.tvNama.setText(nama);
        holder.tvEmail.setText(email);
        holder.tvNohp.setText(nohp);
        holder.tvKelas.setText(kelas);
    }

    @Override
    public int getItemCount() {
        return daftarMahasiswa.size();
    }

    public interface FirebaseDataListener{
        void onDeleteData(Mahasiswa mahasiswa, int position);
    }
}