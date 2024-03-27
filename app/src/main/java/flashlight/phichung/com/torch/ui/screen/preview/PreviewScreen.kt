package flashlight.phichung.com.torch.ui.screen.preview

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import flashlight.phichung.com.torch.ui.components.NavigationIcon
import java.io.File


object PreviewNavigation {

    const val pathArg = "path"

    const val titleScreen = "Preview"
    const val route = "preview/{$pathArg}"
    fun createRoute(path: String) = "preview/${Uri.encode(path)}"


}

@Composable
fun PreviewScreen(
    viewModel: PreviewViewModel = hiltViewModel<PreviewViewModel>(),
    navController: NavHostController = rememberNavController(),

    ) {
    val uiState by viewModel.uiState.collectAsState()
    val intentSenderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            navController.navigateUp()
        }
        Log.d("INTENT SENDER", "RESULT: ${it.resultCode}")
    }

    Log.d("PREVIEW SCREEN", "OPA")

    when (val result: PreviewUiState = uiState) {
        PreviewUiState.Initial -> {}
        PreviewUiState.Empty -> {
            Log.d("PreviewUiState", "Empty")

        }
        is PreviewUiState.Ready -> {
            val context = LocalContext.current

            Column {
                PreviewTopAppBar(
                    onBackPressed = {navController.navigateUp() },
                    onDeleteClick = {
                        viewModel.deleteFile(context, intentSenderLauncher, result.file)
                    }
                )
                PreviewImageSection(result.file)

            }
        }

        PreviewUiState.Deleted -> LaunchedEffect(Unit) {
            navController.navigateUp()
        }
    }
}

@Composable
fun PreviewTopAppBar(
    onBackPressed: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        NavigationIcon(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.1F), CircleShape),
            icon = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            onClick = onBackPressed
        )
        NavigationIcon(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.1F), CircleShape),
            icon = Icons.Filled.Delete,
            contentDescription = "Delete",
            onClick = onDeleteClick
        )
    }
}

@Composable
private fun PreviewImageSection(file: File) {
    AsyncImage(
        modifier = Modifier.fillMaxSize(),
        model = file,
        contentScale = ContentScale.Fit,
        contentDescription = file.name,
    )
}
