package flashlight.phichung.com.torch.ui.activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import flashlight.phichung.com.torch.ui.screen.home.HomeNavigation
import flashlight.phichung.com.torch.ui.screen.home.HomeScreen
import flashlight.phichung.com.torch.ui.screen.settings.SettingsNavigation
import flashlight.phichung.com.torch.ui.screen.settings.SettingsScreen


@Composable
fun FlashlightApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeNavigation.route
    ) {
        composable(HomeNavigation.route) {
            HomeScreen(
                openSkinAction = { -> },
                openSettingsAction = { ->
                    navController.navigate(SettingsNavigation.route)
                },
                openColorAction = { -> },
                openCompassAction = { -> },
                openMorseAction = { -> },
                openRemoveAdsAction = { -> },
                openCameraAction = { -> })
        }

        composable(SettingsNavigation.route) {
            SettingsScreen()
        }
    }
}
