package com.example.aplikasiabsensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasiabsensi.R;
import com.example.aplikasiabsensi.model.Absen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterAbsen extends RecyclerView.Adapter<AdapterAbsen.ViewHolder> {

    private ArrayList<Absen> dataAbsen;
    private Context context;

    public AdapterAbsen(ArrayList<Absen> dataabsens, Context ctx){
        dataAbsen = dataabsens;
        context = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvEmailKelas, tvTanggal, tvKet;
        ImageView ivAbsensi;
        LinearLayout linItem;

        ViewHolder(View v) {
            super(v);
            tvNama = (TextView) v.findViewById(R.id.tv_nama);
            tvEmailKelas = (TextView) v.findViewById(R.id.tv_emailkelas);
            tvTanggal = (TextView) v.findViewById(R.id.tv_tanggal);
            tvKet = (TextView) v.findViewById(R.id.tv_keterangan);
            ivAbsensi = (ImageView) v.findViewById(R.id.iv_absensi);
            linItem = (LinearLayout) v.findViewById(R.id.lin_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absen, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String nama = dataAbsen.get(position).getNama();
        final String email = dataAbsen.get(position).getEmail();
        final String kelas = dataAbsen.get(position).getJurusan_kelas();
        final String tanggal = dataAbsen.get(position).getTanggal();
        final String keterangan = dataAbsen.get(position).getKeterangan();
        final String absensi = dataAbsen.get(position).getAbsensi();
        holder.linItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.linItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }
        });
        if(absensi.equals("Absen")){
            holder.ivAbsensi.setImageResource(R.drawable.cancel);
        }else{
            holder.ivAbsensi.setImageResource(R.drawable.checked);
        }
        holder.tvNama.setText(nama);
        holder.tvEmailKelas.setText(email+" - "+kelas);
        holder.tvTanggal.setText(tanggal);
        holder.tvKet.setText(keterangan);
    }

    @Override
    public int getItemCount() {
        return dataAbsen.size();
    }

}