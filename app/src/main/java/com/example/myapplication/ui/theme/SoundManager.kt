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
    private var soundsLoaded = false // Flag to track loading status

    init {
        soundPool = SoundPool.Builder().setMaxStreams(3).build()
        soundPool?.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                Log.d("SoundManager", "Sound loaded: $sampleId")
                if (sampleId == swordSoundId) soundsLoaded = true //only set to true when sword is loaded
            } else {
                Log.e("SoundManager", "Sound loading failed: $sampleId, status: $status")
            }
        }

        hsSoundId = soundPool!!.load(context, R.raw.stokovoexp, 1) //Priority 1 for all sounds
        hilSoundId = soundPool!!.load(context, R.raw.xp, 1)
        swordSoundId = soundPool!!.load(context, R.raw.yron, 1)
    }

    fun playSound(type: SoundType) {
        if (!soundsLoaded) {
            Log.w("SoundManager", "Sounds not fully loaded yet. Trying again later.")
            return // Don't play if sounds aren't loaded
        }
        val soundId = when (type) {
            SoundType.DECREASE -> hsSoundId
            SoundType.INCREASE -> hilSoundId
            SoundType.RESET -> swordSoundId
        }
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }

    enum class SoundType(val resourceId: Int) {
        DECREASE(R.raw.glass),
        INCREASE(R.raw.hil),
        RESET(R.raw.sword)
    }
}

