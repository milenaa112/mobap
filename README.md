KORAK 2: Kreiranje klase Subject (pogledaj zadatak 7 (ako je TimePicker stavi kao string), (sve sto je Checkbox je Boolean))
Sa leve strane ekrana pronađi folder java, pa unutar njega otvori prvi folder (onaj koji u zagradi nema test ili androidTest, npr. com.example.kolokvijum1).Klikni desnim klikom na taj folder.Izaberi New $\rightarrow$ Java Class.U prozorčiću koji se pojavi, upiši naziv: Subject (sa velikim slovom S) i pritisni Enter.Sada ti je otvorena prazna klasa. Prebriši sve unutra i prekopiraj ovaj čist, jednostavan kod:Javapackage com.example.kolokvijum1; // Ovde će stajati tvoj stvarni naziv paketa, ne diraj prvu liniju ako već postoji

public class Subject {
    private String naziv;
    private String datum;
    private boolean isActive;

    // Konstruktor - služi da napravimo novi predmet sa ovim podacima
    public Subject(String naziv, String datum, boolean isActive) {
        this.naziv = naziv;
        this.datum = datum;
        this.isActive = isActive;
    }

    // Geteri - služe da kasnije izvučemo ove podatke za prikaz u listi
    public String getNaziv() { return naziv; }
    public String getDatum() { return datum; }
    public boolean isActive() { return isActive; }
}




KORAK 3:
U tekstu za Kolokvijum 1a (Zadatak 1) piše:"Unutar MainActivity postaviti Toolbar i LinearLayout."   Sada radimo samo XML dizajn tog glavnog ekrana.Šta tačno radiš:Sa leve strane ekrana pronađi folder res $\rightarrow$ otvori folder layout $\rightarrow$ dvoklikni na fajl activity_main.xml.Kada ti se otvori, prebaci se na Code prikaz (gore desno u uglu imaš tri male ikonice: Code, Split, Design. Klikni na Code).Selektuj (obriši) apsolutno sve što se trenutno nalazi u tom fajlu i prekopiraj ovaj čist kod:XML

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary" />

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>



2. Kolokvijum 1b (CoordinatorLayout - PLAVA pozadina)U tekstu za 1b piše: "Unutar MainActivity postaviti Toolbar i CoordinatorLayout sa plavom pozadinom." 
Da nam i sam fragment ne bi poplaveo, unutar FrameLayout-a stavljamo belu pozadinu (#FFFFFF).  XML

<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#0000FF"> <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary" />

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="?attr/actionBarSize"
        android:background="#FFFFFF" /> </androidx.coordinatorlayout.widget.CoordinatorLayout>



3. Kolokvijum 1c (RelativeLayout - ŽUTA pozadina)U tekstu za 1c piše: "Unutar MainActivity postaviti Toolbar i RelativeLayout sa žutom pozadinom." 
Pošto RelativeLayout ređa stvari jednu preko druge, ovde koristimo android:layout_below da bismo FrameLayout gurnuli ispod Toolbara.  XML

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFF00"> <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary" />

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/toolbar"
        android:background="#FFFFFF" /> </RelativeLayout>





KORAK 4: 
Povezivanje Toolbar-a u Java kodu (MainActivity.java)
Sada otvori fajl MainActivity.java. Unutar onCreate metode moramo da povežemo ovaj Toolbar da bi aplikacija znala da ga koristi kao svoju glavnu traku.

Prebriši sve unutra i stavi ovaj kod:


package com.example.kolokvijum1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Pazi da uvezeš androidx verziju!

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Povezuje Java sa onim XML-om

        // 1. Pronalazimo Toolbar preko ID-ja koji smo dali u XML-u
        Toolbar toolbar = findViewById(R.id.toolbar);
        
        // 2. Kažemo aktivnosti: "Ovaj Toolbar je sada glavna akciona traka (ActionBar)"
        setSupportActionBar(toolbar);
    }
}
Ove dve linije za Toolbar su takođe zajedničke za sve grupe i uvek se pišu isto.






