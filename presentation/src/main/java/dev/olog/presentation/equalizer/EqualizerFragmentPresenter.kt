package dev.olog.presentation.equalizer

import android.content.Context
import dev.olog.core.dagger.ApplicationContext
import dev.olog.core.prefs.EqualizerPreferencesGateway
import dev.olog.injection.equalizer.IBassBoost
import dev.olog.injection.equalizer.IEqualizer
import dev.olog.injection.equalizer.IVirtualizer
import dev.olog.presentation.R
import javax.inject.Inject

internal class EqualizerFragmentPresenter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val equalizer: IEqualizer,
    private val bassBoost: IBassBoost,
    private val virtualizer: IVirtualizer,
    private val equalizerPrefsUseCase: EqualizerPreferencesGateway
) {

    fun getPresets() = try {
        equalizer.getPresets()
    } catch (ex: Throwable) {
        ex.printStackTrace()
        listOf(context.getString(R.string.equalizer_not_found))
    }

    fun getCurrentPreset() = equalizer.getCurrentPreset()

    fun setPreset(position: Int) {
        equalizer.setPreset(position)
    }

    fun isEqualizerEnabled(): Boolean = equalizerPrefsUseCase.isEqualizerEnabled()

    fun setEqualizerEnabled(enabled: Boolean) {
        equalizer.setEnabled(enabled)
        virtualizer.setEnabled(enabled)
        bassBoost.setEnabled(enabled)
        equalizerPrefsUseCase.setEqualizerEnabled(enabled)
    }

    fun getBandLevel(band: Int): Float = equalizer.getBandLevel(band) / 100

    fun setBandLevel(band: Int, level: Float) {
        equalizer.setBandLevel(band, level * 100)
    }

    fun getBassStrength(): Int = bassBoost.getStrength() / 10

    fun setBassStrength(value: Int) {
        bassBoost.setStrength(value * 10)
    }

    fun getVirtualizerStrength(): Int = virtualizer.getStrength() / 10

    fun setVirtualizerStrength(value: Int) {
        virtualizer.setStrength(value * 10)
    }

    fun addEqualizerListener(listener: IEqualizer.Listener) {
        equalizer.addListener(listener)
    }

    fun removeEqualizerListener(listener: IEqualizer.Listener) {
        equalizer.removeListener(listener)
    }

}
