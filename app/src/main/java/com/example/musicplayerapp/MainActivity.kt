package com.example.musicplayerapp

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.*
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noMusicTextView: TextView
    private val songsList = ArrayList<AudioModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        noMusicTextView = findViewById(R.id.no_songs_text)

        if (!checkPermission()) {
            requestPermission()
            return
        }

        val projection = arrayOf(
           Audio.Media.TITLE,
           Audio.Media.DATA,
           Audio.Media.DURATION
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = contentResolver.query(Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        cursor?.let {
            while (it.moveToNext()) {
                val songData = AudioModel(it.getString(1), it.getString(0), it.getString(2))
                if (File(songData.path).exists()) {
                    songsList.add(songData)
                }
            }
            it.close()
        }

        if (songsList.isEmpty()) {
            noMusicTextView.visibility = View.VISIBLE
        } else {
            // RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = MusicListAdapter(songsList, applicationContext)
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this@MainActivity,
                "Разрешить приложению доступ к файлам",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
        }
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = MusicListAdapter(songsList, applicationContext)
    }
}