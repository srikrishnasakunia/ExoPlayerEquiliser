package dev.krishna.exoplayerequiliser.ui.viewmodel

import android.media.audiofx.Equalizer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krishna.exoplayerequiliser.data.model.AudioEffects
import dev.krishna.exoplayerequiliser.data.storage.EqualizerPreference
import dev.krishna.exoplayerequiliser.utils.ACOUSTIC
import dev.krishna.exoplayerequiliser.utils.DANCE
import dev.krishna.exoplayerequiliser.utils.FLAT
import dev.krishna.exoplayerequiliser.utils.HIP_HOP
import dev.krishna.exoplayerequiliser.utils.JAZZ
import dev.krishna.exoplayerequiliser.utils.PODCAST
import dev.krishna.exoplayerequiliser.utils.POP
import dev.krishna.exoplayerequiliser.utils.PRESET_ACOUSTIC
import dev.krishna.exoplayerequiliser.utils.PRESET_CUSTOM
import dev.krishna.exoplayerequiliser.utils.PRESET_DANCE
import dev.krishna.exoplayerequiliser.utils.PRESET_FLAT
import dev.krishna.exoplayerequiliser.utils.PRESET_HIP_HOP
import dev.krishna.exoplayerequiliser.utils.PRESET_JAZZ
import dev.krishna.exoplayerequiliser.utils.PRESET_PODCAST
import dev.krishna.exoplayerequiliser.utils.PRESET_POP
import dev.krishna.exoplayerequiliser.utils.PRESET_ROCK
import dev.krishna.exoplayerequiliser.utils.ROCK
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AudioEqualizerViewModel @Inject constructor(
    private val equalizerPreference: EqualizerPreference
): ViewModel() {

    val audioEffects = MutableStateFlow<AudioEffects?>(null)

    private var equalizer: Equalizer? = null

    val enableEqualizer = MutableStateFlow(false)

    private var audioSessionId = 0

    init {
        enableEqualizer.value = equalizerPreference.isEqualizerEnabled
        audioEffects.tryEmit(equalizerPreference.audioEffect)

        if (audioEffects.value == null){
            audioEffects.tryEmit(AudioEffects(PRESET_FLAT, FLAT))
        }
    }

    fun onStart(sessionId: Int){
        audioSessionId = sessionId
        equalizer?.enabled = enableEqualizer.value
        equalizer = Equalizer(Int.MAX_VALUE,audioSessionId)

        equalizerPreference.lowestBandLevel = equalizer?.bandLevelRange?.get(0)?.toInt() ?: 0

        audioEffects.value?.gainValues?.forEachIndexed { index, value ->
            val bandLevel = (value * 1000).toInt().toShort()
            equalizer?.setBandLevel(index.toShort(),bandLevel)
        }
    }

    fun onSelectPreset(presetPosition: Int) {
        if(audioEffects.value == null) return

        val gain = if (presetPosition == PRESET_CUSTOM){
            ArrayList(audioEffects.value!!.gainValues)
        } else {
            ArrayList(getPresetGainValue(presetPosition))
        }

        audioEffects.tryEmit(AudioEffects(presetPosition,gain))
        equalizerPreference.audioEffect = audioEffects.value

        equalizer?.apply {
            gain.forEachIndexed { index, value ->
                val bandLevel = (value * 1000).toInt().toShort()
                setBandLevel(index.toShort(),bandLevel)
            }
        }
    }

    fun onBandLevelChanged(changedBand: Int, newGainValue: Int) {
        val lowest = equalizerPreference.lowestBandLevel

        val bandLevel = newGainValue.plus(lowest)

        equalizer?.setBandLevel(changedBand.toShort(),bandLevel.toShort())
        val list = ArrayList(audioEffects.value!!.gainValues)
        list[changedBand] = (newGainValue.toDouble() / 1000)
        audioEffects.tryEmit(
            AudioEffects(
                PRESET_CUSTOM,
                list
            )
        )
        equalizerPreference.audioEffect = audioEffects.value
    }

    fun toggleEqualizer() {
        enableEqualizer.tryEmit(!enableEqualizer.value)
        equalizer?.enabled = enableEqualizer.value
        equalizerPreference.isEqualizerEnabled = enableEqualizer.value
        if (!enableEqualizer.value) {
            audioEffects.tryEmit(AudioEffects(PRESET_FLAT, FLAT))
            equalizerPreference.audioEffect = audioEffects.value
        }
    }

    private fun getPresetGainValue(index: Int): List<Double> {
        return when (index) {
            PRESET_FLAT -> FLAT
            PRESET_ACOUSTIC -> ACOUSTIC
            PRESET_DANCE -> DANCE
            PRESET_JAZZ -> JAZZ
            PRESET_HIP_HOP -> HIP_HOP
            PRESET_PODCAST -> PODCAST
            PRESET_POP -> POP
            PRESET_ROCK -> ROCK
            else -> FLAT
        }
    }
}