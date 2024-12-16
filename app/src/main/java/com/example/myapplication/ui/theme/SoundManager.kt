package com.example.myapplication.ui.theme

import android.content.Context
import android.media.SoundPool
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.myapplication.R

class SoundManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private var hsSoundId = 0
    private var hilSoundId = 0
    private var swordSoundId = 0
    private var textSoundId = 0
    private var menuSoundId = 0
    private var soundsLoaded = 0
    private var totalSoundsToLoad = 3
    val soundLoadSuccess = mutableStateOf(false) //track if loading succeeded


    init {
        soundPool = SoundPool.Builder().setMaxStreams(3).build()
        soundPool?.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                Log.d("SoundManager", "Sound loaded: $sampleId")
                soundsLoaded++
                if (soundsLoaded == totalSoundsToLoad) {
                    soundLoadSuccess.value = true // Set to true only when all sounds are loaded
                }
            } else {
                Log.e("SoundManager", "Sound loading failed: $sampleId, status: $status")
                soundsLoaded = 0 // Reset if any fail
                soundLoadSuccess.value = false
            }
        }

        hsSoundId = soundPool!!.load(context, R.raw.xp, 1)
        hilSoundId = soundPool!!.load(context, R.raw.yron, 1)
        swordSoundId = soundPool!!.load(context, R.raw.hs, 1)
        textSoundId = soundPool!!.load(context,R.raw.anime_text,1)
       menuSoundId = soundPool!!.load(context,R.raw.memalert,1)
    }

    fun playSound(type: SoundType) {
        if (!soundLoadSuccess.value) {
            Log.w("SoundManager", "Sounds not fully loaded yet. Trying again later.")
            return
        }
        soundPool?.let {
            val soundId = when (type) {
                SoundType.DECREASE -> hsSoundId
                SoundType.INCREASE -> hilSoundId
                SoundType.RESET -> swordSoundId
                SoundType.Text -> textSoundId
                SoundType.Menu -> menuSoundId
                else -> 0 // handle unknown type
            }
            if(soundId !=0){
                it.play(soundId, 1f, 1f, 1, 0, 1f)
            } else{
                Log.e("SoundManager", "Sound ID is 0 for type: $type")
            }
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }

    enum class SoundType {
        DECREASE,
        INCREASE,
        RESET,
        Text,
        Menu
    }
}

