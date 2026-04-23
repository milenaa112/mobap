package com.example.kolokvijum1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

// =============================================
// KorisniciFragment.java - FRAGMENT
// =============================================
// Prikazuje se klikom na "Korisnici" u meniju.
// Osnovna uloga: prima broadcast od ProveraServis-a
// i menja pozadinu u ZUTO kada je FAB crven.
//
// KOMUNIKACIJA SA SERVISOM:
//   ProveraServis salje broadcast "PROMENI_POZADINU"
//   Ovaj fragment ga prima i menja boju pozadine.
// =============================================

public class KorisniciFragment extends Fragment {

    // Root view fragmenta - menjamo mu pozadinu
    private View rootView;

    // BroadcastReceiver koji slusa nalog od servisa
    private BroadcastReceiver pozadinaBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Servis je detektovao crveni FAB - promeni pozadinu u zuto
            if (rootView != null) {
                rootView.setBackgroundColor(Color.YELLOW);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Ucitaj fragment_korisnici.xml
        rootView = inflater.inflate(R.layout.fragment_korisnici, container, false);
        return rootView;
    }

    // Registruj receiver kada je fragment aktivan
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("PROMENI_POZADINU");
        requireContext().registerReceiver(pozadinaBroadcast, filter);
    }

    // Odjavi receiver kada fragment nije aktivan (sprecava memory leak)
    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(pozadinaBroadcast);
    }
}
