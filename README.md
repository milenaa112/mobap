Okej, objasniću ti sve od nule, korak po korak, kao da nikad nisi pravio Android projekat.

---

## 1. PRAVLJENJE PROJEKTA U ANDROID STUDIJU

Otvoriš Android Studio → **New Project** → **Empty Views Activity**

Popuniš:
- **Name:** Kolokvijum2
- **Package name:** com.example.kolokvijum2
- **Language:** Kotlin
- **Minimum SDK:** API 24

Klikneš Finish i čekaš da se projekat učita.

---

## 2. STRUKTURA KOJA SE AUTOMATSKI GENERIŠE

Kada se projekat napravi, imaš ovo:

```
app/
├── src/main/
│   ├── java/com/example/kolokvijum2/
│   │   └── MainActivity.kt        ← jedina klasa, ovde sve radiš
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml  ← izgled ekrana
│   │   └── values/
│   │       └── strings.xml        ← tekstovi
│   └── AndroidManifest.xml        ← dozvole i konfiguracija
├── build.gradle (app)             ← zavisnosti (biblioteke)
└── build.gradle (project)        ← generalne postavke
```

---

## 3. REDOSLED RADA — ŠTA PRVO RADIŠ

### Korak 1 — build.gradle (app)
**Ovo radi PRVO pre svega.** Dodaješ biblioteke koje će ti trebati. Bez ovoga Room i Retrofit ne postoje u projektu.

Otvoriš `app/build.gradle` i dodaš:

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'          // ← OBAVEZNO za Room
}

dependencies {
    // Room – baza podataka
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // Retrofit – HTTP zahtevi
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // Lifecycle – za lifecycleScope
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
}
```

Nakon što dodaš, klikneš **Sync Now** (pojavi se žuta traka gore). Čekaš da se završi.

---

### Korak 2 — AndroidManifest.xml
Dodaješ **dozvole** (permisije) — bez njih kamera, lokacija, kontakti ne rade:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

I dodaješ **FileProvider** (obavezno za kameru na Android 7+) unutar `<application>` taga:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths"/>
</provider>
```

---

### Korak 3 — res/xml/file_paths.xml
Ovaj fajl **ne postoji** automatski, moraš ga napraviti.

Desni klik na `res` → New → Android Resource File → naziv `file_paths`, tip `xml`.

Sadržaj:
```xml
<paths>
    <external-files-path name="my_images" path="Pictures/"/>
</paths>
```

Ovo govori FileProvideru gde sme da traži slike.

---

### Korak 4 — activity_main.xml
Otvoriš `res/layout/activity_main.xml`, prebacis na **Code** pogled i postaviš UI elemente. Za ovaj zadatak sve ide vertikalno jedno ispod drugog, pa koristiš `LinearLayout`:

```xml
<ScrollView ...>
  <LinearLayout android:orientation="vertical" ...>

    <TextView android:id="@+id/tvLocation" .../>
    <ImageButton android:id="@+id/ibCamera" .../>
    <ImageView android:id="@+id/ivPhoto" .../>
    <Switch android:id="@+id/switchPosts" .../>
    <Button android:id="@+id/btnDelete" .../>

  </LinearLayout>
</ScrollView>
```

`ScrollView` oko svega je dobra praksa da ne bude odsečeno na malim ekranima.

---

### Korak 5 — Pravljenje paketa i klasa

U Android Studiju, u levom panelu desni klik na `com.example.kolokvijum2` → **New → Package** → nazoveš ga `database`. Isto za `network`.

Onda unutar tih paketa praviš klase: desni klik na paket → **New → Kotlin Class/File**.

---

## 4. KOJE KLASE POSTOJE I ŠTA SVAKA RADI

### `database/PostEntity.kt`
Ovo su **dve stvari u jednom fajlu** — model podataka i DAO interfejs.

**PostEntity** — predstavlja jedan red u tabeli baze:
```kotlin
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)
```
Anotacija `@Entity` kaže Roomu: "napravi tabelu za ovu klasu". `@PrimaryKey` je obavezan primarni ključ.

**PostDao** — interfejs kroz koji komuniciraš sa bazom:
```kotlin
@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(posts: List<PostEntity>)

    @Query("SELECT COUNT(*) FROM posts")
    fun getCount(): Int

    @Query("SELECT * FROM posts ORDER BY rowid ASC LIMIT 1")
    fun getFirstPost(): PostEntity?

    @Delete
    fun delete(post: PostEntity)
}
```
`@Dao` anotacija govori Roomu da je ovo klasa za pristup bazi. Nikad ne praviš objekat od ovoga direktno — Room sam generiše implementaciju.

---

### `database/AppDatabase.kt`
Ovo je **ulaz u bazu** — singleton koji drži konekciju:

```kotlin
@Database(entities = [PostEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "db")
                    .build().also { INSTANCE = it }
            }
    }
}
```

Koristiš ga u MainActivity ovako:
```kotlin
val db = AppDatabase.getDatabase(this)
db.postDao().getCount()   // pa pozvaš bilo koju metodu iz DAO-a
```

`@Volatile` + `synchronized` su tu da baza ne bi bila otvorena dva puta ako se više threadova istovremeno pokrene — standardni singleton pattern.

---

### `network/ApiService.kt`
Ovde definišeš **šta šalješ serveru i šta očekuješ nazad**.

Najpre wrapper klasa za JSON odgovor (jer API vraća `{ "posts": [...] }`):
```kotlin
data class PostsResponse(
    val posts: List<PostEntity>
)
```

Interfejs sa GET zahtevom:
```kotlin
interface ApiService {
    @GET("posts")
    suspend fun getPosts(): PostsResponse
}
```
`suspend` znači da se ova funkcija može pozvati samo iz korutine (Dispatchers.IO).

