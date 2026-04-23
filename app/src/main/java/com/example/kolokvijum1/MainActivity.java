package com.example.kolokvijum1;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// =============================================
// MainActivity.java - GLAVNA AKTIVNOST
// =============================================
// Aktivnost je "ekran" u Android aplikaciji.
// MainActivity je pocetni ekran - pokrece se prva.
//
// STA RADI:
//   1. Postavlja Toolbar (gornja traka sa menijem)
//   2. Prikazuje ZadaciFragment kao pocetni fragment
//   3. Kreira meni (main_menu.xml)
//   4. Reaguje na klikove menija -> menja fragment
//
// FRAGMENT MANAGER:
//   getSupportFragmentManager() upravlja fragmentima.
//   beginTransaction() pocinje promenu fragmenta.
//   replace(container, fragment) zamenjuje sadrzaj kontejnera.
//   commit() primenjuje promenu.
//
// ZIVOTNI CIKLUS:
//   onCreate() -> setContentView() -> postavi toolbar -> ucitaj fragment
// =============================================

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Postavi activity_main.xml kao izgled ovog ekrana
        setContentView(R.layout.activity_main);

        // ---- Postavi Toolbar ----
        // Toolbar zamenjuje podrazumevani ActionBar
        // Tema mora biti NoActionBar (postavljeno u themes.xml)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ---- Pocetni fragment ----
        // Prikazuje se samo jednom, kada se aktivnost prvi put kreira
        // savedInstanceState je null samo pri prvom pokretanju
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ZadaciFragment())
                    .commit();
        }
    }

    // ---- Kreiraj meni ----
    // Poziva se automatski kada Android treba da prikaze meni
    // Ucitava main_menu.xml i prikazuje stavke u Toolbar-u
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // ---- Reaguj na klik stavke menija ----
    // Poziva se kada korisnik klikne na stavku u meniju
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_zadaci) {
            // Klik na "Zadaci" -> prikazati ZadaciFragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ZadaciFragment())
                    .commit();
            return true;

        } else if (id == R.id.menu_korisnici) {
            // Klik na "Korisnici" -> prikazati KorisniciFragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new KorisniciFragment())
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
