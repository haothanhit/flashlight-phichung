package flashlight.phichung.com.torch.ui.screen.morse


import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.core.text.HtmlCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.MyApplication
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.data.model.Skin
import flashlight.phichung.com.torch.morse.DitDahGeneratorSettings
import flashlight.phichung.com.torch.morse.DitDahSoundStream
import flashlight.phichung.com.torch.morse.SoundTypes
import flashlight.phichung.com.torch.morse.StringToSoundSequence
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import flashlight.phichung.com.torch.utils.listSkin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MorseViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider) {




    private val _uiPlayState = MutableStateFlow(false) // isPlaying
    val uiPlayState: StateFlow<Boolean> = _uiPlayState.asStateFlow()

    private var mSoundPlayer: DitDahSoundStream

    private val _uiMorseCodeState = MutableStateFlow(listOf<SoundTypes>())
    val uiMorseCodeState: StateFlow<List<SoundTypes>> = _uiMorseCodeState.asStateFlow()


    private val _uiIndexCharacterState = MutableStateFlow(-1) // index of character


    private lateinit var  skinCurrent :Skin


    val combinedFlow: Flow<AnnotatedString> = _uiIndexCharacterState.combine(_uiMorseCodeState) { index, morseCodeList ->
        // Map index and morseCodeList to corresponding text colors

        Timber.i("HAOHAO combinedFlow index: $index, morseCodeList: $morseCodeList")
        val formattedString = morseCodeList.joinToString(separator = "") { soundType ->
            when (soundType) {
                SoundTypes.DIT -> "."
                SoundTypes.DAH -> "-"
                SoundTypes.LETTER_SPACE -> " "
                SoundTypes.WORD_SPACE -> "  "
                SoundTypes.UNDEFINED -> "*"
            }
        }
      buildAnnotatedString {
            formattedString.forEachIndexed { idx, char ->
                val color = if (idx <= index) skinCurrent.colorSkin else TextWhiteColor
                withStyle(style = androidx.compose.ui.text.SpanStyle(color = color)) {
                    append(char.toString())
                }
            }
        }


    }


    init {

        val generatorSettings = DitDahGeneratorSettings(MyApplication.instance)
        mSoundPlayer = DitDahSoundStream(generatorSettings)
        mSoundPlayer.streamNotificationListener =
            object : DitDahSoundStream.StreamNotificationListener {
                override fun symbolsExhausted(stream: DitDahSoundStream) {


                    Timber.i("symbolsExhausted: $stream")
                }

            }
        skinCurrent=getSkinCurrent()
    }

    fun setValuePlayState(playState: Boolean) {
        _uiPlayState.value = playState
        if (playState) {
            mSoundPlayer.enqueue(uiMorseCodeState.value)
            launchCoroutineIO {
                mSoundPlayer.makeSoundsWorkerThreadFunc(onDone = {
                    Timber.i("HAOHAO makeSoundsWorkerThreadFunc onDone")
                    _uiPlayState.value = false

                },_uiIndexCharacterState)

            }
        } else {
            _uiIndexCharacterState.value=-1
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