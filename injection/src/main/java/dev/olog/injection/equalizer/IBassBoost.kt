package dev.olog.injection.equalizer

interface IBassBoost {

    fun getStrength(): Int
    fun setStrength(value: Int)

    fun onAudioSessionIdChanged(audioSessionId: Int)

    fun setEnabled(enabled: Boolean)

    fun release()

}