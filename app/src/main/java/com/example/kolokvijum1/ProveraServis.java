package com.example.kolokvijum1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

// =============================================
// ProveraServis.java - SERVICE
// =============================================
// Service je Android komponenta koja radi u POZADINI.
// Ne mora da bude vidljiv korisnik.
// Pokrece se iz ZadaciFragment-a pozivom startService().
//
// STA RADI OVAJ SERVIS:
//   Svakih 60 sekundi (1 minut) proverava da li je FAB crven.
//   Boja FAB-a se salje kroz broadcast "FAB_BOJA_PROMENJENA"
//   iz ZadaciFragment-a svaki put kada se promeni.
//
// AKO JE FAB CRVEN:
//   1. Salje notifikaciju: "Crveno je!"
//   2. Salje broadcast "PROMENI_POZADINU" -> KorisniciFragment
//      menja pozadinu u ZUTO
//
// ZIVOTNI CIKLUS SERVISA:
//   onCreate()       -> jednom, kada se servis kreira
//   onStartCommand() -> svaki put kada ga neko startuje
//   onDestroy()      -> kada se servis gasi
//
// START_STICKY znaci: ako sistem ubije servis, restart ga automatski
// =============================================

public class ProveraServis extends Service {

    private static final String CHANNEL_ID = "kanal_provera";
    private static final int NOTIFICATION_ID = 1;
    private static final long INTERVAL_MS = 60_000; // 1 minut u milisekundama

    // Handler + Runnable za periodicno izvrsavanje
    // Handler radi na glavnoj niti (UI thread)
    private Handler handler = new Handler();
    private Runnable periodicniZadatak;

    // Cuva trenutnu boju FAB-a (prima od ZadaciFragment-a kroz broadcast)
    private boolean fabJeCreven = false;

    // Receiver koji slusa promenu boje FAB-a
    private BroadcastReceiver bojaBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fabJeCreven = intent.getBooleanExtra("fabJeCreven", false);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // Kreiraj notifikacioni kanal (obavezno za Android 8+)
        kreirajNotifikacioniKanal();

        // Registruj receiver za pracenje boje FAB-a
        IntentFilter filter = new IntentFilter("FAB_BOJA_PROMENJENA");
        registerReceiver(bojaBroadcast, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Definisi sta ce se raditi svakih 60 sekundi
        periodicniZadatak = new Runnable() {
            @Override
            public void run() {

                // ---- PROVERI BOJU FAB-A ----
                if (fabJeCreven) {

                    // 1. Posalji notifikaciju
                    posaljiNotifikaciju();

                    // 2. Posalji broadcast KorisniciFragment-u
                    //    da promeni pozadinu u zuto
                    Intent pozadinaIntent = new Intent("PROMENI_POZADINU");
                    sendBroadcast(pozadinaIntent);
                }

                // Zakaži sledeće izvršavanje za 60 sekundi
                handler.postDelayed(this, INTERVAL_MS);
            }
        };

        // Pokreni prvu proveru odmah
        handler.post(periodicniZadatak);

        // START_STICKY = ako sistem ubije servis, automatski ga restartuj
        return START_STICKY;
    }

    // Kreira i prikazuje notifikaciju sa tekstom "Crveno je!"
    private void posaljiNotifikaciju() {
        NotificationManager nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert) // ikona notifikacije
                .setContentTitle("Upozorenje!")
                .setContentText("Crveno je!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // notifikacija nestaje kada korisnik klikne na nju

        nm.notify(NOTIFICATION_ID, builder.build());
    }

    // Notifikacioni kanal je obavezan za Android 8 (API 26) i vise
    // Bez kanala, notifikacija se ne prikazuje na novijim Androidima
    private void kreirajNotifikacioniKanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Provera boje FAB-a",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifikacije kada je FAB crven");

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    // Servis ne podrzava binding (ne komuniciramo direktno sa njim)
    @Override
    public IBinder onBind(Intent intent) {
        return null; // null = servis nije "bound", samo "started"
    }

    // Kada se servis gasi - ocisti resurse
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Zaustavi periodicno izvrsavanje
        handler.removeCallbacks(periodicniZadatak);
        // Odjavi broadcast receiver
        unregisterReceiver(bojaBroadcast);
    }
}
