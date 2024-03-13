package flashlight.phichung.com.torch.ui.screen.home


import android.hardware.camera2.CameraManager
import dagger.hilt.android.lifecycle.HiltViewModel
import flashlight.phichung.com.torch.base.BaseViewModel
import flashlight.phichung.com.torch.data.CachePreferencesHelper
import flashlight.phichung.com.torch.utils.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    contextProvider: CoroutineContextProvider,
//    private val preferencesHelper: CachePreferencesHelper,

    ) : BaseViewModel(contextProvider) {



    private val _uiFlashFloatBlinkState = MutableStateFlow(1f)
    val uiFlashFloatBlinkState: StateFlow<Float> = _uiFlashFloatBlinkState.asStateFlow() // speech blink



    private val _uiPowerState = MutableStateFlow(false)
    val uiPowerState: StateFlow<Boolean> = _uiPowerState.asStateFlow()   //on off

    var job: Job? = null

    fun setValueFlashFloatBlinkState(blinkState: Float) {
        _uiFlashFloatBlinkState.value = blinkState
    }




    fun setPowerState(powerState: Boolean) {
        _uiPowerState.value = powerState
    }


    fun toggleBlinkStateWithDelay(
        camManager: CameraManager?,
        cameraId: String?
    ) {

        if (!uiPowerState.value) {
            camManager?.setTorchMode(cameraId!!, false) // Turn OFF
            job?.cancel() // Cancel the previous coroutine if it exists
            return
        }
        job?.cancel() // Cancel the previous coroutine if it exists
        
        val blinkValue= uiFlashFloatBlinkState.value.toInt()

        Timber.i("blinkValue: " +blinkValue.toString())
        
        if (blinkValue == 0) { //SOS
            job = CoroutineScope(Dispatchers.IO).launch {

                while (uiPowerState.value) {
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())

                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(600.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(600.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(600.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(200.toLong())
                    camManager?.setTorchMode(cameraId!!, true)   // Turn ON
                   delay(200.toLong())

                    camManager?.setTorchMode(cameraId!!, false)   // Turn OFF
                   delay(1400.toLong())
                }
            } 
            
        }else if (blinkValue==1){
            camManager?.setTorchMode(cameraId!!, true) // Turn ON

        }else{
            job = CoroutineScope(Dispatchers.IO).launch {
                val delayMs = ((10 - blinkValue) * 100L) // Adjust as needed

                while (uiPowerState.value) {
                    camManager?.setTorchMode(cameraId!!, true) // Turn ON
                    delay(delayMs)
                    camManager?.setTorchMode(cameraId!!, false) // Turn OFF
                    delay(delayMs) // Wait for 1 second
                }
            }
        }

     

    }





}