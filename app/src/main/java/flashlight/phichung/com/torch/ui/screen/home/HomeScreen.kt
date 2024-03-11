package flashlight.phichung.com.torch.ui.screen.home

import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import flashlight.phichung.com.torch.R
import flashlight.phichung.com.torch.ui.theme.GrayColor
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor


object HomeNavigation {

    const val route = "home"

}

@Composable
fun HomeScreen(
//    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
//    navController: NavHostController = rememberNavController(),
    openSkinAction: () -> Unit,
    openSettingsAction: () -> Unit,
    openColorAction: () -> Unit,
    openCompassAction: () -> Unit,
    openMorseAction: () -> Unit,
    openRemoveAdsAction: () -> Unit,
    openCameraAction: () -> Unit,
) {


    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.bg_home),
            contentDescription = "background home",
            contentScale = ContentScale.FillBounds
        )
        Scaffold( modifier = Modifier.fillMaxSize(), containerColor = Color.Transparent, content =({
            Column (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)){
                Spacer(modifier = Modifier.size(15.dp))
                SliderFlash()
                ButtonPower(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly) {

                    ButtonChild(
                        icon = R.drawable.ic_brightness,
                        clickAction = openColorAction
                    )
                    ButtonChild(
                        icon = R.drawable.ic_camera,
                        clickAction = openCameraAction
                    )

                    ButtonChild(
                        icon = R.drawable.ic_compass,
                        clickAction = openCompassAction
                    )

                    ButtonChild(
                        icon = R.drawable.ic_morse,
                        clickAction = openMorseAction
                    )
                }
                Spacer(modifier = Modifier.size(15.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly) {

                    ButtonChild(
                        icon = R.drawable.ic_volume_off,
                        clickAction = {

                        }
                    )
                    ButtonChild(
                        icon = R.drawable.ic_skin,
                        clickAction = openSkinAction
                    )

                    ButtonChild(
                        icon = R.drawable.ic_pro,
                        clickAction = openRemoveAdsAction
                    )

                    ButtonChild(
                        icon = R.drawable.ic_settings,
                        clickAction = openSettingsAction
                    )
                }
                Spacer(modifier = Modifier.size(15.dp))


            }
        }))
    }







}



@Preview
@Composable
fun SliderFlash(modifier: Modifier= Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column {

        Text(text = if(sliderPosition==0f) "S0S" else sliderPosition.toString(), style = MaterialTheme.typography.titleLarge, color = TextWhiteColor)
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

        )
    }
}

@Preview
@Composable
fun ButtonPower(modifier: Modifier= Modifier) {


    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image (
            painter = painterResource(id = R.drawable.ic_power),
            contentDescription = "Button Power",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .shadow(10.dp, CircleShape)
                .border(
                    BorderStroke(4.dp, Color.Yellow),
                    CircleShape
                )
                .clip(CircleShape)
        )
    }
}


@Preview
@Composable
fun ButtonChild(modifier: Modifier?= Modifier,@DrawableRes icon: Int,clickAction: () -> Unit = {}) {

    Box(modifier = Modifier
        .size(50.dp)
        .clickable { clickAction.invoke() }, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "",
//            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(IconWhiteColor),
            modifier = Modifier
                .size(50.dp)
                .border(
                    BorderStroke(2.dp, GrayColor),
                    CircleShape
                )
                .padding(10.dp)
                .clip(CircleShape)
        )
    }




}
