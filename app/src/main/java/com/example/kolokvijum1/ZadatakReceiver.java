package com.example.kolokvijum1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

// =============================================
// ZadatakReceiver.java - BROADCAST RECEIVER
// =============================================
// BroadcastReceiver je komponenta koja "slusá" dogadjaje.
// Kada ZadaciFragment doda zadatak, salje broadcast "ZADATAK_DODAT".
// Ovaj receiver ga prima i menja boju FAB-a.
//
// LOGIKA BOJE:
//   - Neparan broj zadataka (1, 3, 5...) --> FAB postaje CRVEN
//   - Paran broj zadataka  (0, 2, 4...) --> FAB postaje PLAV
//
// REGISTRACIJA:
//   Receiver se registruje u ZadaciFragment.onResume()
//   i odjavljuje u ZadaciFragment.onPause()
//   jer je FAB dostupan samo dok je fragment aktivan.
//
// NAPOMENA: U AndroidManifest.xml je prijavljen staticno,
//   ali ovaj receiver prima referencu na FAB dinamicki
//   jer manifest ne moze preneti UI reference.
// =============================================

public class ZadatakReceiver extends BroadcastReceiver {

    // Referenca na FAB iz ZadaciFragment-a
    // Postavlja se kroz konstruktor
    private FloatingActionButton fab;

    // Konstruktor prima FAB kako bi mogao da mu menja boju
    public ZadatakReceiver(FloatingActionButton fab) {
        this.fab = fab;
    }

    // onReceive() se poziva kada stigne broadcast "ZADATAK_DODAT"
    // intent.getIntExtra("brojZadataka") sadrzi trenutni broj zadataka
    @Override
    public void onReceive(Context context, Intent intent) {

        // Uzmi broj zadataka koji je poslat u broadcastu
        int brojZadataka = intent.getIntExtra("brojZadataka", 0);

        if (fab != null) {
            if (brojZadataka % 2 != 0) {
                // NEPARAN broj --> boji FAB u CRVENO
                fab.setBackgroundTintList(
                    ColorStateList.valueOf(Color.RED)
                );
            } else {
                // PARAN broj --> boji FAB u PLAVO
                fab.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#2196F3"))
                );
            }
        }
    }
}
