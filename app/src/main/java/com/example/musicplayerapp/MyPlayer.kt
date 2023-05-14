package com.example.musicplayerapp

import android.media.MediaPlayer

object MyPlayer {
    private var instance: MediaPlayer? = null
    var currentIndex = -1

    fun getInstance(): MediaPlayer {
        if (instance == null) {
            instance = MediaPlayer()
        }
        return instance!!
    }
}