KORAK 5: 
Kreiranje gornjeg menija.
Gde to piše u tekstu kolokvijuma?
Pogledaj Zadatak 4 na svom papiru:"4. Dodati meni komponentu sa stavkom Subject. (0.5)"Šta tačno radiš na računaru:Meni se uvek pravi iz dva dela: prvo nacrtamo kako izgleda (XML), a onda ga prikažemo u Java kodu.

1. Deo: Pravljenje XML izgleda za meniPošto Android na početku nema folder za menije, moramo ga napraviti:Sa leve strane ekrana desnim klikom klikni na folder res.Izaberi New $\rightarrow$ Android Resource Directory.U prozoru koji iskoči, tamo gde piše Resource type, klikni i izaberi menu. Klikni OK. (Sada si dobila novi folder pod nazivom menu unutar res foldera).Klikni desnim klikom na taj novi folder menu $\rightarrow$ izaberi New $\rightarrow$ Menu Resource File.Nazovi ga main_menu (sve malim slovima) i klikni Enter.Otvori taj fajl, prebaci se na Code prikaz, obriši sve i prekopiraj ovo:XML

<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    
    <item
        android:id="@+id/action_subject"
        android:title="Subject"
        app:showAsAction="ifRoom" />
        
</menu>
Napomena za sutra: Ako ti u nekoj drugoj grupi (npr. 1b ili 1c) piše stavka "Person" ili "Task" , ti ovde samo promeniš android:title="Person" i promeniš ID u android:id="@+id/action_person". Sve ostalo je potpuno isto!



2. Deo: Prikazivanje menija u Java kodu
Sada moramo da kažemo našoj aktivnosti da "napumpa" (prikaže) ovaj meni na ekranu.

Otvori ponovo MainActivity.java i ispod onCreate metode (ali pre poslednje zatvorene vitičaste zagrade }) dodaj ovu metodu:

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Ova linija uzima naš main_menu.xml i prikazuje ga u Toolbaru
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
}
(Kada ovo ukucaš, reč Menu će pocrveneti. Samo klikni na nju i pritisni Alt + Enter da je Android Studio sam uveze na vrh fajla).








KORAK 6: Kreiranje fragmenta i njegovo otvaranje na klik menija

Gde to piše u tekstu kolokvijuma?
Pogledaj Zadatak 2 i Zadatak 5 na svom papiru:
" 2. Kreirati fragment: SubjectFragment. (1)"
" 5. Klikom na Subject, unutar MainActivity se prikazuje SubjectFragment. (0.5)"



1. Deo: Pravljenje XML izgleda za fragment
Sa leve strane, u folderu res/layout, klikni desnim klikom na New pa Layout Resource File.Nazovi ga fragment_subject (sve malim slovima) i klikni Enter.Prebaci se na Code prikaz, obriši sve i stavi ovaj jednostavan kod:XML

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF"> <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_above="@id/btn_dodati" /> <Button
        android:id="@+id/btn_dodati"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:text="Dodati"
        android:backgroundTint="#00FF00" /> </RelativeLayout>
(Ovim smo već pripremili i teren za Zadatak 3, jer smo odmah stavili RecyclerView i zeleno dugme "Dodati" na dno ekrana ).



2. Deo: Pravljenje Java klase za fragmentDesnim klikom klikni na tvoj glavni Java paket (tamo gde ti stoje MainActivity i Subject) $\rightarrow$ New $\rightarrow$ Java Class.Nazovi je tačno kako piše na papiru: SubjectFragment.  Prebriši sve i stavi ovaj osnovni kod koji samo "podiže" onaj XML koji smo malopre napravili:

package com.example.kolokvijum1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.class;
import androidx.fragment.app.Fragment;

public class SubjectFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ova linija povezuje klasu sa XML izgledom fragment_subject
        return inflater.inflate(R.layout.fragment_subject, container, false);
    }
}



