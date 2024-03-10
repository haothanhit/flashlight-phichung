package flashlight.phichung.com.torch.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


object HomeNavigation {

   // const val addressArg = "parametersArg"
    const val route = "home"

//    fun createRoute(addresses: Address): String {
//        val encoded = Uri.encode(addresses.toString())
//        val route = "addressBook?${addressArg}=$encoded"
//        Log.e("frank", "route $route")
//        return route
//    }
//
//    fun fromNav(navBackStackEntry: NavBackStackEntry): Address? {
//        return navBackStackEntry.arguments?.getParcelable(addressArg)
//    }

}

@Composable
fun  HomeScreen(
    viewModel: HomeViewModel =  hiltViewModel<HomeViewModel>(),
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
    }


}