package flashlight.phichung.com.torch.ui.screen.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


object SettingsNavigation {

    const val titleScreen = "Settings"

    // const val addressArg = "parametersArg"
    const val route = "settings"

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>(),
    navController: NavHostController = rememberNavController()
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = {
                Text(text = SettingsNavigation.titleScreen, textAlign = TextAlign.Center)
            })
        },
        content = ({

        })

    )

}