3. Deo: Povezivanje klika u MainActivity da otvori ovaj fragment
Sada se vraćamo u MainActivity.java da završimo Zadatak 5 (klikom na stavku menija otvara se fragment).  Pronađi metodu onOptionsItemSelected koju smo pisali u prošlom koraku i dopuni je da izgleda ovako:

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_subject) {
        
        // KOD ZA OTVARANJE FRAGMENTA (Ovo prepisuješ na svakom kolokvijumu):
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SubjectFragment()) // Ubaci fragment u našu praznu kutiju
                .commit();
                
        return true;
    }
    return super.onOptionsItemSelected(item);
}


dodaj ovo ako nemas u SubjectFragment.java
import androidx.fragment.app.Fragment;







KORAK 7: Otvaranje forme za unos podataka (Dijalog)


Gde to piše u tekstu kolokvijuma?
Pogledaj Zadatak 6 i Zadatak 7 na svom papiru:"6. Klikom na dugme otvara se forma za dodavanje novog predmeta. (0.5)""7. Forma ima: naziv predmeta, datum izvršenja (DatePicker), Checkbox sa labelom 'Active', dugme za potvrdu i dugme za odustajanje (1). Klikom na dugme za odustajanje zatvoriti formu (0.5)." 
Šta tačno radiš na računaru:Prvo moramo da nacrtamo kako ta forma (dijalog) izgleda. Pravimo novi mali XML fajl.

1. Deo: Pravljenje XML izgleda za formuDesni klik na folder res/layout $\rightarrow$ New $\rightarrow$ Layout Resource File.Nazovi ga dialog_add_subject (sve malim slovima) i klikni Enter.Prebaci se na Code prikaz, obriši sve i prekopiraj ovaj kod u kom se nalaze sva polja koja profesor traži:  XML

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/et_naziv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Naziv predmeta" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Datum izvršenja:"
        android:layout_marginTop="10dp"/>

    <DatePicker
        android:id="@+id/datePicker"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:datePickerMode="spinner"
        android:calendarViewShown="false" />

    <CheckBox
        android:id="@+id/cb_active"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Active"
        android:layout_marginTop="10dp" />

</LinearLayout>
(Zašto spinner mod na DatePicker-u? Ako ostaviš običan DatePicker, on će progutati ceo ekran i nećemo videti ništa drugo. Ovako izgleda kao mali elegantni točkić).


2. Deo: Otvaranje ovog dijaloga na klik zelenog dugmetaSada idemo u SubjectFragment.java da nateramo zeleno dugme da otvori ovu formu kada se na njega klikne.  Izmeni tvoj SubjectFragment.java tako da ubacimo onViewCreated metodu u kojoj ćemo pratiti klik na dugme:


package com.example.kolokvijum1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog; // Pazi na uvoz za AlertDialog!
import androidx.fragment.app.Fragment;

public class SubjectFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subject, container, false);
    }

    // Ova metoda se pokreće čim se fragment prikaže na ekranu
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Pronalazimo zeleno dugme "Dodati" iz fragment_subject.xml
        Button btnDodati = view.findViewById(R.id.btn_dodati);

        // 2. Osluškujemo klik na to dugme
        btnDodati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pozivamo našu metodu koja će prikazati dijalog formu
                prikaziDijalogFormu();
            }
        });
    }

    // Metoda za kreiranje i prikazivanje AlertDialog-a (Forme)
    private void prikaziDijalogFormu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Dodaj novi predmet");

        // "Napumpavamo" izgled naše forme u dijalog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        // Pronalazimo komponente sa forme (trebaće nam u sledećem koraku)
        EditText etNaziv = dialogView.findViewById(R.id.et_naziv);
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        CheckBox cbActive = dialogView.findViewById(R.id.cb_active);

        // Dugme za POTVRDU na dijalogu (Zadatak 7)
        builder.setPositiveButton("Potvrdi", (dialog, which) -> {
            // Ovde ćemo u sledećem koraku pokupiti podatke i dodati ih u listu!
        });

        // Dugme za ODUSTAJANJE na dijalogu (Zadatak 7 - donosi 0.5 bodova)
        builder.setNegativeButton("Odustani", (dialog, which) -> {
            dialog.dismiss(); // Klikom na odustajanje zatvori formu (Zadatak 7)
        });

        // Prikaži dijalog na ekranu
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}






