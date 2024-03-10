package flashlight.phichung.com.torch.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


object HomeNavigation {

    const val route = "home"

}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    navController: NavHostController = rememberNavController(),
    openSkinAction: () -> Unit,
    openSettingsAction: () -> Unit,
    openColorAction: () -> Unit,
    openCompassAction: () -> Unit,
    openMorseAction: () -> Unit,
    openRemoveAdsAction: () -> Unit,
    openCameraAction: () -> Unit,
) {


    Column {
        Button(onClick = {
            openSettingsAction()
        }) {
            Text(text = "Go to Settings")
        }

        Button(onClick = {
            openCameraAction()
        }) {
            Text(text = "Go to Camera")
        }

        Button(onClick = {
            openMorseAction()
        }) {
            Text(text = "Go to Morse")
        }

        Button(onClick = {
            openSkinAction()
        }) {
            Text(text = "Go to Skin")
        }

        Button(onClick = {
            openColorAction()
        }) {
            Text(text = "Go to brightness")
        }

        Button(onClick = {
            openRemoveAdsAction()
        }) {
            Text(text = "Go to Pro")
        }

        Button(onClick = {
            openCompassAction()
        }) {
            Text(text = "Go to Compass")

        }
    }




}