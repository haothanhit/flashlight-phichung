package flashlight.phichung.com.torch.ui.screen.brightness


import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class BrightnessViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
    private val preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider) {


    private val _uiBlinkFloatState = MutableStateFlow(0f)
    val uiBlinkFloatState: StateFlow<Float> = _uiBlinkFloatState.asStateFlow()


    private val _uiIsBlinkState = MutableStateFlow(false)
    val uiIsBlinkState: StateFlow<Boolean> = _uiIsBlinkState.asStateFlow()

    private var isBlinking = false // Flag to control the blinking loop

    fun setBlinkState(blinkState: Float) {
        _uiBlinkFloatState.value = blinkState
    }

    fun setIsBlinkState(isBlink: Boolean) {
        _uiIsBlinkState.value = isBlink
    }


    fun toggleBlinkStateWithDelay(sliderPosition: Float) {

        launchCoroutineIO {
            Timber.d("sliderPosition: $sliderPosition")
            isBlinking = true
            val delayMs = ((10 - sliderPosition.toInt()) * 150L) // Adjust as needed
            while (isBlinking) {
                setIsBlinkState(true)
                delay(delayMs)
                setIsBlinkState(false)
                delay(delayMs) // Wait for 1 second

            }

        }
    }

    fun stopBlinking() {
        isBlinking = false
    }
}