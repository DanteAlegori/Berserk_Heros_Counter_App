package com.example.myapplication.ui.theme

import android.content.Context
import android.media.SoundPool
import android.util.Log
import com.example.myapplication.R

class SoundManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private var hsSoundId = 0
    private var hilSoundId = 0
    private var swordSoundId = 0
    private var soundsLoaded = 0
    private var totalSoundsToLoad = 3 //Keep track of total sounds


    init {
        soundPool = SoundPool.Builder().setMaxStreams(3).build()
        soundPool?.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                Log.d("SoundManager", "Sound loaded: $sampleId")
                soundsLoaded++;
                soundsLoaded == totalSoundsToLoad; // Use === for strict equality

            } else {
                Log.e("SoundManager", "Sound loading failed: $sampleId, status: $status")
                soundsLoaded = 0 //If one fails, set to false
            }
        }

        hsSoundId = soundPool!!.load(context, R.raw.xp, 1)
        hilSoundId = soundPool!!.load(context, R.raw.yron, 1)
        swordSoundId = soundPool!!.load(context, R.raw.hs, 1)
    }

    fun playSound(type: SoundType) {
        if (soundsLoaded==0) {
            Log.w("SoundManager", "Sounds not fully loaded yet. Trying again later.")
            return
        }
        soundPool?.let {
            val soundId = when (type) {
                SoundType.DECREASE -> hsSoundId
                SoundType.INCREASE -> hilSoundId
                SoundType.RESET -> swordSoundId
            }
            it.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }

    enum class SoundType {
        DECREASE,
        INCREASE,
        RESET
    }
}