Retrofit singleton:
```kotlin
object RetrofitClient {
    val api: ApiService = Retrofit.Builder()
        .baseUrl("https://app.beeceptor.com/mock-server/dummy-json/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
```
`GsonConverterFactory` automatski pretvara JSON odgovor u Kotlin objekte.

---

### `MainActivity.kt`
Ovo je **jedina aktivnost** — ona kontroliše sve. Implementira `SensorEventListener` da bi mogla da prima podatke sa senzora.

Bitne stvari na vrhu klase:
```kotlin
class MainActivity : AppCompatActivity(), SensorEventListener {

    // UI elementi
    private lateinit var tvLocation: TextView
    private lateinit var ibCamera: ImageButton
    // itd...

    // Senzori
    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null
    private var accelerometer: Sensor? = null

    // Vrednosti senzora
    private var gyroX = 0f; private var gyroY = 0f; private var gyroZ = 0f
    private var accelX = 0f; private var accelY = 0f; private var accelZ = 0f

    // Za kameru
    private var photoUri: Uri? = null

    // Baza
    private lateinit var db: AppDatabase
```

`lateinit` znači: "ova promenljiva će biti inicijalizovana pre upotrebe, ali ne odmah" — koristiš je za objekte koje inicijalizuješ u `onCreate`.

---

## 5. ŽIVOTNI CIKLUS AKTIVNOSTI — ŠTA IDE GDE

```
onCreate()   ← sve inicijalizuješ ovde (findViewById, sensorManager, db, klikovi...)
onResume()   ← registruješ senzore (poziva se kad se ekran pojavi)
onPause()    ← deregistruješ senzore (poziva se kad napustiš ekran)
```

Zašto senzori u onResume/onPause? Ako ih registruješ u onCreate a nikad ne deregistruješ, troše bateriju čak i kada korisnik napusti aplikaciju.

---

## 6. KAKO FUNKCIONIŠU LAUNCHERS (Kamera, Permisije)

Moderniji Android ne koristi `startActivityForResult` nego **launchers**:

```kotlin
// Definišeš ga kao promenljivu IZVAN onCreate
private val cameraLauncher =
    registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            ivPhoto.setImageURI(photoUri)   // prikaži sliku
            // prikaži gyro toast
        }
    }

// U onClick ga pokrećeš
cameraLauncher.launch(photoUri)
```

```kotlin
private val permissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        if (results[Manifest.permission.ACCESS_FINE_LOCATION] == true) fetchLocation()
    }

// Pokretanje
permissionLauncher.launch(arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_CONTACTS
))
```

---

## 7. KORUTINE — ZAŠTO I KAKO

Room i Retrofit **ne smeju da se pozivaju na glavnom (UI) threadu**. Koristiš `lifecycleScope`:

```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    // Ovde si na IO threadu — baza i mreža
    val posts = RetrofitClient.api.getPosts()
    db.postDao().insertAll(posts.posts.take(10))

    withContext(Dispatchers.Main) {
        // Ovde si nazad na UI threadu — možeš da menjaš UI
        Toast.makeText(this@MainActivity, "Gotovo!", Toast.LENGTH_SHORT).show()
    }
}
```

---

## 8. NOTIFIKACIONI KANAL

Na Android 8+ moraš napraviti kanal pre slanja notifikacije. Radiš to jednom u `onCreate`:

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        "moj_kanal",          // ID kanala
        "Naziv kanala",       // vidljivo korisniku u Settings
        NotificationManager.IMPORTANCE_DEFAULT
    )
    getSystemService(NotificationManager::class.java)
        .createNotificationChannel(channel)
}
```

Slanje:
```kotlin
val notif = NotificationCompat.Builder(this, "moj_kanal")
    .setSmallIcon(android.R.drawable.ic_dialog_info)
    .setContentTitle("Naslov")
    .setContentText("Nema više postova!")
    .build()
NotificationManagerCompat.from(this).notify(1, notif)
```

---

## 9. REDOSLED PISANJA KODA (preporuka za kolokvijum)

1. ✅ `build.gradle` → Sync
2. ✅ `AndroidManifest.xml` → permisije + FileProvider
3. ✅ `res/xml/file_paths.xml` → napravi fajl
4. ✅ `activity_main.xml` → postavi sve UI elemente sa ID-jevima
5. ✅ `PostEntity.kt` → `@Entity` + `@Dao`
6. ✅ `AppDatabase.kt` → singleton
7. ✅ `ApiService.kt` → Retrofit
8. ✅ `MainActivity.kt` → sve ostalo

---

## 10. BRZA REFERENCA — ŠTA SE ZA ŠTA KORISTI

| Zadatak | Klasa/API | Ključna metoda |
|---|---|---|
| Lokacija | `LocationManager` | `getLastKnownLocation()` |
| Kamera | `TakePicture` launcher | `cameraLauncher.launch(uri)` |
| Žiroskop/Akcelerometar | `SensorManager` | `onSensorChanged()` |
| Baza | Room `@Dao` | `insertAll()`, `getFirstPost()`, `delete()` |
| HTTP | Retrofit `@GET` | `suspend fun getPosts()` |
| SharedPreferences | `getSharedPreferences()` | `.edit().putString().apply()` |
| Kontakti | `ContentResolver` | `query(ContactsContract...)` |
| Notifikacija | `NotificationCompat` | `.notify(id, notif)` |
| Threading | `lifecycleScope` | `launch(Dispatchers.IO)` |

Srećno sutra! Ako te nešto zakoči u toku rada, samo pitaj.
