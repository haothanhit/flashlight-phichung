package flashlight.phichung.com.torch.ui.screen.brightness

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.flask.colorpicker.ColorPickerView
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor
import flashlight.phichung.com.torch.utils.hexToColor
import timber.log.Timber


object BrightnessNavigation {

    const val titleScreen = "Brightness"
    const val route = "brightness"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrightnessScreen(
    viewModel: BrightnessViewModel = hiltViewModel<BrightnessViewModel>(),
    navController: NavHostController = rememberNavController()
) {
    var stateIsShowUi by remember { mutableStateOf(true) }
    var stateColorCurrent by remember { mutableStateOf("#191919") }

    if (!stateIsShowUi) {
        Scaffold(
            containerColor = hexToColor(stateColorCurrent),
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {

                    },
                    actions = {
                        IconButton(onClick = { stateIsShowUi = true }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_eyes),
                                contentDescription = "Button Open eyes",
                                colorFilter = ColorFilter.tint(IconWhiteColor)
                            )
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )


                )
            },
            content = ({

            }),

            )
    } else {
        Scaffold(
            containerColor = hexToColor(stateColorCurrent),

            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = BrightnessNavigation.titleScreen,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextWhiteColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.KeyboardArrowLeft, null, tint = IconWhiteColor)
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconButton(onClick = { stateIsShowUi = false }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_close_eyes),
                                contentDescription = "Button Close eyes",
                                colorFilter = ColorFilter.tint(IconWhiteColor)
                            )
                        }
                    }


                )
            },
            content = ({
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                ) {
//                   Spacer(modifier = Modifier.fillMaxWidth().size(1.dp).shadow(1.dp))
                    AndroidView(
                        modifier = Modifier.size(150.dp),
                        factory = { context ->
                            // Inflate your custom view here
                            val customView = ColorPickerView(context)
                            customView.addOnColorChangedListener {
                                Timber.i(
                                    "ColorPickerView addOnColorChangedListener: ${
                                        Integer.toHexString(
                                            it
                                        )
                                    }"
                                )

                                stateColorCurrent = "#${Integer.toHexString(it).substring(2)}"
                            }
                            customView
                        }
                    )
                    Row {
                        SliderBrightness(modifier = Modifier.weight(1f))

                        SliderBlink(modifier = Modifier.weight(1f))

                    }
                }

            }),

            )
    }





}




@Preview
@Composable
fun SliderBrightness(modifier: Modifier= Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column(modifier=modifier.fillMaxWidth()) {


        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 9,
            valueRange = 0f..10f,
            modifier = Modifier.rotate(270f)

        )
        Text(text = "Brightness", style = MaterialTheme.typography.titleLarge, color = TextWhiteColor)

    }
}

@Preview
@Composable
fun SliderBlink(modifier: Modifier= Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column(modifier=modifier.fillMaxWidth().padding(15.dp)) {

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 9,
            valueRange = 0f..10f,
            modifier = Modifier.rotate(270f)

        )
        Text(text = "Blink", style = MaterialTheme.typography.titleLarge, color = TextWhiteColor)

    }
}


