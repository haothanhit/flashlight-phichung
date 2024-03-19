package flashlight.phichung.com.torch.ui.screen.morse


import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.MyApplication
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.data.model.Skin
import flashlight.phichung.com.torch.morse.DitDahGeneratorSettings
import flashlight.phichung.com.torch.morse.DitDahSoundStream
import flashlight.phichung.com.torch.morse.SoundTypes
import flashlight.phichung.com.torch.morse.StringToSoundSequence
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import flashlight.phichung.com.torch.utils.listSkin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MorseViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider) {


    private val _uiMorseCodeState = MutableStateFlow(listOf<SoundTypes>())
    val uiMorseCodeState: StateFlow<List<SoundTypes>> = _uiMorseCodeState.asStateFlow()


    private val _uiPlayState = MutableStateFlow(false)
    val uiPlayState: StateFlow<Boolean> = _uiPlayState.asStateFlow()

    private var mSoundPlayer: DitDahSoundStream


    init {

        val generatorSettings = DitDahGeneratorSettings(MyApplication.instance)
        mSoundPlayer = DitDahSoundStream(generatorSettings)
        mSoundPlayer.streamNotificationListener = object : DitDahSoundStream.StreamNotificationListener {
            override fun symbolsExhausted(stream: DitDahSoundStream) {


                Timber.i("symbolsExhausted: $stream")
            }

        }

    }

    fun setValuePlayState(playState: Boolean) {
        _uiPlayState.value = playState
        if(playState) {
            mSoundPlayer.enqueue(uiMorseCodeState.value)
        } else {
            mSoundPlayer.quit()
        }

    }


    fun generateTextMorse(text: String) {
        Timber.i("HAOHAO $uiMorseCodeState : $text")
        _uiMorseCodeState.value = StringToSoundSequence(text.uppercase(Locale.ROOT))
    }


    fun getSkinCurrent(): Skin {
        return preferencesHelper.skin ?: listSkin[0]
    }


}