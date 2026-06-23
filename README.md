# ANDROID KOLOKVIJUM 2 – KOMPLETAN VODIČ

---

## REDOSLED RADA (ne menjaj redosled!)

```
1. build.gradle (app)       → dodaj biblioteke → Sync Now
2. AndroidManifest.xml      → dodaj permisije + FileProvider
3. res/xml/file_paths.xml   → napravi novi fajl
4. activity_main.xml        → postavi UI
5. PostEntity.kt            → napravi paket "database", dodaj fajl
6. AppDatabase.kt           → u paketu "database"
7. ApiService.kt            → napravi paket "network", dodaj fajl
8. MainActivity.kt          → poslednje, kad sve ostalo postoji
```

---

## KORAK 1 — build.gradle (app)

Otvori `Gradle Scripts → build.gradle (Module: app)` i zameni ceo sadržaj:

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
}

android {
    namespace 'com.example.kolokvijum2'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.kolokvijum2"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Lifecycle – potrebno za lifecycleScope
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'

    // Room – baza podataka
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // Retrofit – HTTP zahtevi
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}
```

**Nakon što zalepite → kliknite "Sync Now" (žuta traka gore) i sačekajte da završi.**

---

## KORAK 2 — AndroidManifest.xml

Otvori `app/src/main/AndroidManifest.xml` i zameni ceo sadržaj:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.kolokvijum2">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <uses-feature android:name="android.hardware.camera" android:required="false" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Kolokvijum2"
        android:usesCleartextTraffic="true">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>

    </application>
</manifest>
```

---

## KORAK 3 — res/xml/file_paths.xml

Ovaj fajl ne postoji automatski. Napravi ga:
- Desni klik na `res` → New → Android Resource File
- File name: `file_paths`
- Resource type: `XML`
- Klikni OK

Zalepiti sadržaj:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path
        name="my_images"
        path="Pictures/" />
</paths>
```

---

## KORAK 4 — activity_main.xml

Otvori `res/layout/activity_main.xml`, prebaci na **Code** pogled i zameni ceo sadržaj:

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:gravity="center_horizontal">

        <TextView
            android:id="@+id/tvLocation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            android:padding="8dp"
            android:text="Učitavanje lokacije..."
            android:textSize="16sp" />

        <ImageButton
            android:id="@+id/ibCamera"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            android:contentDescription="Otvori kameru"
            android:src="@android:drawable/ic_menu_camera" />

        <ImageView
            android:id="@+id/ivPhoto"
            android:layout_width="match_parent"
            android:layout_height="250dp"
            android:layout_marginBottom="16dp"
            android:contentDescription="Fotografija"
            android:scaleType="centerCrop"
            android:background="@android:color/darker_gray" />

        <Switch
            android:id="@+id/switchPosts"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            android:text="Postovi" />

        <Button
            android:id="@+id/btnDelete"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="X:0.0 Y:0.0 Z:0.0" />

    </LinearLayout>
</ScrollView>
```

---

## KORAK 5 — database/PostEntity.kt

Napravi paket: desni klik na `com.example.kolokvijum2` → New → Package → ukucaj `database`

Unutar tog paketa: desni klik → New → Kotlin Class/File → naziv `PostEntity`

Zalepiti kompletan sadržaj:

```kotlin
package com.example.kolokvijum2.database

import androidx.room.*

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)

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

**Napomena:** `ORDER BY rowid ASC` znači prvi UPISANI post u tabelu, ne post sa najmanjim ID-jem.

---

## KORAK 6 — database/AppDatabase.kt

U paketu `database`: desni klik → New → Kotlin Class/File → naziv `AppDatabase`

Zalepiti kompletan sadržaj:

```kotlin
package com.example.kolokvijum2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kolokvijum2_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

## KORAK 7 — network/ApiService.kt

Napravi paket: desni klik na `com.example.kolokvijum2` → New → Package → ukucaj `network`

Unutar tog paketa: desni klik → New → Kotlin Class/File → naziv `ApiService`

Zalepiti kompletan sadržaj:

