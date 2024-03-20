package flashlight.phichung.com.torch.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import flashlight.phichung.com.torch.FlashlightApp
import flashlight.phichung.com.torch.ui.theme.FlashlightTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashlightTheme {
                FlashlightApp()
            }
        }
    }


}



