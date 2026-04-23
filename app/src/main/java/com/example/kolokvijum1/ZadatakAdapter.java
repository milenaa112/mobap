package com.example.kolokvijum1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// =============================================
// ZadatakAdapter.java - RECYCLERVIEW ADAPTER
// =============================================
// Svaki RecyclerView mora imati Adapter.
// Adapter je "most" izmedju Liste podataka i vizuelnog prikaza.
//
// Kako funkcionise:
// 1. onCreateViewHolder() - kreira vizuelni red (item_zadatak.xml)
// 2. onBindViewHolder()   - puni red podacima iz liste
// 3. getItemCount()       - kaze RecyclerView-u koliko redova ima
//
// ViewHolder cuva reference na TextView-ove jednog reda
// kako bi se izbegle sporé findViewById() pretrage.
// =============================================

public class ZadatakAdapter extends RecyclerView.Adapter<ZadatakAdapter.ViewHolder> {

    // Lista zadataka koja se prikazuje - ista lista kao u ZadaciFragment-u
    private List<Zadatak> lista;

    public ZadatakAdapter(List<Zadatak> lista) {
        this.lista = lista;
    }

    // ---- VIEWHOLDER ----
    // Cuva reference na poglede jednog reda u listi
    // Kreira se jednom po redu, pa se reciklira (otuda RecyclerView)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNaziv;   // Odgovara R.id.tvNaziv u item_zadatak.xml
        TextView tvVreme;   // Odgovara R.id.tvVreme u item_zadatak.xml

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNaziv = itemView.findViewById(R.id.tvNaziv);
            tvVreme = itemView.findViewById(R.id.tvVreme);
        }
    }

    // ---- KORAK 1: Kreiraj vizuelni red ----
    // Inflate-uje item_zadatak.xml i vraca ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_zadatak, parent, false);
        return new ViewHolder(view);
    }

    // ---- KORAK 2: Popuni red podacima ----
    // position = indeks u listi (0, 1, 2...)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Zadatak zadatak = lista.get(position);
        holder.tvNaziv.setText(zadatak.getNaziv());
        holder.tvVreme.setText("Vreme: " + zadatak.getVreme());
    }

    // ---- KORAK 3: Broj redova ----
    @Override
    public int getItemCount() {
        return lista.size();
    }
}
