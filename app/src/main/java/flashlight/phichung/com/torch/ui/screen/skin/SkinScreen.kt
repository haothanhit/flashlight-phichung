package flashlight.phichung.com.torch.ui.screen.skin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import flashlight.phichung.com.torch.ui.theme.IconWhiteColor
import flashlight.phichung.com.torch.ui.theme.TextWhiteColor


object SkinNavigation {

    const val titleScreen = "Skin"
    const val route = "skin"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinScreen(
    viewModel: SkinViewModel = hiltViewModel<SkinViewModel>(),
    navController: NavHostController = rememberNavController()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = SkinNavigation.titleScreen,
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
                    containerColor = MaterialTheme.colorScheme.primary
                )


            )
        },
        content = ({

        }),

        )

}

