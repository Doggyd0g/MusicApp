package com.example.musicplayerapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity(), Runnable {

    private lateinit var titleTv: TextView
    private lateinit var currentTimeTv: TextView
    private lateinit var totalTimeTv: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var pausePlay: ImageView
    private lateinit var nextBtn: ImageView
    private lateinit var previousBtn: ImageView
    private lateinit var musicIcon: ImageView
    private lateinit var songsList: ArrayList<AudioModel>
    private lateinit var currentSong: AudioModel
    private lateinit var btInfo: Button
    private val mediaPlayer: MediaPlayer = MyPlayer.getInstance()
    private var x = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        titleTv = findViewById(R.id.song_title)
        currentTimeTv = findViewById(R.id.current_time)
        totalTimeTv = findViewById(R.id.total_time)
        seekBar = findViewById(R.id.seek_bar)
        pausePlay = findViewById(R.id.pause_play)
        nextBtn = findViewById(R.id.next)
        previousBtn = findViewById(R.id.previous)
        musicIcon = findViewById(R.id.music_icon_big)
        btInfo = findViewById(R.id.button1)

        titleTv.isSelected = true
        songsList = intent.getSerializableExtra("LIST") as ArrayList<AudioModel>
        setResourcesWithMusic()

        runOnUiThread {
            if (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
                currentTimeTv.text = convertToMMSS(mediaPlayer.currentPosition.toString())
                pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                //musicIcon.rotation = x++.toFloat()
            } else {
                pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                musicIcon.rotation = 0f
            }
            Handler().postDelayed(this, 100)
        }

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        btInfo.setOnClickListener {
            Toast.makeText(this, "Приложение, Перова Егора", Toast.LENGTH_SHORT).show()
        }
    }


    override fun run() {
        runOnUiThread {
            if (mediaPlayer.isPlaying) {
                seekBar.progress = mediaPlayer.currentPosition
                currentTimeTv.text = convertToMMSS(mediaPlayer.currentPosition.toString())
                pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                //musicIcon.rotation = x++.toFloat()
            } else {
                pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                musicIcon.rotation = 0f
            }
        }
        Handler().postDelayed(this, 100)
    }

    private fun setResourcesWithMusic() {
        currentSong = songsList[MyPlayer.currentIndex]
        titleTv.text = currentSong.title
        totalTimeTv.text = currentSong.duration?.let { convertToMMSS(it) }
        pausePlay.setOnClickListener { pausePlay() }
        nextBtn.setOnClickListener { playNextSong() }
        previousBtn.setOnClickListener { playPreviousSong() }
        playMusic()
    }

    private fun playMusic() {
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(currentSong.path)
            mediaPlayer.prepare()
            mediaPlayer.start()
            seekBar.progress = 0
            seekBar.max = mediaPlayer.duration
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun playNextSong() {
        if (MyPlayer.currentIndex == songsList.size - 1)
            return
        MyPlayer.currentIndex += 1
        mediaPlayer.reset()
        setResourcesWithMusic()
    }

    private fun playPreviousSong() {
        if (MyPlayer.currentIndex == 0)
            return
        MyPlayer.currentIndex -= 1
        mediaPlayer.reset()
        setResourcesWithMusic()
    }

    private fun pausePlay() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        else
            mediaPlayer.start()
    }

    private fun convertToMMSS(duration: String): String {
        val millis = duration.toLong()
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }
}