KORAK 8: 
Pravljenje izgleda za jedan red u listi (item_subject.xml)
Gde to piše u tekstu?
U Zadatku 3 piše: "SubjectFragment sadrži RecyclerView i služi za prikaz predmeta". Pre nego što u Javi povežemo listu, moramo da napravimo XML kako izgleda samo jedan red (jedna stavka) u toj listi.
Šta radiš na računaru:Desni klik na folder res/layout $\rightarrow$ New $\rightarrow$ Layout Resource File.Nazovi ga item_subject (sve malim slovima) i klikni Enter.Prebaci se na Code prikaz, obriši sve i stavi ovaj jednostavan kod koji prikazuje naziv predmeta i datum (jedan ispod drugog):XML

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="12dp">

    <TextView
        android:id="@+id/tv_item_naziv"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold"
        android:textColor="#000000" />

    <TextView
        android:id="@+id/tv_item_datum"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="14sp"
        android:textColor="#555555"
        android:layout_marginTop="4dp" />

</LinearLayout>






KORAK 9: 
Pravljenje SubjectAdapter klase (Srce liste)
Ovo je onaj šablon koji prepisuješ na svakom kolokvijumu, samo menjaš ime modela (Subject, Person ili Task).
Šta radiš na računaru:Desni klik na tvoj glavni Java paket $\rightarrow$ New $\rightarrow$ Java Class.Nazovi je SubjectAdapter i klikni Enter.Prebriši sve unutra i prekopiraj ovaj šablon:Java

package com.example.kolokvijum1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private ArrayList<Subject> listaPredmeta;

    // Konstruktor koji prima listu sa ekranu
    public SubjectAdapter(ArrayList<Subject> listaPredmeta) {
        this.listaPredmeta = listaPredmeta;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ovde povezujemo naš item_subject.xml layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        // Uzimamo trenutni predmet iz liste i lepimo njegove podatke na TextView-ove
        Subject trenutni = listaPredmeta.get(position);
        holder.tvNaziv.setText(trenutni.getNaziv());
        holder.tvDatum.setText(trenutni.getDatum());
    }

    @Override
    public int getItemCount() {
        return listaPredmeta.size(); // Kaže listi koliko stavki ima ukupno
    }

    // Klasa koja drži komponente iz item_subject.xml dizajna
    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView tvNaziv, tvDatum;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNaziv = itemView.findViewById(R.id.tv_item_naziv);
            tvDatum = itemView.findViewById(R.id.tv_item_datum);
        }
    }
}



KORAK 10: Povezivanje Adaptera i liste unutar SubjectFragment.java, kao i na kupljenje podataka iz forme na klik dugmeta "Potvrdi"

Gde to piše u tekstu?
Pogledaj Zadatak 3 i kraj Zadatka 7:

"3. SubjectFragment sadrži RecyclerView i služi za prikaz predmeta... (1)"
"7. ...Klikom na potvrdu dodati predmet u RecyclerView. (2)"

Šta tačno radiš na računaru:
Otvori ponovo klasu SubjectFragment.java. Sada ćemo unutar nje napraviti praznu listu, povezati RecyclerView sa našim SubjectAdapter-om, a zatim izmeniti dugme "Potvrdi" da izvuče tekst iz polja i osveži ekran.

Prebriši ceo kod unutar SubjectFragment.java i stavi ovu konačnu verziju:


