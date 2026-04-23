# Kolokvijum1 - Android Aplikacija

Kompletno resenje Android kolokvijuma u Javi.

---

## Struktura projekta

```
app/src/main/
├── AndroidManifest.xml               ← Registracija svih komponenti
├── java/com/example/kolokvijum1/
│   ├── MainActivity.java             ← Zadatak 1, 4, 5
│   ├── ZadaciFragment.java           ← Zadatak 2, 3, 6, 7, 8, 9
│   ├── KorisniciFragment.java        ← Zadatak 2, 10
│   ├── Zadatak.java                  ← Model klasa
│   ├── ZadatakAdapter.java           ← RecyclerView adapter (Zadatak 6)
│   ├── ZadatakReceiver.java          ← BroadcastReceiver (Zadatak 9)
│   └── ProveraServis.java            ← Service (Zadatak 10)
└── res/
    ├── layout/
    │   ├── activity_main.xml         ← Toolbar + RelativeLayout
    │   ├── fragment_zadaci.xml       ← RecyclerView + FAB
    │   ├── fragment_korisnici.xml    ← Bela pozadina (zuti se)
    │   ├── dialog_dodaj_zadatak.xml  ← Forma: naziv + TimePicker + dugmad
    │   └── item_zadatak.xml          ← Jedan red u listi
    ├── menu/
    │   └── main_menu.xml             ← Stavke: Zadaci, Korisnici
    └── values/
        ├── strings.xml
        └── themes.xml
```

---

## Kako ubaciti u Android Studio

### Opcija A — Novi projekat (preporuceno)

1. Otvori Android Studio
2. **File → New → New Project**
3. Izaberi **Empty Views Activity**
4. Postavi:
   - Name: `Kolokvijum1`
   - Package: `com.example.kolokvijum1`
   - Language: `Java`
   - Min SDK: `API 26`
5. Klikni **Finish** i sacekaj Gradle sync
6. Sad kopiraj svaki fajl iz ovog projekta na odgovarajuce mesto

### Kopiranje fajlova

| Fajl iz ovog projekta | Gde ide u Android Studiu |
|---|---|
| `MainActivity.java` | `app/src/main/java/com/example/kolokvijum1/` |
| `ZadaciFragment.java` | isto |
| `KorisniciFragment.java` | isto |
| `Zadatak.java` | isto |
| `ZadatakAdapter.java` | isto |
| `ZadatakReceiver.java` | isto |
| `ProveraServis.java` | isto |
| `activity_main.xml` | `app/src/main/res/layout/` |
| `fragment_zadaci.xml` | isto |
| `fragment_korisnici.xml` | isto |
| `dialog_dodaj_zadatak.xml` | isto |
| `item_zadatak.xml` | isto |
| `main_menu.xml` | `app/src/main/res/menu/` ← napravi folder ako ne postoji |
| `AndroidManifest.xml` | `app/src/main/` (zameni postojeci) |
| `themes.xml` | `app/src/main/res/values/` (zameni postojeci) |
| `strings.xml` | `app/src/main/res/values/` (zameni postojeci) |

### Kreiranje menu foldera (ako ne postoji)

Desni klik na `res` → **New → Android Resource Directory** → Resource type: `menu` → OK

---

## Pokretanje

1. **Tools → Device Manager → Create Device**
   - Izaberi: Pixel 6, API 33
2. Pritisni **Play** pored kreiranog uredjaja
3. Sacekaj da se emulator upali (~2 min prvi put)
4. Pritisni zeleni **▶ Run** u toolbaru Android Studia

---

## Tok aplikacije

```
Pokretanje
    └── MainActivity
            ├── Toolbar (meni: Zadaci | Korisnici)
            └── ZadaciFragment (pocetni)
                    ├── RecyclerView (lista zadataka)
                    └── FAB (plavo dugme +)
                            └── Klik → AlertDialog (forma)
                                    ├── EditText (naziv)
                                    ├── TimePicker (vreme)
                                    ├── Odustani → zatvori formu
                                    └── Potvrdi → dodaj u listu
                                                → sendBroadcast("ZADATAK_DODAT")
                                                        └── ZadatakReceiver
                                                                ├── neparan → FAB = CRVENA
                                                                └── paran   → FAB = PLAVA

ProveraServis (pokrenut iz ZadaciFragment)
    └── svakih 60s proverava: da li je FAB crven?
            └── DA → posalji notifikaciju "Crveno je!"
                   → sendBroadcast("PROMENI_POZADINU")
                           └── KorisniciFragment → pozadina = ZUTA
```

---

## Ceste greske i resenja

| Greska | Resenje |
|---|---|
| `Cannot resolve symbol 'R'` | Build → Clean Project, pa Rebuild |
| `menu` folder ne postoji | res → desni klik → New → Android Resource Directory → menu |
| Notifikacija se ne prikazuje | AndroidManifest mora imati POST_NOTIFICATIONS dozvolu |
| App crasha odmah | Pogledaj Logcat (dole) - crvene linije pokazuju razlog |
| Gradle sync fail | Proveri internet konekciju, File → Sync Project with Gradle Files |
