package com.example.musicplayerapp

import java.io.Serializable
import android.os.Parcel
import android.os.Parcelable

class AudioModel(
    var path: String?,
    var title: String?,
    var duration: String?
) : java.io.Serializable