package com.example.kolokvijum1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SubjectFragment extends Fragment {

    // 1. Deklarišemo listu, adapter i RecyclerView da budu dostupni u celoj klasi
    private ArrayList<Subject> listaPredmeta;
    private SubjectAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subject, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Inicijalizujemo praznu listu predmeta
        listaPredmeta = new ArrayList<>();

        // 3. Pronalazimo RecyclerView iz XML-a i povezujemo ga sa adapterom
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Ovo govori listi da ide odozgo na dole
        
        adapter = new SubjectAdapter(listaPredmeta);
        recyclerView.setAdapter(adapter);

        // Pronalazimo zeleno dugme "Dodati"
        Button btnDodati = view.findViewById(R.id.btn_dodati);
        btnDodati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prikaziDijalogFormu();
            }
        });
    }

    private void prikaziDijalogFormu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Dodaj novi predmet");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);
        builder.setView(dialogView);

        EditText etNaziv = dialogView.findViewById(R.id.et_naziv);
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        CheckBox cbActive = dialogView.findViewById(R.id.cb_active);

        // KOD ZA DUGME POTVRDI (Izvlačenje podataka i ubacivanje u listu)
        builder.setPositiveButton("Potvrdi", (dialog, which) -> {
            // A. Uzimamo naziv iz EditText-a
            String unetiNaziv = etNaziv.getText().toString();

            // B. Izvlačimo datum iz DatePicker-a i pakujemo ga u String (format: dan.mesec.godina)
            int dan = datePicker.getDayOfMonth();
            int mesec = datePicker.getMonth() + 1; // Android broji mesece od 0, zato dodajemo 1
            int godina = datePicker.getYear();
            String unetiDatum = dan + "." + mesec + "." + godina + ".";

            // C. Proveravamo da li je Checkbox štikliran
            boolean unetiStatus = cbActive.isChecked();

            // D. Pravimo novi predmet i dodajemo ga u našu listu
            Subject noviPredmet = new Subject(unetiNaziv, unetiDatum, unetiStatus);
            listaPredmeta.add(noviPredmet);

            // E. KLJUČNA LINIJA: Javljamo adapteru da se lista promenila da bi je osvežio na ekranu!
            adapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Odustani", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}





KORAK 11: Kreiranje servisa koji radi na svakih minut vremena

Gde to piše u tekstu?
Pogledaj Zadatak 9 na svom papiru za Kolokvijum 1a:

"9. Kreirati servis koji se pokreće na svaki minut (2.5)..."

Šta tačno radiš na računaru:
Servis se uvek pravi iz dva obavezna dela: kreiranje same Java klase i registracija te klase u AndroidManifest.xml fajlu (ako zaboraviš manifest, aplikacija će se srušiti ili servis uopšte neće raditi!).


1. Deo: Pravljenje Java klase za servisDesni klik na tvoj glavni Java paket $\rightarrow$ New $\rightarrow$ Java Class.Nazovi je MojServis i klikni Enter.Prebriši sve unutra i prekopiraj ovaj univerzalni šablon za servis koji kuca na svakih 60 sekundi:Java

package com.example.kolokvijum1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MojServis extends Service {

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // --- OVDE IDE LOGIKA KOJA SE PONAURANJA SVAKOG MINUTA ---
            
            // Privremena poruka da na ekranu vidiš da servis stvarno radi
            Toast.makeText(MojServis.this, "Servis proverava...", Toast.LENGTH_SHORT).show();
            
            // Ovde ćemo u sledećem koraku dodati proveru dozvole za lokaciju!
            
            // ------------------------------------------------------
            
            // Ponovo pokreni isti ovaj kod za 60000 milisekundi (1 minut)
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Kada se servis pokrene, odmah pokrećemo našu Runnable petlju
        handler.post(runnable);
        return START_STICKY; // Kaže sistemu da ponovo pokrene servis ako ga ubije
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Zaustavljamo petlju kada se servis gasi da ne troši bateriju
        handler.removeCallbacks(runnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Ovo nam ne treba za kolokvijum, samo ostavi null
    }
}




2. Deo: Upisivanje servisa u Manifest (⚠️ NAJVAŽNIJE!)
Sa leve strane u folderu manifests otvori fajl AndroidManifest.xml.

Skroluj dole dok ne nađeš oznaku </activity>.

Tačno ispod </activity>, a pre zatvaranja cele aplikacije </application>, upiši ovu jednu liniju:

XML
<service android:name=".MojServis" />


3. Deo: Pokretanje servisa iz MainActivity
Da bi se ovaj servis uopšte pokrenuo kada se aplikacija upali, moramo dodati jednu liniju koda u MainActivity.java na kraj onCreate metode:

Java
// Pokretanje našeg servisa čim se aplikacija upali
Intent intent = new Intent(this, MojServis.class);
startService(intent);



VERZIJA ZA KOLOKVIJUM 1a: Provera Lokacije
Šta se traži: Provera da li aplikacija ima dozvolu za lokaciju.
Šta kucaš unutar run() metode:  

@Override
public void run() {
    // 1. Provera sistemske dozvole za lokaciju
    int provera = androidx.core.content.ContextCompat.checkSelfPermission(
            MojServis.this, 
            android.Manifest.permission.ACCESS_FINE_LOCATION
    );

    // 2. Ako je dozvola odobrena (GRANTED)
    if (provera == android.content.pm.PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(MojServis.this, "Lokacija dozvoljena!", Toast.LENGTH_SHORT).show();
        // Ovde profesor traži i žutu pozadinu fragmenta/notifikaciju, ali i sam Toast ti donosi bodove za proveru!
    } else {
        Toast.makeText(MojServis.this, "Nemamo dozvolu za lokaciju!", Toast.LENGTH_SHORT).show();
    }

    // Ponovi petlju za 1 minut
    handler.postDelayed(this, 60000);
}



VERZIJA ZA KOLOKVIJUM 1b: Provera Lokacije i Interneta
Šta se traži: Provera dozvole za lokaciju i da li je uređaj konektovan na internet.
Šta kucaš unutar run() metode:  Java

@Override
public void run() {
    // 1. Provera dozvole za lokaciju
    int proveraLokacije = androidx.core.content.ContextCompat.checkSelfPermission(
            MojServis.this, 
            android.Manifest.permission.ACCESS_FINE_LOCATION
    );
    boolean imaLokaciju = (proveraLokacije == android.content.pm.PackageManager.PERMISSION_GRANTED);

    // 2. Provera da li ima interneta
    android.net.ConnectivityManager cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
    android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean imaInternet = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

    // 3. Profesorovi uslovi sa papira (Kombinacije)
    if (imaLokaciju && !imaInternet) {
        Toast.makeText(MojServis.this, "Lokacija dozvoljena!", Toast.LENGTH_SHORT).show();
    } else if (!imaLokaciju && imaInternet) {
        Toast.makeText(MojServis.this, "Konektovan", Toast.LENGTH_SHORT).show();
    } else if (imaLokaciju && imaInternet) {
        Toast.makeText(MojServis.this, "Sve OK!", Toast.LENGTH_SHORT).show();
    }

    // Ponovi petlju za 1 minut
    handler.postDelayed(this, 60000);
}


VERZIJA ZA KOLOKVIJUM 1c: Provera Kamere
Šta se traži: Provera da li je dozvoljena kamera.
Šta kucaš unutar run() metode:  Java

@Override
public void run() {
    // 1. Provera sistemske dozvole za kameru
    int proveraKamere = androidx.core.content.ContextCompat.checkSelfPermission(
            MojServis.this, 
            android.Manifest.permission.CAMERA
    );

    // 2. Ako je kamera odobrena
    if (proveraKamere == android.content.pm.PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(MojServis.this, "Kamera je dozvoljena!", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(MojServis.this, "Kamera NIJE dozvoljena!", Toast.LENGTH_SHORT).show();
    }

    // Ponovi petlju za 1 minut
    handler.postDelayed(this, 60000);
}