```kotlin
package com.example.kolokvijum2.network

import com.example.kolokvijum2.database.PostEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class PostsResponse(
    val posts: List<PostEntity>
)

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): PostsResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://app.beeceptor.com/mock-server/dummy-json/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

---

## KORAK 8 — MainActivity.kt

Otvori postojeći `MainActivity.kt` i zameni CELO sadržaj:

```kotlin
package com.example.kolokvijum2

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.kolokvijum2.database.AppDatabase
import com.example.kolokvijum2.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    // ── UI elementi ──────────────────────────────────────────────────────────
    private lateinit var tvLocation: TextView
    private lateinit var ibCamera: ImageButton
    private lateinit var ivPhoto: ImageView
    private lateinit var switchPosts: Switch
    private lateinit var btnDelete: Button

    // ── Senzori ──────────────────────────────────────────────────────────────
    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null
    private var accelerometer: Sensor? = null

    private var gyroX = 0f
    private var gyroY = 0f
    private var gyroZ = 0f

    // ── Kamera ───────────────────────────────────────────────────────────────
    private var photoUri: Uri? = null

    // ── Baza ─────────────────────────────────────────────────────────────────
    private lateinit var db: AppDatabase

    // ── Konstante ────────────────────────────────────────────────────────────
    private val PREFS_NAME = "MyPrefs"
    private val PREFS_TEKST = "tekst"
    private val CHANNEL_ID = "posts_channel"
    private val NOTIF_ID = 1

    // ── Launcher za kameru ───────────────────────────────────────────────────
    // Poziva se nakon što korisnik slikanje fotografije
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                // Prikaži sliku u ImageView
                ivPhoto.setImageURI(photoUri)
                // Prikaži žiroskop vrednosti u Toast
                Toast.makeText(
                    this,
                    "Žiroskop → X: %.2f  Y: %.2f  Z: %.2f".format(gyroX, gyroY, gyroZ),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    // ── Launcher za permisije ────────────────────────────────────────────────
    // Poziva se nakon što korisnik prihvati ili odbije dozvole
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                fetchLocation()
            }
        }

    // ─────────────────────────────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Povezi UI elemente sa promenljivima
        bindViews()

        // 2. Inicijalizuj senzore
        setupSensors()

        // 3. Napravi notifikacioni kanal (jednom, pri pokretanju)
        setupNotificationChannel()

        // 4. Inicijalizuj bazu
        db = AppDatabase.getDatabase(this)

        // 5. Zatraži permisije i učitaj lokaciju
        requestPermissionsIfNeeded()

        // 6. Postavi klikove i listenere
        setupCamera()
        setupSwitch()
        setupDeleteButton()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Registruj senzore kad se ekran pojavi
    override fun onResume() {
        super.onResume()
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Deregistruj senzore kad napustiš ekran (štedi bateriju)
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // ─────────────────────────────────────────────────────────────────────────
    private fun bindViews() {
        tvLocation  = findViewById(R.id.tvLocation)
        ibCamera    = findViewById(R.id.ibCamera)
        ivPhoto     = findViewById(R.id.ivPhoto)
        switchPosts = findViewById(R.id.switchPosts)
        btnDelete   = findViewById(R.id.btnDelete)
    }

    // ─────────────────────────────────────────────────────────────────────────
    private fun setupSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gyroscope     = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Ova metoda se poziva automatski svaki put kad senzor dobije novo očitavanje
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_GYROSCOPE -> {
                gyroX = event.values[0]
                gyroY = event.values[1]
                gyroZ = event.values[2]
            }
            Sensor.TYPE_ACCELEROMETER -> {
                // Zadatak 8: tekst dugmeta = akcelerometar u realnom vremenu
                btnDelete.text = "X:%.1f Y:%.1f Z:%.1f".format(
                    event.values[0], event.values[1], event.values[2]
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Ne treba ništa ovde
    }

    // ─────────────────────────────────────────────────────────────────────────
    private fun requestPermissionsIfNeeded() {
        val needed = mutableListOf<String>()

        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            needed += Manifest.permission.ACCESS_FINE_LOCATION

        if (!hasPermission(Manifest.permission.CAMERA))
            needed += Manifest.permission.CAMERA

        if (!hasPermission(Manifest.permission.READ_CONTACTS))
            needed += Manifest.permission.READ_CONTACTS

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !hasPermission(Manifest.permission.POST_NOTIFICATIONS))
            needed += Manifest.permission.POST_NOTIFICATIONS

        if (needed.isNotEmpty()) {
            permissionLauncher.launch(needed.toTypedArray())
        } else {
            fetchLocation()
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Zadatak 3: Lokacija u TextView
    private fun fetchLocation() {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) return

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (location != null) {
                tvLocation.text = "Lat: ${location.latitude}\nLon: ${location.longitude}"
            } else {
                // Ako nema keširane lokacije, zatraži novu
                locationManager.requestSingleUpdate(
                    LocationManager.NETWORK_PROVIDER,
                    object : LocationListener {
                        override fun onLocationChanged(loc: android.location.Location) {
                            tvLocation.text = "Lat: ${loc.latitude}\nLon: ${loc.longitude}"
                        }
                    },
                    mainLooper
                )
            }
        } catch (e: SecurityException) {
            tvLocation.text = "Lokacija nije dostupna"
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Zadatak 4: Kamera
    private fun setupCamera() {
        ibCamera.setOnClickListener {
            if (!hasPermission(Manifest.permission.CAMERA)) {
                permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                return@setOnClickListener
            }
            val photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                photoFile
            )
            cameraLauncher.launch(photoUri)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Zadatak 6: Switch – ON učitaj sa API i upiši u bazu, svaki sledeći put prikaži title prvog posta
    // Zadatak 9: Switch – OFF sačuvaj u SharedPreferences, prikaži prvi kontakt
    private fun setupSwitch() {
        switchPosts.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch je ON
                lifecycleScope.launch(Dispatchers.IO) {
                    val count = db.postDao().getCount()

                    if (count == 0) {
                        // PRVI PUT – dohvati sa API i upiši u bazu
                        try {
                            val response = RetrofitClient.api.getPosts()
                            val first10 = response.posts.take(10)
                            db.postDao().insertAll(first10)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Upisano ${first10.size} postova",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Greška: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        // SVAKI SLEDEĆI PUT – prikaži title prvog posta iz baze
                        val firstPost = db.postDao().getFirstPost()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                firstPost?.title ?: "Nema postova",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            } else {
                // Switch je OFF – Zadatak 9
                // Sačuvaj tekst iz TextView u SharedPreferences
                val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit().putString(PREFS_TEKST, tvLocation.text.toString()).apply()

                // Zameni tekst u TextView sa imenom prvog kontakta
                val firstContact = getFirstContact()
                tvLocation.text = firstContact ?: "Nema kontakata"
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Zadatak 7: Brisanje prvog posta; notifikacija ako je baza prazna
    private fun setupDeleteButton() {
        btnDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val firstPost = db.postDao().getFirstPost()

                if (firstPost != null) {
                    db.postDao().delete(firstPost)
                    val remaining = db.postDao().getCount()

                    withContext(Dispatchers.Main) {
                        if (remaining == 0) {
                            sendNoPostsNotification()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Post obrisan, ostalo: $remaining",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        sendNoPostsNotification()
                    }
                }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Posts Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }

    private fun sendNoPostsNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !hasPermission(Manifest.permission.POST_NOTIFICATIONS)) return

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Postovi")
            .setContentText("Nema više postova!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this).notify(NOTIF_ID, notification)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Zadatak 9: Čitanje prvog kontakta iz Contacts aplikacije
    private fun getFirstContact(): String? {
        if (!hasPermission(Manifest.permission.READ_CONTACTS)) return null

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        return cursor?.use {
            if (it.moveToFirst()) {
                it.getString(
                    it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                )
            } else null
        }
    }
}
```

---

## BRZE NAPOMENE ZA KOLOKVIJUM

### Česte greške koje padaju ljude:

**1. Zaboravljeni `kotlin-kapt` plugin**
Bez `id 'kotlin-kapt'` u build.gradle, Room neće da kompajlira.

**2. Room na Main threadu**
Svaki poziv na bazu mora biti unutar:
```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    // baza ovde
    withContext(Dispatchers.Main) {
        // UI promene ovde
    }
}
```

**3. Kamera pada na Android 7+**
Bez FileProvider u Manifestu + file_paths.xml aplikacija pada pri pokušaju otvaranja kamere.

**4. Notifikacije na Android 13+**
Mora se dodati `POST_NOTIFICATIONS` permisija I zatražiti runtime.

**5. `usesCleartextTraffic="true"` u Manifestu**
Beeceptor koristi HTTP (ne HTTPS). Bez ovog atributa u `<application>` tagu, Retrofit neće moći da se poveže.

### Ako API ne vraća očekivani JSON:
Proveri strukturu odgovora na https://app.beeceptor.com/mock-server/dummy-json/posts i prilagodi `PostsResponse` wrapper klasu.

### Senzori – ne zaboravi:
- `onResume` → `registerListener`
- `onPause` → `unregisterListener`
- Implementiraj i `onAccuracyChanged` (čak i prazna metoda, mora postojati)
