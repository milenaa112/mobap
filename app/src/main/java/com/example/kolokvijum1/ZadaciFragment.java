package com.example.kolokvijum1;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// =============================================
// ZadaciFragment.java - FRAGMENT (GLAVNI)
// =============================================
// Prikazuje se klikom na "Zadaci" u meniju (i na pocetku).
// Sadrzi:
//   - RecyclerView sa listom zadataka
//   - FloatingActionButton (FAB) za dodavanje zadatka
//   - Logiku za otvaranje forme (AlertDialog)
//   - Registraciju BroadcastReceiver-a za promenu boje FAB-a
//   - Pokretanje ProveraServis-a
//
// BROADCAST FLOW:
//   1. Korisnik klikne FAB -> otvori se forma (AlertDialog)
//   2. Korisnik popuni formu i klikne Potvrdi
//   3. Zadatak se dodaje u listu, adapter se osvezava
//   4. Salje se broadcast "ZADATAK_DODAT" sa brojem zadataka
//   5. ZadatakReceiver prima broadcast i menja boju FAB-a
// =============================================

public class ZadaciFragment extends Fragment {

    // UI komponente
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    // Lista zadataka i adapter
    private List<Zadatak> listaZadataka = new ArrayList<>();
    private ZadatakAdapter adapter;

    // BroadcastReceiver - registruje se ovde jer ima pristup FAB-u
    private ZadatakReceiver zadatakReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Ucitaj fragment_zadaci.xml
        View view = inflater.inflate(R.layout.fragment_zadaci, container, false);

        // Povezi UI elemente sa kodom
        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);

        // ---- Postavi RecyclerView ----
        // LinearLayoutManager = vertikalna lista (podrazumevano)
        adapter = new ZadatakAdapter(listaZadataka);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // ---- FAB klik -> otvori formu ----
        fab.setOnClickListener(v -> otvoriFormuZaDodavanje());

        // ---- Pokreni servis koji proverava boju svakog minuta ----
        Intent servisIntent = new Intent(getContext(), ProveraServis.class);
        requireContext().startService(servisIntent);

        return view;
    }

    // ---- Registruj BroadcastReceiver kada fragment postane vidljiv ----
    @Override
    public void onResume() {
        super.onResume();
        zadatakReceiver = new ZadatakReceiver(fab);
        IntentFilter filter = new IntentFilter("ZADATAK_DODAT");
        requireContext().registerReceiver(zadatakReceiver, filter);
    }

    // ---- Odjavi BroadcastReceiver kada fragment nije vidljiv ----
    // VAZNO: Bez ovoga dolazi do "memory leak" i crash-a!
    @Override
    public void onPause() {
        super.onPause();
        if (zadatakReceiver != null) {
            requireContext().unregisterReceiver(zadatakReceiver);
        }
    }

    // ---- Otvori formu za dodavanje zadatka ----
    // Koristi AlertDialog sa custom layoutom (dialog_dodaj_zadatak.xml)
    private void otvoriFormuZaDodavanje() {

        // Kreiraj AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Ucitaj dialog_dodaj_zadatak.xml kao view za dialog
        View formView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_dodaj_zadatak, null);
        builder.setView(formView);

        // Kreira se dialog (jos nije prikazan)
        AlertDialog dialog = builder.create();

        // Povezi elemente forme
        EditText etNaziv = formView.findViewById(R.id.etNaziv);
        TimePicker timePicker = formView.findViewById(R.id.timePicker);
        Button btnPotvrdi = formView.findViewById(R.id.btnPotvrdi);
        Button btnOdustani = formView.findViewById(R.id.btnOdustani);

        // TimePicker - postavi 24h format
        timePicker.setIs24HourView(true);

        // ---- ODUSTANI: samo zatvori dialog ----
        btnOdustani.setOnClickListener(v -> dialog.dismiss());

        // ---- POTVRDI: dodaj zadatak i posalji broadcast ----
        btnPotvrdi.setOnClickListener(v -> {

            // Procitaj unesene vrednosti
            String naziv = etNaziv.getText().toString().trim();

            // Validacija - ne dozvoli prazan naziv
            if (naziv.isEmpty()) {
                etNaziv.setError("Unesite naziv zadatka!");
                return;
            }

            // Procitaj vreme iz TimePicker-a
            int sat = timePicker.getHour();
            int minut = timePicker.getMinute();
            // Formatiranje: "9:5" -> "09:05"
            String vreme = String.format("%02d:%02d", sat, minut);

            // Dodaj novi zadatak u listu
            listaZadataka.add(new Zadatak(naziv, vreme));

            // Obavestiti adapter da se lista promenila -> osvezi prikaz
            adapter.notifyDataSetChanged();

            // ---- POSALJI BROADCAST ----
            // ZadatakReceiver ce primiti ovo i promeniti boju FAB-a
            Intent broadcastIntent = new Intent("ZADATAK_DODAT");
            broadcastIntent.putExtra("brojZadataka", listaZadataka.size());
            requireContext().sendBroadcast(broadcastIntent);

            // Takodje posalji broadcast za servis (cuva trenutnu boju kao string)
            boolean fabJeCreven = fab.getBackgroundTintList() != null
                    && fab.getBackgroundTintList().getDefaultColor() == Color.RED;
            Intent servisBroadcast = new Intent("FAB_BOJA_PROMENJENA");
            servisBroadcast.putExtra("fabJeCreven", fabJeCreven);
            requireContext().sendBroadcast(servisBroadcast);

            // Zatvori dialog
            dialog.dismiss();
        });

        // Prikazi dialog
        dialog.show();
    }

    // Getter za FAB - koristi ga ProveraServis indirektno kroz broadcast
    public FloatingActionButton getFab() {
        return fab;
    }

    // Provera boje FAB-a - koristi je servis kroz staticki pristup
    public static boolean isFabCreven(FloatingActionButton fab) {
        if (fab == null || fab.getBackgroundTintList() == null) return false;
        return fab.getBackgroundTintList().getDefaultColor() == Color.RED;
    }
}
